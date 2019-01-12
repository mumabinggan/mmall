package com.mmall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.google.common.collect.Lists;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.dao.OrderItemMapper;
import com.mmall.dao.OrderMapper;
import com.mmall.dao.PayInfoMapper;
import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import com.mmall.pojo.PayInfo;
import com.mmall.service.IOrderService;
import com.mmall.utils.JHBigDecimalUtils;
import com.mmall.utils.JHDateTimeUtil;
import com.mmall.utils.JHFTPUtils;
import com.mmall.utils.JHPropertiesUtil;
import com.mmall.vo.OrderPaySuccessInfo;
import com.mmall.vo.PayOrderVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private PayInfoMapper payInfoMapper;

	// 支付宝当面付2.0服务
	private static AlipayTradeService tradeService;

	public JHResponse<PayOrderVO> pay(Integer userId, Long orderNO, String path) {
		Order order = orderMapper.selectByUserIdAndOrderNO(userId, orderNO);
		if (order == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetOrderError);
		}

		String qrUrl = this.precreate_trade(userId, order, path);
		if (qrUrl == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetOrderQrUrlError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_GetOrderPayInfoSuccess);
	}


	// 当面付2.0生成支付二维码
	public String precreate_trade(Integer userId, Order order, String path) {
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
		// 需保证商户系统端不能重复，建议通过数据库sequence生成，
		String outTradeNo = Long.toString(order.getOrderNo());

		// (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
		String subject = new StringBuilder().append("happymmall扫码支付，订单号:").append(outTradeNo).toString();

		// (必填) 订单总金额，单位为元，不能超过1亿元
		// 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
		String totalAmount = order.getPayment().toString();

		// (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
		// 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
		String undiscountableAmount = "0";

		// 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
		// 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
		String sellerId = "";

		// 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
		String body = new StringBuilder().append("订单").
				append(outTradeNo).append("购买商品共").
				append(totalAmount).append("元").toString();

		// 商户操作员编号，添加此参数可以为商户操作员做销售统计
		String operatorId = "test_operator_id";

		// (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
		String storeId = "test_store_id";

		// 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
		ExtendParams extendParams = new ExtendParams();
		extendParams.setSysServiceProviderId("2088100200300400500");

		// 支付超时，定义为120分钟
		String timeoutExpress = "120m";

		// 商品明细列表，需填写购买商品详细信息，
		List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
		List<OrderItem> orderItems = orderItemMapper.selectByUserIdAndOrderNO(userId, order.getOrderNo());
		for (OrderItem item : orderItems) {
			// 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
			GoodsDetail goods = GoodsDetail.newInstance(item.getOrderNo().toString(),
					item.getProductName(),
					JHBigDecimalUtils.mul(item.getCurrentUnitPrice().doubleValue(), 100).longValue(),
					item.getQuantity());
			// 创建好一个商品后添加至商品明细列表
			goodsDetailList.add(goods);
		}

		// 创建扫码支付请求builder，设置请求参数
		AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
				.setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
				.setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
				.setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
				.setTimeoutExpress(timeoutExpress)
				.setNotifyUrl(JHPropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
				.setGoodsDetailList(goodsDetailList);

		if (tradeService == null) {
			/** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
			 *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
			 */
			Configs.init("zfbinfo.properties");

			/** 使用Configs提供的默认参数
			 *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
			 */
			tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
		}

		AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
		switch (result.getTradeStatus()) {
			case SUCCESS:
				logger.info("支付宝预下单成功: )");

				AlipayTradePrecreateResponse response = result.getResponse();
				dumpResponse(response);

				File folder = new File(path);
				if (!folder.exists()) {
					folder.setWritable(true);
					folder.mkdirs();
				}

				// 需要修改为运行机器上的路径
				String qrPath = String.format(path + "/qr-%s.png",
						response.getOutTradeNo());
				String fileName = String.format("qr-%s.png",
						response.getOutTradeNo());
				ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

				File targetFile = new File(path, fileName);
				JHFTPUtils.uploadFiles(Lists.newArrayList(targetFile));
				logger.info("filePath:" + qrPath);
				String qrUrl = JHPropertiesUtil.getProperty("ftp.server.http.prefix");
				return qrUrl;
			case FAILED:
				logger.error("支付宝预下单失败!!!");
				break;
			case UNKNOWN:
				logger.error("系统异常，预下单状态未知!!!");
				break;
			default:
				logger.error("不支持的交易状态，交易返回异常!!!");
				break;
		}
		return null;
	}

	private void dumpResponse(AlipayResponse response) {
		if (response != null) {
			logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
			if (StringUtils.isNotEmpty(response.getSubCode())) {
				logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
						response.getSubMsg()));
			}
			logger.info("vbody:" + response.getBody());
		}
	}

	public JHResponse aliCallback(Map<String, String> params) {
		Long orderNO = Long.parseLong(params.get("out_trade_no"));
		String tradeNo = params.get("trade_no");
		String tradeStatus = params.get("trade_status");
		Order order = orderMapper.selectByOrderNO(orderNO);
		if (order == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetOrderUnExist);
		}
		if (order.getStatus() >= JHConst.Order.StatusEnum.Paid.getCode()) {
			return JHResponse.createBySuccess(JHResponseCode.Success_AliPayRepeatCallbackSuccess);
		}
		if (JHConst.TradeStatus.AliPay.Response_Success.equals(tradeStatus)) {
			order.setPaymentTime(JHDateTimeUtil.strToDate(params.get("gmt_payment")));
			order.setStatus(JHConst.Order.StatusEnum.Paid.getCode());
			int count = orderMapper.updateByPrimaryKeySelective(order);
			if (count < 0) {
				return JHResponse.createByError(JHResponseCode.Error_UpdateOrderStatusError);
			}
		}

		PayInfo payInfo = new PayInfo(order.getUserId(),
				orderNO, JHConst.Pay.PlatformEnum.Alipay.getCode(),
				tradeNo, tradeStatus);
		int count = payInfoMapper.insert(payInfo);
		if (count < 0) {
			return JHResponse.createByError(JHResponseCode.Error_SavePayInfoError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_SavePayInfoSuccess);
	}

	public JHResponse<OrderPaySuccessInfo> queryOrderPayStatus(Integer userId, Long orderNO) {
		Order order = orderMapper.selectByUserIdAndOrderNO(userId, orderNO);
		if (order == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetOrderError);
		}
		if (order.getStatus() >= JHConst.Order.StatusEnum.Paid.getCode()) {
			return JHResponse.createBySuccess(JHResponseCode.Success_GetOrderPayInfoSuccess,
					new OrderPaySuccessInfo(true));
		}
		return JHResponse.createByError(JHResponseCode.Error_GetPayFailInfoError);
	}
}
