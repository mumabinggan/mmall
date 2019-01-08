package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.Shipping;

public interface IShippingService {

	JHResponse<Shipping> add(Integer userId, Shipping shipping);

	JHResponse<Shipping> update(Integer userId, Shipping shipping);

	JHResponse delete(Integer userId, Integer shippingId);

	JHResponse<Shipping> select(Integer userId, Integer shippingId);

	JHResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
