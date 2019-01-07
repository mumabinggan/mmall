package com.mmall.controller.user;

import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {

	@Autowired
	private ICartService iCartService;

	/**
	 * 增加商品到购物车
	 * @param productId
	 * @param quantity
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "addProductToCart.do")
	@ResponseBody
	public JHResponse<CartVO> addProductToCart(Integer productId, Integer quantity, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iCartService.add(user.getId(), productId, quantity);
	}

	/**
	 * 更新购物车中商品数量
	 * @param productId
	 * @param quantity
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "updateProductFromCart.do")
	@ResponseBody
	public JHResponse<CartVO> updateProductFromCart(Integer productId, Integer quantity, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iCartService.update(user.getId(), productId, quantity);
	}

	/**
	 * 从购物车中删除商品
	 * @param productIds
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "deleteProductsFromCart.do")
	@ResponseBody
	public JHResponse<CartVO> deleteProductsFromCart(String productIds, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iCartService.deleteProducts(user.getId(), productIds);
	}
}
