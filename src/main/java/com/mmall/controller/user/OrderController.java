package com.mmall.controller.user;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.vo.OrderPaySuccessInfo;
import com.mmall.vo.OrderProductVO;
import com.mmall.vo.OrderVO;
import com.mmall.vo.PayOrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/order/")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private IOrderService iOrderService;

	/**
	 * 支付接口
	 * @param session
	 * @param orderNO
	 * @param request
	 * @return
	 */
	@RequestMapping("pay.do")
	@ResponseBody
	public JHResponse<PayOrderVO> pay(HttpSession session, Long orderNO, HttpServletRequest request) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		String path = request.getSession().getServletContext().getRealPath("upload");
		return iOrderService.pay(user.getId(), orderNO, path);
	}

	/**
	 * 支付宝支付回调
	 * @param request
	 * @return
	 */
	@RequestMapping("alipayCallback.do")
	@ResponseBody
	public Object alipayCallback(HttpServletRequest request) {
		Map<String, String> params = Maps.newHashMap();

		Map requestParams = request.getParameterMap();
		for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; ++i) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		logger.info("支付宝回调, sign:{}, trade_status:{}, 参数:{}",
				params.get("sign"),
				params.get("trade_status"),
				params.toString());

		//验证
		params.remove("sign");
		params.remove("sign_type");

		try {
			boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
			if (!alipayRSACheckedV2) {
				return JHResponse.createByError(JHResponseCode.Error_CheckAlipayCallbackError);
			}
		} catch (AlipayApiException e) {
			logger.error("验证错误", e);
		}

		//todo:验证各种数据
		JHResponse response = iOrderService.aliCallback(params);
		if (response.isSuccess()) {
			return JHConst.TradeStatus.AliPay.Response_Success;
		}
		return JHConst.TradeStatus.AliPay.Response_Failed;
	}

	/**
	 * 查询支付状态
	 * @param session
	 * @param orderNO
	 * @param request
	 * @return
	 */
	@RequestMapping("queryOrderPayStatus.do")
	@ResponseBody
	public JHResponse<OrderPaySuccessInfo> queryOrderPayStatus(HttpSession session, Long orderNO, HttpServletRequest request) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iOrderService.queryOrderPayStatus(user.getId(), orderNO);
	}

	/**
	 * 创建订单
	 * @param session
	 * @param shippingId
	 * @return
	 */
	@RequestMapping("createOrder.do")
	@ResponseBody
	public JHResponse<OrderVO> createOrder(HttpSession session, Integer shippingId) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iOrderService.createOrder(user.getId(), shippingId);
	}

	/**
	 * 取消订单
	 * @param session
	 * @param orderNO
	 * @return
	 */
	@RequestMapping("cancelOrder.do")
	@ResponseBody
	public JHResponse<OrderVO> cancelOrder(HttpSession session, Long orderNO) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iOrderService.cancelOrder(user.getId(), orderNO);
	}

	/**
	 * 得到购物车中勾选的信息
	 * @param session
	 * @return
	 */
	@RequestMapping("getOrderCartProducts.do")
	@ResponseBody
	public JHResponse<OrderProductVO> getOrderCartProducts(HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iOrderService.getOrderCartProducts(user.getId());
	}

	/**
	 * 得到订单详情
	 * @param session
	 * @param orderNO
	 * @return
	 */
	@RequestMapping("getOrderDetail.do")
	@ResponseBody
	public JHResponse<OrderVO> getOrderDetail(HttpSession session, Long orderNO) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iOrderService.getOrderDetail(user.getId(), orderNO);
	}

	/**
	 * 得到订单列表
	 * @param session
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("getOrderList.do")
	@ResponseBody
	public JHResponse<PageInfo> getOrderList(HttpSession session,
											 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
											 @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return iOrderService.list(user.getId(), pageNum, pageSize);
	}
}
