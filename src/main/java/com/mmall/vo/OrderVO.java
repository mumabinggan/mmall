package com.mmall.vo;

import com.google.common.collect.Lists;
import com.mmall.common.JHConst;
import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import com.mmall.pojo.Shipping;
import com.mmall.utils.JHDateTimeUtil;
import com.mmall.utils.JHPropertiesUtil;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;
import java.util.List;

public class OrderVO {

	private Long orderNo;

	private BigDecimal payment;

	private Integer paymentType;

	private String paymentTypeDesc;
	private Integer postage;

	private Integer status;

	private String statusDesc;

	private String paymentTime;

	private String sendTime;

	private String endTime;

	private String closeTime;

	private String createTime;

	//订单的明细
	private List<OrderItemVO> orderItemVoList;

	private Integer shippingId;
	private String receiverName;

	private ShippingVO shippingVo;

	private String imageHost = JHPropertiesUtil.getProperty("ftp.server.http.prefix");

	public OrderVO(Order order, List<OrderItem> itemList, Shipping shipping) {
		this.orderNo = order.getOrderNo();
		this.payment = order.getPayment();
		this.paymentType = order.getPaymentType();
		this.paymentTypeDesc = JHConst.Pay.TypeEnum.codeOf(order.getPaymentType()).getName();
		this.postage = order.getPostage();
		this.status = order.getStatus();
		this.statusDesc = JHConst.Order.StatusEnum.codeOf(order.getStatus()).getValue();
		this.shippingId = order.getShippingId();
		if (shipping != null) {
			this.setReceiverName(shipping.getReceiverName());
			this.shippingVo = new ShippingVO(shipping);
		}
		this.paymentTime = JHDateTimeUtil.dateToStr(order.getPaymentTime());
		this.sendTime = JHDateTimeUtil.dateToStr(order.getSendTime());
		this.endTime = JHDateTimeUtil.dateToStr(order.getEndTime());
		this.closeTime = JHDateTimeUtil.dateToStr(order.getCloseTime());
		this.createTime = JHDateTimeUtil.dateToStr(order.getCreateTime());

		List<OrderItemVO> orderItemVOList = Lists.newArrayList();
		for (OrderItem item : itemList) {
			orderItemVOList.add(new OrderItemVO(item));
		}
		this.orderItemVoList = orderItemVOList;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentTypeDesc() {
		return paymentTypeDesc;
	}

	public void setPaymentTypeDesc(String paymentTypeDesc) {
		this.paymentTypeDesc = paymentTypeDesc;
	}

	public Integer getPostage() {
		return postage;
	}

	public void setPostage(Integer postage) {
		this.postage = postage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<OrderItemVO> getOrderItemVoList() {
		return orderItemVoList;
	}

	public void setOrderItemVoList(List<OrderItemVO> orderItemVoList) {
		this.orderItemVoList = orderItemVoList;
	}

	public Integer getShippingId() {
		return shippingId;
	}

	public void setShippingId(Integer shippingId) {
		this.shippingId = shippingId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public ShippingVO getShippingVo() {
		return shippingVo;
	}

	public void setShippingVo(ShippingVO shippingVo) {
		this.shippingVo = shippingVo;
	}
}
