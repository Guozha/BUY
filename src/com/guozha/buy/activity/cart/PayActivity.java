package com.guozha.buy.activity.cart;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.activity.mine.MyOrderActivity;
import com.guozha.buy.dialog.CustomDialog;
import com.guozha.buy.entry.cart.PayOrderMesg;
import com.guozha.buy.entry.cart.PayValidateResult;
import com.guozha.buy.entry.cart.PayWayEntry;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.server.AlipayManager;
import com.guozha.buy.server.WXpayManager;
import com.guozha.buy.util.PayResult;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.guozha.buy.util.WXPayUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class PayActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "PayPAGE";
	
	private static final int REQUEST_CODE = 0x0001;
	
	private static final int HAND_PAY_WAY_COMPLETED = 0x0004;		//支付方式请求完毕
	private static final int HAND_MAIN_SIGNLE_COMPLETED = 0x0005; 	//主单信息请求完毕
	private static final int HAND_PAY_VALIDATE_COMPLETED = 0x0006;  //付款前验证完毕
	private static final int HAND_PAY_SUCCESSED = 0x0007;			//支付成功
	private static final int HAND_CHOOSED_TICKET_COMPLETED = 0x0008; //选择菜谱完成
	
	private boolean mOrderComeIn = false; 	//是否从订单进来
	
	private int mOrderId = -1;
	private int mServicePrice = -1;
	
	private boolean mBeanChecked = false;				//是否选择使用菜豆
	private boolean mAccountRemainChecked = false;		//是否选择使用账户余额

	
	private int mTotalPrice;		//总金额
	private int mBeanNum;			//菜豆数
	private int mAccountRemain;		//账户余额
	private int mTicketId = -1;			//菜票Id
	private int mTicketPrice;		//菜票面额
	
	private TextView mTotalPriceText;	//总费用
	private TextView mServerPriceText;  //服务费
	private View mCanUseMoneyView;			//账户余额
	private TextView mCanUseMoneyDeduct;	//扣除余额
	private ImageView mCanUserMoneyViewIcon;	
	private View mCanUseBeanView;			//菜豆
	private ImageView mCanUserBeanViewIcon;	
	private TextView mTicketText;		//菜票
	private View mTicketView;		
	private ImageView mTicketArrowIcon;	//菜票右边箭头
	private TextView mPriceText;		//应支付
	
	private TextView mCanUseMoneyText;
	private TextView mCanUseBeanText;
	
	private View mZhiFuBaoView;
	private View mWeiXinView;
	private View mWangYingView;
	private View mHuoDaoFuKuanView;
	
	private List<PayWayEntry> mPayWayList;  //支付方式
	
	private PayOrderMesg mPayOrderMesg;		//支付信息
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case AlipayManager.SDK_PAY_FLAG: 
					PayResult payResult = new PayResult((String) msg.obj);
					String resultStatus = payResult.getResultStatus();
					if (TextUtils.equals(resultStatus, "9000")) {
						mHandler.sendEmptyMessage(HAND_PAY_SUCCESSED);
					} else {
						if (TextUtils.equals(resultStatus, "8000")) {
							ToastUtil.showToast(PayActivity.this, "支付结果确认中");
							Intent intent = new Intent(PayActivity.this, MyOrderActivity.class);
							startActivity(intent);
							PayActivity.this.finish();
						} else {
							ToastUtil.showToast(PayActivity.this, "支付失败");
						}
					}
					break;
				case AlipayManager.SDK_CHECK_FLAG: 
					ToastUtil.showToast(PayActivity.this, "检查结果为：" + msg.obj);
					break;
				case HAND_PAY_WAY_COMPLETED:
					for(int i = 0; i < mPayWayList.size(); i++){
						switch (mPayWayList.get(i).getPayWayId()) {
						case 1:			//支付宝支付
							mZhiFuBaoView.setVisibility(View.VISIBLE);
							break;
						case 2:			//微信支付
							mWeiXinView.setVisibility(View.VISIBLE);
							break;
						case 3:			//网银支付
							mWangYingView.setVisibility(View.VISIBLE);
							break;
						case 4:			//货到付款
							mHuoDaoFuKuanView.setVisibility(View.VISIBLE);
							break;
						};
					}
					break;
				case HAND_MAIN_SIGNLE_COMPLETED:
					mTotalPriceText.setText(UnitConvertUtil.getSwitchedMoney(mTotalPrice) + "元");
					mServerPriceText.setText(UnitConvertUtil.getSwitchedMoney(mServicePrice) + "元");
					mPriceText.setText(UnitConvertUtil.getSwitchedMoney(mTotalPrice + mServicePrice) + "元");
					break;
				case HAND_PAY_VALIDATE_COMPLETED:
					requestPayMoney();
					break;
				case HAND_PAY_SUCCESSED:
					ToastUtil.showToast(PayActivity.this, "支付成功");
					if(mOrderComeIn){
						Intent intent = getIntent();
						if(intent != null){
							intent.putExtra("paySuccess", true);
						}
						setResult(0, intent);
						PayActivity.this.finish();
					}else{
						Intent intent = new Intent(PayActivity.this, MyOrderActivity.class);
						startActivity(intent);
						PayActivity.this.finish();
					}
					break;
				case HAND_CHOOSED_TICKET_COMPLETED: //选择菜票成功
					if(mTicketId != -1){
						mTicketArrowIcon.setVisibility(View.GONE);
						mTicketText.setVisibility(View.VISIBLE);
						mTicketText.setText(UnitConvertUtil.getSwitchedMoney(mTicketPrice) + "元");
						setPayPriceText();
					}
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		customActionBarStyle("支付");
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mOrderId = bundle.getInt("orderId");
				mServicePrice = bundle.getInt("serverPrice");
				mOrderComeIn = bundle.getBoolean("orderComeIn");
			}
		}
		setResult(0);
		initView();
		initData();
		//允许我的账户数据更新
		MainPageInitDataManager.mAccountUpdated = true;
	}
	
	private ImageView payZhifubaoIcon;
	private ImageView payWeixinIcon;
	private ImageView payWangyingIcon;
	private ImageView payHuodaofukuanIcon;
	
	private void initView(){
		mTotalPriceText = (TextView) findViewById(R.id.pay_total_price);
		mServerPriceText = (TextView) findViewById(R.id.pay_server_price);
		mCanUseMoneyView = findViewById(R.id.pay_can_use_money);
		mCanUserMoneyViewIcon = (ImageView) findViewById(R.id.pay_can_use_money_icon);
		mCanUseBeanView = findViewById(R.id.pay_can_use_bean);
		mCanUserBeanViewIcon = (ImageView) findViewById(R.id.pay_can_use_bean_icon);
		mTicketText = (TextView) findViewById(R.id.pay_ticket);
		mPriceText = (TextView) findViewById(R.id.pay_price);
		mTicketArrowIcon = (ImageView) findViewById(R.id.pay_ticket_icon);
		mCanUseMoneyDeduct = (TextView) findViewById(R.id.pay_can_use_money_deduct);
		mCanUseMoneyText = (TextView) findViewById(R.id.pay_can_use_money_text);
		mCanUseBeanText = (TextView) findViewById(R.id.pay_can_user_bean_text);
		
		mZhiFuBaoView = findViewById(R.id.pay_way_zhifubao);
		mZhiFuBaoView.setOnClickListener(this);
		payZhifubaoIcon = (ImageView) findViewById(R.id.pay_way_zhifubao_icon);
		mWeiXinView = findViewById(R.id.pay_way_weixin);
		mWeiXinView.setOnClickListener(this);
		payWeixinIcon = (ImageView) findViewById(R.id.pay_way_weixin_icon);
		mWangYingView = findViewById(R.id.pay_way_wangying);
		mWangYingView.setOnClickListener(this);
		payWangyingIcon = (ImageView) findViewById(R.id.pay_way_wangying_icon);
		mHuoDaoFuKuanView = findViewById(R.id.pay_way_huodaofukuan);
		mHuoDaoFuKuanView.setOnClickListener(this);
		payHuodaofukuanIcon = (ImageView) findViewById(R.id.pay_way_huodaofukuan_icon);
		
		mCanUseBeanView.setOnClickListener(this);
		mCanUseMoneyView.setOnClickListener(this);
		findViewById(R.id.pay_server_fee_rule).setOnClickListener(this);
		mTicketView = findViewById(R.id.pay_ticket_choose);
		mTicketView.setOnClickListener(this);
		findViewById(R.id.pay_button).setOnClickListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		//请求主单信息
		RequestParam paramPath = new RequestParam("order/info")
		.setParams("orderId", mOrderId);
		HttpManager.getInstance(this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						//mOrderNum = response.getString("orderNo");  //订单号
						mTotalPrice = response.getInt("totalPrice");//商品金额
						mServicePrice = response.getInt("serviceFee");				//服务费
						mHandler.sendEmptyMessage(HAND_MAIN_SIGNLE_COMPLETED);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
		
		//获取账户信息(菜豆、菜票）
		AccountInfo accountInfo = 
				MainPageInitDataManager.getInstance(CustomApplication.getContext()).getAccountInfo(null);
		if(accountInfo != null){
			mAccountRemain = accountInfo.getBalance();
			mBeanNum = accountInfo.getBeanAmount();
			mCanUseMoneyText.setText("可用账户余额" +
						UnitConvertUtil.getSwitchedMoney(mAccountRemain));	 //账户余额
			mCanUseBeanText.setText("可用菜豆数" + mBeanNum + "个" + UnitConvertUtil.getBeanMoney(mBeanNum) + "元"); //菜豆数
		}
		
		//获取支付方式
		paramPath = new RequestParam("payment/listPayWay")
		.setParams("addressId", addressId);
		HttpManager.getInstance(this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mPayWayList= gson.fromJson(response, new TypeToken<List<PayWayEntry>>() { }.getType());
					mHandler.sendEmptyMessage(HAND_PAY_WAY_COMPLETED);
				}
		});
	}
	
	private long mBeginTimeMillis; //防止重复提交的时间记录
	
	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.pay_server_fee_rule: 	//服务费规则
			CustomDialog customDialog = new CustomDialog(
					PayActivity.this, R.layout.dialog_server_fee_rule);
			customDialog.setDismissButtonId(0);
			break;
		case R.id.pay_ticket_choose:	//请求有效菜票
			intent = new Intent(PayActivity.this, ChooseTicketActivity.class);
			intent.putExtra("money", mTotalPrice);
			startActivityForResult(intent, REQUEST_CODE);
			break;
		case R.id.pay_button:
			if(System.currentTimeMillis() - mBeginTimeMillis > 3000){
				mBeginTimeMillis = System.currentTimeMillis();
				requestPayCheck();
			}else{
				ToastUtil.showToast(PayActivity.this, "请不要重复提交");
			}
			break;
		case R.id.pay_can_use_money:   //扣除余额选择
			if(mCanUserMoneyViewIcon == null) return;
			mAccountRemainChecked = exchangeIcon(mCanUserMoneyViewIcon);
			setPayPriceText();
			break;
		case R.id.pay_can_use_bean:	   //扣除菜豆选择
			if(mCanUserBeanViewIcon == null) return;
			mBeanChecked = exchangeIcon(mCanUserBeanViewIcon);
			setPayPriceText();
			break;
		case R.id.pay_way_zhifubao:
			payWayExchangeIcon(payZhifubaoIcon, PayWay.ZHI_FU_BAO);
			break;
		case R.id.pay_way_weixin:
			payWayExchangeIcon(payWeixinIcon, PayWay.WEI_XIN);
			break;
		case R.id.pay_way_wangying:
			payWayExchangeIcon(payWangyingIcon, PayWay.WANG_YING);
			break;
		case R.id.pay_way_huodaofukuan:
			payWayExchangeIcon(payHuodaofukuanIcon, PayWay.HUO_DAO_FU_KUAN);
			break;
		}
	}
	
	private int mAccountRemainDeduct = 0; //扣除的余额
	private int mBeanrDeduct = 0;		  //扣除菜豆

	/**
	 * 设置应付多少款文字
	 */
	private void setPayPriceText() {
		int payPrice = mTotalPrice + mServicePrice;
		if(mAccountRemainChecked){
			if(payPrice > mAccountRemain){
				payPrice = payPrice - mAccountRemain;
				mAccountRemainDeduct = mAccountRemain;
			}else{
				mAccountRemainDeduct = payPrice;
				payPrice = 0;
				mTicketId = -1;
				mBeanrDeduct = 0;
				mCanUseBeanView.setVisibility(View.GONE);
				mTicketView.setVisibility(View.GONE);
			}
			mCanUseMoneyDeduct.setVisibility(View.VISIBLE);
			mCanUseMoneyDeduct.setText(" 扣除" + UnitConvertUtil.getSwitchedMoney(mAccountRemainDeduct) + "元");
		}else{
			mCanUseMoneyDeduct.setVisibility(View.GONE);
			mCanUseBeanView.setVisibility(View.VISIBLE);
			mTicketView.setVisibility(View.VISIBLE);
		}
		if(mBeanChecked){
			payPrice = payPrice - mBeanNum;
			if(payPrice < 0){
				payPrice = 0;
			}
			mBeanrDeduct = mBeanNum;
		}else{
			mBeanrDeduct = 0;
		}
		
		if(mTicketId != -1){
			payPrice = payPrice - mTicketPrice;
			if(payPrice < 0){
				payPrice = 0;
			}
		}
		mPriceText.setText(UnitConvertUtil.getSwitchedMoney(payPrice) + "元");
	}
	
	/**
	 * 请求支付核对
	 */
	private void requestPayCheck(){
		String payWayId = null;
		switch (mPayWay) {
		case ZHI_FU_BAO:
			payWayId = "1";
			break;
		case WEI_XIN:
			payWayId = "2";
			break;
		case WANG_YING:
			payWayId = "3";
			break;
		case HUO_DAO_FU_KUAN:
			payWayId = "4";
			break;
		case UNKNOW:
			break;
		}
		if(payWayId == null){
			ToastUtil.showToast(PayActivity.this, "请选择支付方式");
			return;
		}
		String token = ConfigManager.getInstance().getUserToken();
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("payment/preparePay")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("orderId", mOrderId)
		.setParams("useTicketId", mTicketId == -1 ? "0": String.valueOf(mTicketId))
		.setParams("payWayId", payWayId)
		.setParams("balanceDecPrice", mAccountRemainDeduct)
		.setParams("useBeanAmount", mBeanrDeduct);
		
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					PayValidateResult payValidateResult = 
							gson.fromJson(response, new TypeToken<PayValidateResult>() { }.getType());
					if(payValidateResult != null){
						String resultCode = payValidateResult.getReturnCode();
						if("1".equals(resultCode)){
							String flag = payValidateResult.getNeedPayFlag();
							if("0".equals(flag)){
								mHandler.sendEmptyMessage(HAND_PAY_SUCCESSED);
							}else if("1".equals(flag)){
								mPayOrderMesg = payValidateResult.getOrder();
								if(mPayOrderMesg != null){
									if(mPayOrderMesg.getPayPrice() <= 0){
										ToastUtil.showToast(PayActivity.this, "支付金额必须大于0");
									}else{
										mHandler.sendEmptyMessage(HAND_PAY_VALIDATE_COMPLETED);
									}
								}else{
									ToastUtil.showToast(PayActivity.this, "订单信息获取失败");
								}
							}else{
								ToastUtil.showToast(PayActivity.this, "获取的支付码未定义");
							}
						}else{
							ToastUtil.showToast(PayActivity.this, payValidateResult.getMsg());
						}
					}else{
						ToastUtil.showToast(PayActivity.this, "验证订单失败");
					}
				}
		});
 
	}

	/**
	 * 请求支付
	 */
	private void requestPayMoney() {
		if(mPayWay == null) {
			ToastUtil.showToast(PayActivity.this, "请选择支付方式");
			return;
		}
		String tag;
		switch (mPayWay) {
		case ZHI_FU_BAO:
			tag = String.valueOf(payZhifubaoIcon.getTag());
			if("1".equals(tag)){
				AlipayManager alipayManager = new AlipayManager(
						mPayOrderMesg.getOrderNo(), 
						mPayOrderMesg.getFirstShowName() + "等" + mPayOrderMesg.getQuantity() + "件商品", 
						mPayOrderMesg.getMemo(), 
						UnitConvertUtil.getSwitchedMoney(mPayOrderMesg.getPayPrice()));
				alipayManager.requestPay(this, mHandler);
			}else{
				ToastUtil.showToast(PayActivity.this, "请选择支付方式");
			}
			break;
		case WEI_XIN:
			tag = String.valueOf(payWeixinIcon.getTag());
			if("1".equals(tag)){
				WXpayManager wxpayManager = new WXpayManager(
						this, 
						mPayOrderMesg.getOrderNo(), 
						mPayOrderMesg.getFirstShowName() + "等" + mPayOrderMesg.getQuantity() + "件商品", 
						mPayOrderMesg.getMemo(), 
						mPayOrderMesg.getPayPrice());
				wxpayManager.requestPay();
			}else{
				ToastUtil.showToast(PayActivity.this, "请选择支付方式");
			}
			break;
		case HUO_DAO_FU_KUAN:
			
			break;
		case WANG_YING:
			
			break;
		}
	}
	
	enum PayWay{
		ZHI_FU_BAO,
		WANG_YING,
		WEI_XIN,
		HUO_DAO_FU_KUAN,
		UNKNOW
	}
	
	private PayWay mPayWay = PayWay.UNKNOW;	//支付方式
	private void payWayExchangeIcon(ImageView imageView, PayWay payWay){
		mPayWay = payWay;
		clearPayWayIcon();
		if(imageView == null) return;
		exchangeIcon(imageView);
	}
	
	/**
	 * 清空（重置）选择
	 */
	private void clearPayWayIcon(){
		payZhifubaoIcon.setImageResource(R.drawable.pay_selectbox_normal);
		payWangyingIcon.setImageResource(R.drawable.pay_selectbox_normal);
		payWeixinIcon.setImageResource(R.drawable.pay_selectbox_normal);
		payHuodaofukuanIcon.setImageResource(R.drawable.pay_selectbox_normal);
		
		payZhifubaoIcon.setTag("0");
		payWangyingIcon.setTag("0");
		payWeixinIcon.setTag("0");
		payHuodaofukuanIcon.setTag("0");
	}

	/**
	 * 改变图片
	 * @param imageView
	 * @param choseId
	 * @param notchoseId
	 */
	private boolean exchangeIcon(ImageView imageView) {
		String moneyTag = String.valueOf(imageView.getTag());
		if(moneyTag == null || "null".equals(moneyTag) || "0".equals(moneyTag)){
			imageView.setImageResource(R.drawable.pay_selectbox_selected);
			imageView.setTag("1");
			return true;
		}else{
			imageView.setImageResource(R.drawable.pay_selectbox_normal);
			imageView.setTag("0");
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE){
			if(data == null) return;
			Bundle bundle = data.getExtras();
			if(bundle != null){
				mTicketId = bundle.getInt("ticktId");
				mTicketPrice = bundle.getInt("ticketPrice");
				mHandler.sendEmptyMessage(HAND_CHOOSED_TICKET_COMPLETED);
			} 
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//友盟界面统计
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
}
