package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JHResponse;
import com.mmall.vo.OrderPaySuccessInfo;
import com.mmall.vo.OrderProductVO;
import com.mmall.vo.OrderVO;
import com.mmall.vo.PayOrderVO;

import java.util.Map;

public interface IOrderService {

	JHResponse<PayOrderVO> pay(Integer userId, Long orderNO, String path);

	JHResponse aliCallback(Map<String, String> params);

	JHResponse<OrderPaySuccessInfo> queryOrderPayStatus(Integer userId, Long orderNO);

	JHResponse<OrderVO> createOrder(Integer userId, Integer shippingId);

	JHResponse cancelOrder(Integer userId, Long orderId);

	JHResponse<OrderProductVO> getOrderCartProducts(Integer userId);

	JHResponse<OrderVO> getOrderDetail(Integer userId, Long orderNO);

	JHResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

	JHResponse<PageInfo> adminList(int pageNum, int pageSize);

	JHResponse<OrderVO> adminDetail(Long orderNO);

	JHResponse<PageInfo> adminSearch(Long orderNO, int pageNum, int pageSize);

	JHResponse adminSendGoods(Long orderNO);
}
