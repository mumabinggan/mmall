package com.mmall.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import sun.security.krb5.internal.crypto.Des;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public interface ProductStatus {
		int OnSale = 1;		//在售
		int OffSale = 2;	//下架
	}

	public interface ProductOrderBy {
		int PriceAsc = 0;
		int PriceDesc = 1;
		HashMap<Integer, String> PriceOrderByMap = new HashMap<Integer, String>() {
			{
				put(PriceDesc, "price_desc");
				put(PriceAsc, "price_asc");
			}
		};
	}

	public interface Cart {
		int UnCheck = 0;	//没有选中
		int Check = 1;		//选中

		int UnLimitQuantity = 0;	//没有限制
		int LimitQuantity = 1;		//限制
	}
}
