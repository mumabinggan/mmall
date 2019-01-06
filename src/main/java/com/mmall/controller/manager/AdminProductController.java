package com.mmall.controller.manager;


import com.github.pagehelper.PageInfo;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private IProductService iProductService;

	/**
	 * 发布商品
	 * @param product
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "productSave.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse productSave(Product product, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.saveOrUpdateProduct(product);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 设置商品状态
	 * @param productId
	 * @param status
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "setSaleStatus.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse setSaleStatus(Integer productId, Integer status, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.setSaleStatus(productId, status);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 得到商品详情
	 * @param productId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getProductDetail.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse getProductDetail(Integer productId, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.getProductDetail(productId, true);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 得到商品列表
	 * @param pageNum
	 * @param pageSize
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getProductList.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<PageInfo> getProductList(@RequestParam(value = "pageNum", defaultValue = "1")
														   Integer pageNum,
											   @RequestParam(value = "pageSize", defaultValue = "20")
													   Integer pageSize,
											   HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.getProductList(pageNum, pageSize);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 搜索商品列表
	 * @param productName
	 * @param productId
	 * @param pageNum
	 * @param pageSize
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "searchProductList.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<PageInfo> searchProductListByProductNameAndProductId(String productName,
																		   Integer productId,
																		   @RequestParam(value = "pageNum", defaultValue = "1")
																				   Integer pageNum,
																		   @RequestParam(value = "pageSize", defaultValue = "20")
																				   Integer pageSize,
																		   HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.searchProductListByProductNameAndProductId(productName,
					productId, pageNum, pageSize);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}
}
