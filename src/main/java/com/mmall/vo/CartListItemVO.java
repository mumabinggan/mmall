package com.mmall.vo;

import com.mmall.common.JHConst;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.utils.JHBigDecimalUtils;
import com.mmall.utils.JHPropertiesUtil;

import java.math.BigDecimal;

public class CartListItemVO {

	private Integer id;
	private Integer userId;
	private Integer productId;
	private Integer quantity;	//购物车中此商品的数量
	private String productName;
	private String productSubTitle;
	private String productMainImage;
	private BigDecimal productPrice;
	private Integer productStatus;
	private BigDecimal productTotalPrice;
	private Integer productStock;
	private Integer productChecked;	//此商品是否勾选

	private Integer limitQuantity;	//限制数量的返回结果
	private String imageHost = JHPropertiesUtil.getProperty("ftp.server.http.prefix");

	public CartListItemVO(Product product, Cart cart) {
		this.id = cart.getId();
		this.userId = cart.getUserId();
		this.productId = cart.getProductId();
		this.productName = product.getName();
		this.productSubTitle = product.getSubtitle();
		this.productMainImage = product.getMainImage();
		this.productPrice = product.getPrice();
		this.productStatus = product.getStatus();
		this.productStock = product.getStock();
		this.productTotalPrice = JHBigDecimalUtils.mul(cart.getQuantity(), product.getPrice().doubleValue());
		this.limitQuantity =
				(product.getStock() > cart.getQuantity()) ? JHConst.Cart.UnLimitQuantity : JHConst.Cart.LimitQuantity;
		this.productChecked = cart.getChecked();
		this.quantity = (product.getStock() > cart.getQuantity()) ? cart.getQuantity() : product.getStock();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSubTitle() {
		return productSubTitle;
	}

	public void setProductSubTitle(String productSubTitle) {
		this.productSubTitle = productSubTitle;
	}

	public String getProductMainImage() {
		return productMainImage;
	}

	public void setProductMainImage(String productMainImage) {
		this.productMainImage = productMainImage;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}

	public BigDecimal getProductTotalPrice() {
		return productTotalPrice;
	}

	public void setProductTotalPrice(BigDecimal productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}

	public Integer getProductStock() {
		return productStock;
	}

	public void setProductStock(Integer productStock) {
		this.productStock = productStock;
	}

	public Integer getProductChecked() {
		return productChecked;
	}

	public void setProductChecked(Integer productChecked) {
		this.productChecked = productChecked;
	}

	public Integer getLimitQuantity() {
		return limitQuantity;
	}

	public void setLimitQuantity(Integer limitQuantity) {
		this.limitQuantity = limitQuantity;
	}
}
