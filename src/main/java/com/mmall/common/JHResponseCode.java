package com.mmall.common;

public enum JHResponseCode {

	Success(0, "返回成功"),
	ReLogin(1000, "重新登录"),
	InvalidArgument(1001, "参数错误"),
	Error(1, "返回失败"),

	Error_UsernameUnExist(2000001, "用户名不存在"),
	Error_PasswordError(2000002, "密码错误"),
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


	Error_UnAdminLogin(0, "非管理员不能登录"),

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
