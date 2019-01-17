package com.mmall.vo;

import com.google.common.collect.Lists;
import com.mmall.pojo.OrderItem;
import com.mmall.utils.JHBigDecimalUtils;
import com.mmall.utils.JHPropertiesUtil;

import java.math.BigDecimal;
import java.util.List;

public class OrderProductVO {

	private List<OrderItemVO> orderItemVoList;

	private BigDecimal productTotalPrice;

	private String imageHost = JHPropertiesUtil.getProperty("ftp.server.http.prefix");

	public OrderProductVO(List<OrderItem> itemList) {
		BigDecimal totalPrice = JHBigDecimalUtils.BigDecimalWithDouble(0);
		List<OrderItemVO> orderItemVOList = Lists.newArrayList();
		for (OrderItem item : itemList) {
			orderItemVOList.add(new OrderItemVO(item));
			totalPrice = JHBigDecimalUtils.add(totalPrice.doubleValue(),
					item.getQuantity() * item.getCurrentUnitPrice().doubleValue());
		}
		this.orderItemVoList = orderItemVOList;
		this.productTotalPrice = totalPrice;
	}

	public List<OrderItemVO> getOrderItemVoList() {
		return orderItemVoList;
	}

	public void setOrderItemVoList(List<OrderItemVO> orderItemVoList) {
		this.orderItemVoList = orderItemVoList;
	}

	public BigDecimal getProductTotalPrice() {
		return productTotalPrice;
	}

	public void setProductTotalPrice(BigDecimal productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}
}
