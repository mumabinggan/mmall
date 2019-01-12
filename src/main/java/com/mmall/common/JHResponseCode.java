package com.mmall.common;

public enum JHResponseCode {

	Success(0, "返回成功"),
	ReLogin(1000, "重新登录"),
	InvalidArgument(1001, "参数错误"),
	Error(1, "返回失败"),

	Error_UsernameUnExist(2000001, "用户名不存在"),
	Error_PasswordError(2000002, "密码错误"),
	Error_UserUnExist(2000003, "用户不存在"),
	Success_LoginSuccess(0, "登录成功"),

	Error_UsernameIsEmpty(2001001, "用户名为空"),
	Error_EmailIsEmpty(2001002, "邮箱为空"),
	Error_RegisterError(2001003, "邮箱为空"),
	Error_EmailHasExist(2001004, "邮箱已经存在"),
	Error_UsernameHasExist(2001005, "用户名已经存在"),
	Error_UserValidTypeUnExist(2001006, "验证类型不存在"),
	Success_RegisterSuccess(2001100, "注册成功"),

	Error_GetUserInfoError(0, "得到用户信息失败"),
	Success_GetUserInfoSuccess(0, "得到用户信息成功"),

	Success_GetQuestionSuccess(0, "得到问题成功"),

	Error_TokenIsEmpty(2002100, "Token为空"),
	Success_CheckQuestionAndAnswerSuccess(0, "问题答案验证成功"),

	Error_NewPasswordIsEmpty(2003101, "新密码为空"),
	Error_UpdateNewPasswordError(2003102, "更新新密码失败"),
	Success_UpdateNewPasswordSuccess(0, "更新新密码成功"),

	Error_EmailUnExist(2004101, "邮箱已经存在"),
	Error_UpdateUserInfoError(0, "更新用户信息失败"),
	Success_UpdateUserInfoSuccess(0, "更新用户信息成功"),


	Error_UnAdminLogin(2005101, "非管理员不能登录"),

	Error_GetSubCategoryListError(2006101, "得到子分类列表失败"),
	Success_GetSubCategoryListSuccess(0, "得到子分类列表成功"),

	Error_AddCategoryError(2007102, "添加分类失败"),
	Success_AddCategorySuccess(0, "添加分类成功"),

	Error_UpdateCategoryError(2007102, "更新分类失败"),
	Success_UpdateCategorySuccess(0, "更新分类成功"),

	Success_GetDeepSubCategorySuccess(0, "得到子分类成功"),

	Error_AddProductError(2008101, "增加产品失败"),
	Success_AddProductSuccess(0, "增加产品成功"),

	Error_UpdateProductError(2009101, "更新产品失败"),
	Success_UpdateProductSuccess(0, "更新产品成功"),

	Error_SetProductStatusError(2010101, "更新产品状态失败"),
	Success_SetProductStatusSuccess(0, "更新产品状态成功"),

	Error_ProductUnExist(2011101, "商品不存在"),
	Error_ProductOffSale(2011102, "商品已下架"),
	Error_GetProductDetailError(2011102, "得到商品详情失败"),
	Success_GetProductDetailSuccess(0, "得到商品详情成功"),

	Error_GetProductListError(2011102, "得到商品列表失败"),
	Success_GetProductListSuccess(0, "得到商品列表成功"),

	Error_SearchProductListError(2012102, "搜索商品列表失败"),
	Success_SearchProductListSuccess(0, "搜索商品列表成功"),

	Error_CustomSearchProductListError(2013102, "搜索商品列表失败"),
	Success_CustomSearchProductListSuccess(0, "搜索商品列表成功"),

	Error_UploadFilesError(0, "上传文件失败"),
	Success_UploadFilesSuccess(0, "上传文件成功"),

	Error_AddProductToCartError(0, "增加购物车失败"),
	Success_AddProductToCartSuccess(0, "增加购物车成功"),

	Error_DeleteProductFromCartError(0, "从购物车中删除商品失败"),
	Success_DeleteProductFromCartSuccess(0, "从购物车中删除商品成功"),

	Error_GetProductListFromCartError(0, "得到商品列表失败"),
	Success_GetProductListFromCartSuccess(0, "得到商品列表成功"),

	Error_SetCheckedProductFromCartError(0, "设置商品选中失败"),
	Success_SetCheckedProductFromCartSuccess(0, "设置商品选中成功"),

	Error_GetProductCountInCartError(0, "得到购物车商品数量失败"),
	Success_GetProductCountInCartSuccess(0, "得到购物车商品数量成功"),

	Error_AddShippingError(0, "添加地址失败"),
	Success_AddShippingSuccess(0, "添加地址成功"),

	Error_UpdateShippingError(0, "更新地址失败"),
	Success_UpdateShippingSuccess(0, "更新地址成功"),

	Error_DeleteShippingError(0, "删除地址失败"),
	Success_DeleteShippingSuccess(0, "删除地址成功"),

	Error_GetShippingListError(0, "得到地址列表失败"),
	Success_GetShippingListSuccess(0, "得到地址列表成功"),

	Error_GetShippingError(0, "得到地址失败"),
	Success_GetShippingSuccess(0, "得到地址成功"),


	Error_GetOrderError(0, "得到订单失败"),
	Error_GetOrderUnExist(0, "订单不存在"),
	Error_GetOrderQrUrlError(0, "得到订单支付宝二维码失败"),
	Success_GetOrderPayInfoSuccess(0, "得到订单支付信息成功"),

	Error_CheckAlipayCallbackError(0, "支付宝回调失败"),
	Success_CheckAlipayCallbackSuccess(0, "支付宝回调成功"),

	Success_AliPayRepeatCallbackSuccess(0, "支付宝重复回调"),

	Error_UpdateOrderStatusError(0, "更新订单状态失败"),

	Error_SavePayInfoError(0, "保存支付信息失败"),
	Success_SavePayInfoSuccess(0, "保存支付信息成功"),

	Error_GetPayFailInfoError(0, "没有支付成功"),
	Success_GetPaySuccessInfoSuccess(0, "支付成功"),

	Error_XXX(2000001, "用户名不存在");

	private final int code;
	private final String msg;

	public static int successMaxCode = 999;

	JHResponseCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
