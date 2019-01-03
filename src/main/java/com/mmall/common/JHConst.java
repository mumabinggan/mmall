package com.mmall.common;

public class JHConst {
	public static String SessionUserKey = "kSessionUserKey";

	public interface Role {
		int Custom = 0;		//普通用户
		int Admin = 1;		//管理员
	}

	public interface CheckUserValidType {
		int Username = 0;	//用户名
		int Email = 1;		//邮箱
	}
}
