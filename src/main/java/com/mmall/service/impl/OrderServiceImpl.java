package com.mmall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.Car;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.dao.*;
import com.mmall.pojo.*;
import com.mmall.service.ICartService;
import com.mmall.service.IOrderService;
import com.mmall.utils.JHBigDecimalUtils;
import com.mmall.utils.JHDateTimeUtil;
import com.mmall.utils.JHFTPUtils;
import com.mmall.utils.JHPropertiesUtil;
import com.mmall.vo.*;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private ShippingMapper shippingMapper;

	@Autowired
	private ICartService iCartService;

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private ProductMapper productMapper;

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

	public JHResponse<OrderVO> createOrder(Integer userId, Integer shippingId) {
		Shipping shipping = shippingMapper.selectByUserIdAndId(userId, shippingId);
		if (shipping == null) {
			return JHResponse.createByError(JHResponseCode.Error_CreateOrderShippingIdError);
		}

		//插入订单Item表
		List<Cart> cartList = cartMapper.selectCheckedByUserId(userId);
		if (cartList == null || CollectionUtils.isEmpty(cartList)) {
			return JHResponse.createByError(JHResponseCode.Error_CreateOrderCartUnCheckedProductError);
		}
		Long orderNO = this.generateOrderNo();
		List<OrderItem> orderItemList = Lists.newArrayList();
		for (Cart item : cartList) {
			Product product = productMapper.selectByPrimaryKey(item.getProductId());
			//检测是否在线售卖状态
			if (JHConst.ProductStatus.OnSale != product.getStatus()) {
				return JHResponse.createByError(JHResponseCode.Error.getCode(),
						"产品"+product.getName()+"不是在线售卖状态");
			}
			//检测库存
			if (product.getStock() < item.getQuantity()) {
				return JHResponse.createByError(JHResponseCode.Error.getCode(),
						"产品"+product.getName()+"库存不足");
			}
			CartListItemVO itemVO = iCartService.cartListItemByCart(item);
			OrderItem orderItem = new OrderItem(itemVO, orderNO);
			orderItemList.add(orderItem);
		}
		int count = orderItemMapper.batchInsert(orderItemList);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_CreateOrderError);
		}

		//插入订单表
		Order order = new Order();
		order.setOrderNo(orderNO);
		order.setUserId(userId);
		order.setShippingId(shippingId);
		BigDecimal totalPrice = iCartService.totalPrice(cartList);
		order.setPayment(totalPrice);
		order.setPaymentType(JHConst.Pay.TypeEnum.OnlinePay.getCode());
		order.setPostage(0);
		order.setStatus(JHConst.Order.StatusEnum.No_Pay.getCode());
		count = orderMapper.insert(order);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_CreateOrderError);
		}

		//减少库存
		List<Product> productList = Lists.newArrayList();
		for (OrderItem item : orderItemList) {
			Product product = productMapper.selectByPrimaryKey(item.getProductId());
			product.setStock(product.getStock() - item.getQuantity());
			productList.add(product);
			count = productMapper.updateByPrimaryKeySelective(product);
			if (count <= 0) {
				return JHResponse.createByError(JHResponseCode.Error.getCode(), "产品"+product.getName()+"生成订单时减少库存失败");
			}
		}

		//清空购物车
		List<Integer> productIdList = Lists.newArrayList();
		for (Cart item : cartList) {
			productIdList.add(item.getProductId());
		}
		count = cartMapper.batchDeleteByProductIds(productIdList);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_CreateOrderBatchDeleteCartProductError);
		}

		return JHResponse.createBySuccess(JHResponseCode.Success_CreateOrderSuccess,
				new OrderVO(order, orderItemList, shipping));
	}

	private long generateOrderNo(){
		long currentTime = System.currentTimeMillis();
		return currentTime + new Random().nextInt(100);
	}

	public JHResponse cancelOrder(Integer userId, Long orderId) {
		if (orderId == null) {
			return JHResponse.createByError(JHResponseCode.Error_OrderNOIsEmptyError);
		}
		Order order = orderMapper.selectByUserIdAndOrderNO(userId, orderId);
		if (order == null) {
			return JHResponse.createByError(JHResponseCode.Error_OrderUnExistError);
		}
		if (order.getStatus() > JHConst.Order.StatusEnum.No_Pay.getCode()) {
			return JHResponse.createByError(JHResponseCode.Error_CancelOrderHadPayError);
		}
		order.setStatus(JHConst.Order.StatusEnum.Order_Close.getCode());
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_CancelOrderError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_CancelOrderSuccess);
	}

	public JHResponse<OrderProductVO> getOrderCartProducts(Integer userId) {
		List<Cart> cartList = cartMapper.selectCheckedByUserId(userId);
		if (cartList == null || CollectionUtils.isEmpty(cartList)) {
			return JHResponse.createByError(JHResponseCode.Error_CreateOrderCartUnCheckedProductError);
		}
		List<OrderItem> orderItemList = Lists.newArrayList();
		for (Cart item : cartList) {
			CartListItemVO itemVO = iCartService.cartListItemByCart(item);
			OrderItem orderItem = new OrderItem(itemVO, null);
			orderItemList.add(orderItem);
		}
		OrderProductVO productVO = new OrderProductVO(orderItemList);
		return JHResponse.createBySuccess(JHResponseCode.Success_GetCartCheckedProductSuccess, productVO);
	}

	public JHResponse<OrderVO> getOrderDetail(Integer userId, Long orderNO) {
		Order order = orderMapper.selectByUserIdAndOrderNO(userId, orderNO);
		OrderVO orderVO = assembleOrderVOList(order);
		if (orderNO == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetOrderDetailError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_GetOrderDetailSuccess,
				orderVO);
	}

	private OrderVO assembleOrderVOList(Order order) {
		if (order == null) {
			return null;
		}
		Integer userId = order.getUserId();
		Long orderNO = order.getOrderNo();
		Shipping shipping = shippingMapper.selectByUserIdAndId(userId, order.getShippingId());
		if (shipping == null) {
			return null;
		}
		List<OrderItem> orderItemList = orderItemMapper.selectByUserIdAndOrderNO(userId, orderNO);
		if (orderItemList == null || CollectionUtils.isEmpty(orderItemList)) {
			return null;
		}
		return new OrderVO(order, orderItemList, shipping);
	}

	public JHResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
		PageInfo pageInfo = assemblePageInfo(userId, pageNum, pageSize);
		if (pageInfo == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetOrderListError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_GetOrderListSuccess, pageInfo);
	}

	private PageInfo assemblePageInfo(Integer userId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Order> orderList = null;
		if (userId == null) {
			orderList = orderMapper.selectAllOrder();
		} else {
			orderList = orderMapper.selectByUserId(userId);
		}
		PageInfo pageInfo = new PageInfo(orderList);
		List<OrderVO> orderVOList = Lists.newArrayList();
		for (Order item : orderList) {
			OrderVO orderVO = assembleOrderVOList(item);
			orderVOList.add(orderVO);
		}
		pageInfo.setList(orderVOList);
		return pageInfo;
	}



	public JHResponse<PageInfo> adminList(int pageNum, int pageSize) {
		PageInfo pageInfo = assemblePageInfo(null, pageNum, pageSize);
		if (pageInfo == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetOrderListError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_GetOrderListSuccess, pageInfo);
	}

	public JHResponse<OrderVO> adminDetail(Long orderNO) {
		Order order = orderMapper.selectByOrderNO(orderNO);
		OrderVO orderVO = assembleOrderVOList(order);
		if (orderNO == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetOrderDetailError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_GetOrderDetailSuccess,
				orderVO);
	}

	public JHResponse<PageInfo> adminSearch(Long orderNO, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Order order = orderMapper.selectByOrderNO(orderNO);
		OrderVO orderVO = assembleOrderVOList(order);
		if (orderNO == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetOrderDetailError);
		}
		PageInfo pageInfo = new PageInfo(Lists.newArrayList(order));
		pageInfo.setList(Lists.newArrayList(orderVO));
		return JHResponse.createBySuccess(JHResponseCode.Success_GetOrderDetailSuccess,
				pageInfo);
	}

	public JHResponse adminSendGoods(Long orderNO) {
		Order order = orderMapper.selectByOrderNO(orderNO);
		if (orderNO == null) {
			return JHResponse.createByError(JHResponseCode.Error_OrderUnExistError);
		}
		if (order.getStatus() == JHConst.Order.StatusEnum.Paid.getCode()) {
			order.setStatus(JHConst.Order.StatusEnum.Shipped.getCode());
			order.setSendTime(new Date());
			int count = orderMapper.updateByPrimaryKeySelective(order);
			if (count <= 0) {
				return JHResponse.createBySuccess(JHResponseCode.Error_SendGoodsError);
			}
			return JHResponse.createBySuccess(JHResponseCode.Success_SendGoodsSuccess);
		}
		return JHResponse.createBySuccess(JHResponseCode.Error_SendGoodsError);
	}
}
