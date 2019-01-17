package com.mmall.vo;

import com.mmall.pojo.OrderItem;
import com.mmall.utils.JHDateTimeUtil;
import com.mmall.utils.JHPropertiesUtil;

import java.math.BigDecimal;

public class OrderItemVO {

	private Long orderNo;

	private Integer productId;

	private String productName;
	private String productImage;

	private BigDecimal currentUnitPrice;

	private Integer quantity;

	private BigDecimal totalPrice;

	private String createTime;

	public OrderItemVO(OrderItem orderItem) {
		this.orderNo = orderItem.getOrderNo();
		this.productId = orderItem.getProductId();
		this.productName = orderItem.getProductName();
		this.productImage = orderItem.getProductImage();
		this.currentUnitPrice = orderItem.getCurrentUnitPrice();
		this.quantity = orderItem.getQuantity();
		this.totalPrice = orderItem.getTotalPrice();
		this.createTime = JHDateTimeUtil.dateToStr(orderItem.getCreateTime());
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public BigDecimal getCurrentUnitPrice() {
		return currentUnitPrice;
	}

	public void setCurrentUnitPrice(BigDecimal currentUnitPrice) {
		this.currentUnitPrice = currentUnitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
