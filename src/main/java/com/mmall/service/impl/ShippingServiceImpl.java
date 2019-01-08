package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

	@Autowired
	private ShippingMapper shippingMapper;

	public JHResponse<Shipping> add(Integer userId, Shipping shipping) {
		if (userId == null || shipping == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		shipping.setUserId(userId);
		int count = shippingMapper.insert(shipping);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_AddShippingError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_AddShippingSuccess, shipping);
	}

	public JHResponse<Shipping> update(Integer userId, Shipping shipping) {
		if (userId == null || shipping == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		shipping.setUserId(userId);
		int count = shippingMapper.updateByShipping(shipping);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_UpdateShippingError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_UpdateShippingSuccess, shipping);
	}

	public JHResponse delete(Integer userId, Integer shippingId) {
		if (userId == null || shippingId == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		int count = shippingMapper.deleteByUserIdAndId(userId, shippingId);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_DeleteShippingError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_DeleteShippingSuccess);
	}

	public JHResponse<Shipping> select(Integer userId, Integer shippingId) {
		if (userId == null || shippingId == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		Shipping shipping = shippingMapper.selectByUserIdAndId(userId, shippingId);
		if (shipping == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetShippingError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_GetShippingSuccess);
	}

	public JHResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
		if (userId == null || pageNum == null || pageSize == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Shipping> list = shippingMapper.selectByUserId(userId);
		PageInfo pageResult = new PageInfo(list);
		return JHResponse.createBySuccess(JHResponseCode.Success_GetShippingListSuccess, pageResult);
	}
}
