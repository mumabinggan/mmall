package com.mmall.controller.user;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.service.impl.ShippingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

	@Autowired
	private IShippingService iShippingService;

	/**
	 * 添加地址
	 * @param shipping
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "addShipping.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<Shipping> add(Shipping shipping, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iShippingService.add(user.getId(), shipping);
	}

	/**
	 * 更新地址
	 * @param shipping
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "updateShipping.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<Shipping> update(Shipping shipping, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iShippingService.update(user.getId(), shipping);
	}

	/**
	 * 删除地址
	 * @param shippingId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "deleteShipping.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse delete(Integer shippingId, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iShippingService.delete(user.getId(), shippingId);
	}

	/**
	 * 得到某个地址详细信息
	 * @param shippingId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "selectShipping.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<Shipping> select(Integer shippingId, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iShippingService.select(user.getId(), shippingId);
	}

	/**
	 * 得到地址列表
	 * @param pageNum
	 * @param pageSize
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getShippingList.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<PageInfo> list(Integer pageNum, Integer pageSize, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iShippingService.list(user.getId(), pageNum, pageSize);
	}
}
