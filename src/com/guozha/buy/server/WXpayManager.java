package com.guozha.buy.server;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;

import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.cart.PayActivity;
import com.guozha.buy.util.Keys;
import com.guozha.buy.util.MD5;
import com.guozha.buy.util.WXPayUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付
 * @author PeggyTong
 *
 */
public class WXpayManager {
	//TODO 注意，这里是测试服地址
	public static String NOTIFY_URL = "http://120.24.220.86:9999/PAY_WECHAT/notify_url.jsp";
	private IWXAPI payApi;
	
	private String mOrderNum;
	private String mSubject;
	//private String mBody;
	private int mTotalPrice;
	private PayReq req;
	private Map<String,String> resultunifiedorder;
	
	public WXpayManager(Context context, String orderNum, String subject, String body, int price){
		mOrderNum = orderNum;
		mSubject = subject;
		//mBody = body;
		mTotalPrice = price;
		
		mTotalPrice = 1;
		req = new PayReq();
		payApi = WXAPIFactory.createWXAPI(context, null);
		//第一步：注册APP_ID
		payApi.registerApp(Keys.APP_ID);
	}
	
	/**
	 * 请求支付
	 */
	
	public void requestPay(Context context){
		//第二步：生成预支付订单
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask(context);
		getPrepayId.execute();
	}
	
	/**
	 * 微信支付统一下单接口参数
	 * @return
	 */
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();
		try {
			String	nonceStr = genNonceStr();
		    xml.append("</xml>");
	        List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Keys.APP_ID));  
			packageParams.add(new BasicNameValuePair("body", mSubject));   //商品描述
			//packageParams.add(new BasicNameValuePair("body","bodytest"));   //商品描述
			packageParams.add(new BasicNameValuePair("mch_id", Keys.MCH_ID));  //商户号
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));	 //随机字符串
			packageParams.add(new BasicNameValuePair("notify_url", NOTIFY_URL));  //通知地址
			packageParams.add(new BasicNameValuePair("out_trade_no", mOrderNum));    //商户订单号
			packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));   //终端ip
			packageParams.add(new BasicNameValuePair("total_fee", String.valueOf(mTotalPrice)));			//总金额，单位分
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));			//交易类型
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));		//签名
		    String xmlstring =toXml(packageParams);
			return new String(xmlstring.toString().getBytes(), "ISO8859-1");
		} catch (Exception e) {
			return null;
		}
	}
	
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	/**
	 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Keys.API_KEY);
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion",packageSign);
		return packageSign;
	}
	
	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");
			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");
		Log.e("orion",sb.toString());
		return sb.toString();
	}

	

	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {
		private ProgressDialog dialog;
		private Context context;
		public GetPrepayIdTask(Context context){
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(context, "提示", "正在获取预支付订单...");
		}

		@Override
		protected void onPostExecute(Map<String,String> result) {
			resultunifiedorder=result;
			//第三步：生成签名参数
			genPayReq();
			//第四步：调用微信客户端进行支付
			payApi.registerApp(Keys.APP_ID);
			payApi.sendReq(req);
			if (dialog != null) {
				dialog.dismiss();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String,String>  doInBackground(Void... params) {
			//微信支付统一下单接口
			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			//需要的参数
			String entity = genProductArgs();

			Log.e("orion",entity);

			byte[] buf = WXPayUtil.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String,String> xml=decodeXml(content);

			return xml;
		}
	}
	
	public Map<String,String> decodeXml(String content) {
		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if("xml".equals(nodeName)==false){
							//实例化student对象
							xml.put(nodeName,parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion",e.toString());
		}
		return null;
	}
	
	/**
	 * 生成签名参数
	 */
	private void genPayReq() {
		req.appId = Keys.APP_ID;
		req.partnerId = Keys.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "prepay_id="+resultunifiedorder.get("prepay_id");
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);
	}
	
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Keys.API_KEY);
		String appSign = MD5.getMessageDigest(sb.toString().getBytes());
		return appSign;
	}
}
