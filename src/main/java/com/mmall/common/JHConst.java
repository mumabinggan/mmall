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

	public interface Order {
		enum StatusEnum{
			Canceled(0,"已取消"),
			No_Pay(10,"未支付"),
			Paid(20,"已付款"),
			Shipped(40,"已发货"),
			Order_Success(50,"订单完成"),
			Order_Close(60,"订单关闭");

			StatusEnum(int code,String value){
				this.code = code;
				this.value = value;
			}
			private int code;
			private String value;

			public int getCode() {
				return code;
			}
			public String getValue() {
				return value;
			}
			public static StatusEnum codeOf(int code) {
				for(StatusEnum statusEnum : values()) {
					if(statusEnum.getCode() == code) {
						return statusEnum;
					}
				}
				throw new RuntimeException("么有找到对应的枚举");
			}
		}
	}

	public interface TradeStatus {
		interface AliPay {
			String Wait_Buyer_Pay = "WAIT_BUYER_PAY";
			String Trade_Success = "TRADE_SUCCESS";

			String Response_Success = "success";
			String Response_Failed = "failed";
		}
	}

	public interface Pay {
		enum PlatformEnum {
			Alipay(1, "支付宝"),
			Wechat(1, "微信");
			PlatformEnum(int code, String name) {
				this.code = code;
				this.name = name;
			}
			private int code;
			private String name;

			public int getCode() {
				return code;
			}

			public void setCode(int code) {
				this.code = code;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}
	}

}
