package com.mmall.controller.manager;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private IOrderService iOrderService;

	/**
	 * 得到订单列表(所有订单)
	 * @param session
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "getList.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<PageInfo> getOrderList(HttpSession session,
											 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
											 @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iOrderService.adminList(pageNum, pageSize);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 得到订单详情
	 * @param session
	 * @param orderNO
	 * @return
	 */
	@RequestMapping(value = "getOrderDetail.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<OrderVO> getOrderDetail(HttpSession session, Long orderNO) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iOrderService.adminDetail(orderNO);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 搜索
	 * @param session
	 * @param orderNO
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "search.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<PageInfo> search(HttpSession session,
											Long orderNO,
											@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
											@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iOrderService.adminSearch(orderNO, pageNum, pageSize);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 发货
	 * @param session
	 * @param orderNO
	 * @return
	 */
	@RequestMapping(value = "sendGoods.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<PageInfo> sendGoods(HttpSession session,
										  Long orderNO) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iOrderService.adminSendGoods(orderNO);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}
}
