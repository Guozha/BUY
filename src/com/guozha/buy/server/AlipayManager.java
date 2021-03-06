package com.guozha.buy.server;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.guozha.buy.util.Keys;
import com.guozha.buy.util.Rsa;

/**
 * 支付宝支付
 * @author PeggyTong
 *
 */
public class AlipayManager{
	
	public static final int SDK_PAY_FLAG = 1;
	public static final int SDK_CHECK_FLAG = 2;
	public static String NOTIFY_URL = "http://120.24.211.45:9999/PAY_ALI/notify_url.jsp";
	
	private String mOrderNum;
	private String mSubject;
	private String mBody;
	private double mPrice;
	
	public AlipayManager(String orderNum, String subject, String body, double price){
		mOrderNum = orderNum;
		mSubject = subject;
		mPrice = price;
		mBody = body;
		if(mBody == null || "".equals(mBody)){
			mBody = "没有备注";
		}
	}
	/**
	 * 请求支付
	 */
	public void requestPay(final Activity activity, final Handler handler){
		//生成订单
		String orderInfo = getOrderInfor(mSubject, mBody, String.valueOf(mPrice));
		
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		if(sign == null) return;
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	/**
	 * 创建订单信息
	 * @param subject 商品名称
	 * @param body   商品描述
	 * @param price  价格
	 * @return
	 */
	private String getOrderInfor(String subject, String body, String price){
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Keys.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Keys.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + NOTIFY_URL + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	/**
	 * 生成商户订单号，必须唯一哦
	 * @return
	 */
	private String getOutTradeNo(){
		//TODO 菜场编号 + 时间 + 单号
		return mOrderNum;
	}
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		//return SignUtils.sign(content, RSA_PRIVATE);
		return Rsa.sign(content, Keys.RSA_PRIVATE);
	}
	
	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
