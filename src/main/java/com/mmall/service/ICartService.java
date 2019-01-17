package com.mmall.service;

import com.mmall.common.JHResponse;
import com.mmall.pojo.Cart;
import com.mmall.vo.CartListItemVO;
import com.mmall.vo.CartVO;

import java.math.BigDecimal;
import java.util.List;

public interface ICartService {

	JHResponse<CartVO> add(Integer userId, Integer productId, Integer quantity);

	JHResponse<CartVO> update(Integer userId, Integer productId, Integer quantity);

	JHResponse<CartVO> deleteProducts(Integer userId, String productIds);

	JHResponse<CartVO> list(Integer userId);

	JHResponse<CartVO> checkProduct(Integer userId, Integer productId, Integer checked);

	JHResponse<Integer> getProductCountFromCart(Integer userId);

	CartListItemVO cartListItemByCart(Cart cart);

	BigDecimal totalPrice(List<Cart> cartList);

	BigDecimal totalPriceOfCheckedProduct(Integer userId);
}
