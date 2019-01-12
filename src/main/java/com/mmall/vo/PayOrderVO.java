package com.mmall.vo;

public class PayOrderVO {

	private String imagePath;

	private Long orderNo;

	public PayOrderVO(String imagePath, Long orderNo) {
		this.imagePath = imagePath;
		this.orderNo = orderNo;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
}
