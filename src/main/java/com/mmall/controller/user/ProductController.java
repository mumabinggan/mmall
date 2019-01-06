package com.mmall.controller.user;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.User;
import com.mmall.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/product/")
public class ProductController {

	@Autowired
	private ProductServiceImpl iProductService;

	/**
	 * 得到商品详情
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "getProductDetail.do")
	@ResponseBody
	public JHResponse getProductDetail(Integer productId) {
		return iProductService.getProductDetail(productId, false);
	}

	/**
	 * 搜索商品
	 * @param keyword
	 * @param categoryId
	 * @param orderBy
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "searchProductList.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<PageInfo> searchProductList(String keyword,
												  Integer categoryId,
												  Integer orderBy,
												  @RequestParam(value = "pageNum", defaultValue = "1")
															  Integer pageNum,
												  @RequestParam(value = "pageSize", defaultValue = "20")
															  Integer pageSize) {
		return iProductService.searchProductListByProductNameAndCategoryId(keyword,
				categoryId, orderBy, pageNum, pageSize);
	}
}
