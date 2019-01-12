package com.mmall.service;

import com.mmall.common.JHResponse;
import com.mmall.vo.OrderPaySuccessInfo;
import com.mmall.vo.PayOrderVO;

import java.util.Map;

public interface IOrderService {

	JHResponse<PayOrderVO> pay(Integer userId, Long orderNO, String path);

	JHResponse aliCallback(Map<String, String> params);

	JHResponse<OrderPaySuccessInfo> queryOrderPayStatus(Integer userId, Long orderNO);
}
