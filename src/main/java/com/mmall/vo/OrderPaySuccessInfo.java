package com.mmall.vo;

public class OrderPaySuccessInfo {

	private Boolean isSuccess;

	public OrderPaySuccessInfo(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Boolean getSuccess() {
		return isSuccess;
	}

	public void setSuccess(Boolean success) {
		isSuccess = success;
	}
}
