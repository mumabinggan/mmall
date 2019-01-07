package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JHResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.FileUploadVO;
import com.mmall.vo.ProductDetailVO;
import com.mmall.vo.ProductListItemVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {

	JHResponse saveOrUpdateProduct(Product product);

	JHResponse setSaleStatus(Integer productId, Integer status);

	JHResponse<ProductDetailVO> getProductDetail(Integer productId, boolean isAdmin);

	JHResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

	JHResponse<PageInfo> searchProductListByProductNameAndProductId(String productName,
																 	Integer productId,
																 	Integer pageNum,
																 	Integer pageSize);

	boolean isOnSale(Product product);

	JHResponse<PageInfo> searchProductListByProductNameAndCategoryId(String productName,
																	 Integer categoryId,
																	 Integer orderBy,
																	 Integer pageNum,
																	 Integer pageSize);

	JHResponse<FileUploadVO> uploadFiles(MultipartFile file, String path);
}
