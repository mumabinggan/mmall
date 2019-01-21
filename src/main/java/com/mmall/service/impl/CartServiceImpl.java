package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.utils.JHBigDecimalUtils;
import com.mmall.vo.CartListItemVO;
import com.mmall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
public class CartServiceImpl implements ICartService {

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private ProductMapper productMapper;

	public JHResponse<CartVO> add(Integer userId, Integer productId, Integer quantity) {
		if (userId == null || productId == null || quantity == null || quantity <= 0) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
		if (cart == null) {
			cart = new Cart();
			cart.setUserId(userId);
			cart.setProductId(productId);
			cart.setQuantity(quantity);
			cart.setChecked(JHConst.Cart.Check);
			cartMapper.insert(cart);
		} else {
			cart.setQuantity(cart.getQuantity() + quantity);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		CartVO cartVO = cartVOByUserId(userId);
		if (cartVO == null) {
			return JHResponse.createByError(JHResponseCode.Error_AddProductToCartError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_AddProductToCartSuccess, cartVO);
	}

	public CartListItemVO cartListItemByCart(Cart cart) {
		Product product = productMapper.selectByPrimaryKey(cart.getProductId());
		CartListItemVO cartListItemVO = new CartListItemVO(product, cart);
		return cartListItemVO;
	}

	public BigDecimal totalPrice(List<Cart> cartList) {
		if (cartList == null) {
			return null;
		}
		BigDecimal totalPrice = JHBigDecimalUtils.BigDecimalWithDouble(0);
		for (Cart item : cartList) {
			CartListItemVO cartListItemVO = this.cartListItemByCart(item);
			if (item.getChecked() == JHConst.Cart.Check) {
				totalPrice = JHBigDecimalUtils.add(totalPrice.doubleValue(), cartListItemVO.getProductTotalPrice().doubleValue());
			}
		}
		return totalPrice;
	}

	public BigDecimal totalPriceOfCheckedProduct(Integer userId) {
		List<Cart> cartList = cartMapper.selectCheckedByUserId(userId);
		return this.totalPrice(cartList);
	}

	private CartVO cartVOByUserId(Integer userId) {
		List<Cart> cartList = cartMapper.selectByUserId(userId);
		if (cartList == null) {
			return null;
		}
		List<CartListItemVO> cartListItemList = Lists.newArrayList();
		BigDecimal totalPrice = new BigDecimal("0");
		for (Cart item : cartList) {
			CartListItemVO cartListItemVO = this.cartListItemByCart(item);
			cartListItemList.add(cartListItemVO);
			if (item.getChecked() == JHConst.Cart.Check) {
				totalPrice = JHBigDecimalUtils.add(totalPrice.doubleValue(), cartListItemVO.getProductTotalPrice().doubleValue());
			}
		}
		CartVO cartVO = new CartVO(isAllChecked(userId), totalPrice, cartListItemList);
		return cartVO;
	}

	private boolean isAllChecked(Integer userId) {
		return (cartMapper.checkoutAllCheckedByUserId(userId) == 1);
	}

	public JHResponse<CartVO> update(Integer userId, Integer productId, Integer quantity) {
		return add(userId, productId, quantity);
	}

	public JHResponse<CartVO> deleteProducts(Integer userId, String productIds) {
		if (userId == null || productIds == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		List<String> deleteProductList = Splitter.on(",").splitToList(productIds);
		if (CollectionUtils.isEmpty(deleteProductList)) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		int count = cartMapper.deleteByUserIdAndProductIdList(userId, deleteProductList);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_DeleteProductFromCartError);
		}
		CartVO cartVO = cartVOByUserId(userId);
		if (cartVO == null) {
			return JHResponse.createByError(JHResponseCode.Error_DeleteProductFromCartError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_DeleteProductFromCartSuccess, cartVO);
	}

	public JHResponse<CartVO> list(Integer userId) {
		if (userId == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		CartVO cartVO = cartVOByUserId(userId);
		if (cartVO == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetProductListFromCartError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_GetProductListFromCartSuccess,
				cartVO);
	}

	public JHResponse<CartVO> checkProduct(Integer userId, Integer productId, Integer checked) {
		if (userId == null || checked == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		int count = cartMapper.updateCheckedByUserIdAndProductIdAndChecked(userId, productId, checked);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_SetCheckedProductFromCartError);
		}
		CartVO cartVO = cartVOByUserId(userId);
		if (cartVO == null) {
			return JHResponse.createByError(JHResponseCode.Error_SetCheckedProductFromCartError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_SetCheckedProductFromCartSuccess,
				cartVO);
	}

	public JHResponse<Integer> getProductCountFromCart(Integer userId) {
		if (userId == null) {
			return JHResponse.createBySuccess(JHResponseCode.Error_GetProductCountInCartError);
		}
		int count = cartMapper.selectProductCountFromCartByUserId(userId);
		return JHResponse.createBySuccess(JHResponseCode.Success_GetProductCountInCartSuccess, count);
	}
}
