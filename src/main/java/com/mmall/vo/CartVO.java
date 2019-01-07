package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVO {
	private Boolean allChecked;		//是否已经都勾选
	private BigDecimal cartTotalPrice;
	private List<CartListItemVO> cartProductVOList;

	public CartVO(Boolean allChecked, BigDecimal cartTotalPrice, List<CartListItemVO> cartProductVOList) {
		this.allChecked = allChecked;
		this.cartTotalPrice = cartTotalPrice;
		this.cartProductVOList = cartProductVOList;
	}

	public Boolean getAllChecked() {
		return allChecked;
	}

	public void setAllChecked(Boolean allChecked) {
		this.allChecked = allChecked;
	}

	public BigDecimal getCartTotalPrice() {
		return cartTotalPrice;
	}

	public void setCartTotalPrice(BigDecimal cartTotalPrice) {
		this.cartTotalPrice = cartTotalPrice;
	}

	public List<CartListItemVO> getCartProductVOList() {
		return cartProductVOList;
	}

	public void setCartProductVOList(List<CartListItemVO> cartProductVOList) {
		this.cartProductVOList = cartProductVOList;
	}
}
