package com.mmall.service;

import com.mmall.common.JHResponse;
import com.mmall.vo.CartVO;

public interface ICartService {

	JHResponse<CartVO> add(Integer userId, Integer productId, Integer quantity);

	JHResponse<CartVO> update(Integer userId, Integer productId, Integer quantity);

	JHResponse<CartVO> deleteProducts(Integer userId, String productIds);

	JHResponse<CartVO> list(Integer userId);

	JHResponse<CartVO> checkProduct(Integer userId, Integer productId, Integer checked);

	JHResponse<Integer> getProductCountFromCart(Integer userId);
}
