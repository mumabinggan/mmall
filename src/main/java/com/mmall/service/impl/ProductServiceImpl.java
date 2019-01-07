package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IFileUploadService;
import com.mmall.service.IProductService;
import com.mmall.utils.JHPropertiesUtil;
import com.mmall.utils.JHStringUtils;
import com.mmall.vo.FileUploadVO;
import com.mmall.vo.ProductDetailVO;
import com.mmall.vo.ProductListItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private ICategoryService iCategoryService;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private IFileUploadService iFileUploadService;

	@Override
	public JHResponse saveOrUpdateProduct(Product product) {
		if (product == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		if (product.getId() == null) {
			int count =	productMapper.insertSelective(product);
			if (count > 0) {
				return JHResponse.createBySuccess(JHResponseCode.Success_AddProductSuccess);
			} else {
				return JHResponse.createByError(JHResponseCode.Error_AddProductError);
			}
		} else {
			int count =	productMapper.updateByPrimaryKeySelective(product);
			if (count > 0) {
				return JHResponse.createBySuccess(JHResponseCode.Success_UpdateProductSuccess);
			} else {
				return JHResponse.createByError(JHResponseCode.Error_UpdateProductError);
			}
		}
	}

	@Override
	public JHResponse setSaleStatus(Integer productId, Integer status) {
		if (productId == null || status == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		Product product = new Product();
		product.setId(productId);
		product.setStatus(status);
		int count = productMapper.updateByPrimaryKeySelective(product);
		if (count > 0) {
			return JHResponse.createBySuccess(JHResponseCode.Success_SetProductStatusSuccess);
		} else {
			return JHResponse.createByError(JHResponseCode.Error_SetProductStatusError);
		}
	}

	@Override
	public JHResponse<ProductDetailVO> getProductDetail(Integer productId, boolean isAdmin) {
		if (productId == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null) {
			return JHResponse.createByError(JHResponseCode.Error_ProductUnExist);
		}

		Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
		if (category == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetProductDetailError);
		}
		ProductDetailVO productDetailVO = new ProductDetailVO(product, category);
		if (productDetailVO == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetProductDetailError);
		}
		if (!isAdmin) {
			if (isOnSale(product)) {
				JHResponse.createBySuccess(JHResponseCode.Success_GetProductDetailSuccess,
						productDetailVO);
			} else {
				return JHResponse.createByError(JHResponseCode.Error_ProductOffSale);
			}
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_GetProductDetailSuccess,
				productDetailVO);
	}

	@Override
	public boolean isOnSale(Product product) {
		return  (product.getStatus() == JHConst.ProductStatus.OnSale);
	}

	@Override
	public JHResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
		//1, startPage -- start
		//2, 填充自己的sql查询语句
		//3, pageHelper收尾
		PageHelper.startPage(pageNum, pageSize);
		List<Product> list = productMapper.selectList();
		if (list == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetProductListError);
		}
		List productListVOList = new ArrayList();
		for (Product item : list) {
			ProductListItemVO listItem = new ProductListItemVO(item);
			productListVOList.add(listItem);
		}
		PageInfo pageResult = new PageInfo(list);
		pageResult.setList(productListVOList);
		return JHResponse.createBySuccess(JHResponseCode.Success_GetProductListSuccess, pageResult);
	}

	@Override
	public JHResponse<PageInfo> searchProductListByProductNameAndProductId(String productName,
																		   Integer productId,
																		   Integer pageNum,
																		   Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		if (JHStringUtils.isNotBlank(productName)) {
			productName = new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Product> list = productMapper.selectListByProductNameAndProductId(productName, productId);
		if (list == null) {
			return JHResponse.createByError(JHResponseCode.Error_SearchProductListError);
		}
		List productListVOList = new ArrayList();
		for (Product item : list) {
			ProductListItemVO listItem = new ProductListItemVO(item);
			productListVOList.add(listItem);
		}
		PageInfo pageResult = new PageInfo(list);
		pageResult.setList(productListVOList);
		return JHResponse.createBySuccess(JHResponseCode.Success_SearchProductListSuccess, pageResult);
	}

	@Override
	public JHResponse<PageInfo> searchProductListByProductNameAndCategoryId(String productName,
																			Integer categoryId,
																			Integer orderBy,
																			Integer pageNum,
																			Integer pageSize) {
		if (JHStringUtils.isBlank(productName) && categoryId == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		if (JHStringUtils.isNotBlank(productName)) {
			productName = new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Integer> categoryIdList = Lists.newArrayList();
		if (categoryId != null) {
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if (category == null && JHStringUtils.isBlank(productName)) {
				PageHelper.startPage(pageNum, pageSize);
				List<ProductListItemVO> list = Lists.newArrayList();
				PageInfo pageInfo = new PageInfo(list);
				return JHResponse.createBySuccess(JHResponseCode.Success_CustomSearchProductListSuccess, pageInfo);
			}
			JHResponse<Set<Integer>> categoryIdSetResponse = iCategoryService.getDeepSubCategoryIdListByCategoryId(categoryId);
			if (categoryIdSetResponse.isSuccess()) {
				for (Integer item : categoryIdSetResponse.getData()) {
					categoryIdList.add(item);
				}
			}
		}
		List<Product> list = productMapper.selectListByProductNameAndCategoryIdList(productName, categoryIdList);
		PageHelper.startPage(pageNum, pageSize);
		if (orderBy != null) {
			String orderByStr = JHConst.ProductOrderBy.PriceOrderByMap.get(orderBy);
			if (JHStringUtils.isNotBlank(orderByStr)) {
				String[] orderByArray = orderByStr.split("_");
				if (orderByArray.length == 2) {
					PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
				}
			}
		}
		PageInfo pageInfo = new PageInfo(list);
		List<ProductListItemVO> itemVOList = Lists.newArrayList();
		for (Product item : list) {
			itemVOList.add(new ProductListItemVO(item));
		}
		pageInfo.setList(itemVOList);
		return JHResponse.createBySuccess(JHResponseCode.Success_CustomSearchProductListSuccess, pageInfo);
	}

	@Override
	public JHResponse<FileUploadVO> uploadFiles(MultipartFile file, String path) {
		String targetFileName = iFileUploadService.upload(file, path);
		if (JHStringUtils.isBlank(targetFileName)) {
			return JHResponse.createByError(JHResponseCode.Error_UploadFilesError);
		}
		String url = JHPropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
		return JHResponse.createBySuccess(JHResponseCode.Success_UploadFilesSuccess, new FileUploadVO(url, targetFileName));
	}
}
