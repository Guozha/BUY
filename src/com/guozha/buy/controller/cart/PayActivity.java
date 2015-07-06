package com.guozha.buy.controller.cart;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.controller.mine.MyOrderActivity;
import com.guozha.buy.entry.cart.PayWayEntry;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.entry.mine.order.Order;
import com.guozha.buy.entry.mine.order.OrderResult;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.OrderModel;
import com.guozha.buy.model.PayModel;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.OrderModelResult;
import com.guozha.buy.model.result.PayModelResult;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.server.AlipayManager;
import com.guozha.buy.server.WXpayManager;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.guozha.buy.util.pay.PayResult;
import com.umeng.analytics.MobclickAgent;

public class PayActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "支付";
	
	private static final int REQUEST_CODE = 0x0001;
	
	private static final int HAND_PAY_WAY_COMPLETED = 0x0004;		//支付方式请求完毕
	private static final int HAND_USER_INFO_COMPLETED = 0x0009;
	private static final int HAND_PAY_VALIDATE_COMPLETED = 0x0006;  //付款前验证完毕
	private static final int HAND_PAY_SUCCESSED = 0x0007;			//支付成功
	private static final int HAND_CHOOSED_TICKET_COMPLETED = 0x0008; //选择菜谱完成
	private static final int HAND_CREATED_ORDER = 0x0010; 	//创建订单成功
	
	private boolean mOrderComeIn = false; 	//是否从订单进来
	
	private int mOrderId = 0;
	private int mServicePrice = -1;
	
	//private boolean mBeanChecked = false;				//是否选择使用菜豆
	private boolean mAccountRemainChecked = false;		//是否选择使用账户余额

	
	private int mTotalPrice;		//总金额
	//private int mBeanNum;			//菜豆数
	private int mAccountRemain;		//账户余额
	private int mTicketId = -1;			//菜票Id
	private int mTicketPrice;		//菜票面额
	
	private TextView mTotalPriceText;	//总费用
	private TextView mServerPriceText;  //服务费
	private View mCanUseMoneyView;			//账户余额
	private TextView mCanUseMoneyDeduct;	//扣除余额
	private ImageView mCanUserMoneyViewIcon;	
	//private View mCanUseBeanView;			//菜豆
	//private ImageView mCanUserBeanViewIcon;	
	private TextView mTicketText;		//菜票
	private View mTicketView;		
	private ImageView mTicketArrowIcon;	//菜票右边箭头
	private TextView mPriceText;		//应支付
	
	private TextView mCanUseMoneyText;
	//private TextView mCanUseBeanText;
	
	private View mZhiFuBaoView;
	private View mWeiXinView;
	private View mWangYingView;
	private View mHuoDaoFuKuanView;
	
	private List<PayWayEntry> mPayWayList;  //支付方式
	private AccountInfo mAccountInfo;
	//private PayOrderMesg mPayOrderMesg;		//支付信息
	private Order mOrder;
	private String mFromArriveTime;
	private String mToArriveTime;
	private String mMemoMessage;
	private int mAddressId;
	
	private OrderModel mOrderModel = new OrderModel(new MyOrderModelResult());
	private PayModel mPayModel = new PayModel(new MyPayModelResult());
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	
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
					int defaultPayWay = ConfigManager.getInstance().getDefaultPayWay();
					switch (defaultPayWay) {
					case 1:
						payWayExchangeIcon(payZhifubaoIcon, PayWay.ZHI_FU_BAO);
						break;
					case 2:
						payWayExchangeIcon(payWeixinIcon, PayWay.WEI_XIN);
						break;
					case 3:
						payWayExchangeIcon(payWangyingIcon, PayWay.WANG_YING);
						break;
					case 4:
						payWayExchangeIcon(payHuodaofukuanIcon, PayWay.HUO_DAO_FU_KUAN);
						break;
					}
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
				case HAND_USER_INFO_COMPLETED:
					if(mAccountInfo == null) return;
					//获取账户信息(菜豆、菜票）
					mAccountRemain = mAccountInfo.getBalance();
					//mBeanNum = mAccountInfo.getBeanAmount();
					mCanUseMoneyText.setText("可用账户余额" +
								UnitConvertUtil.getSwitchedMoney(mAccountRemain));	 //账户余额
					if(mAccountRemain > 0){
						mCanUseMoneyView.setVisibility(View.VISIBLE);
						mAccountRemainChecked = exchangeIcon(mCanUserMoneyViewIcon);
						setPayPriceText();
					}else{
						mCanUseMoneyView.setVisibility(View.GONE);
					}
					//mCanUseBeanText.setText("可用菜豆数" + mBeanNum + "个" + UnitConvertUtil.getBeanMoney(mBeanNum) + "元"); //菜豆数
					break;
				case HAND_CREATED_ORDER:
					requestPay();
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		customActionBarStyle(PAGE_NAME);
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mOrderId = bundle.getInt("orderId");
				mFromArriveTime = bundle.getString("fromArriveTime");
				mToArriveTime = bundle.getString("toArriveTime");
				mMemoMessage = bundle.getString("memoMessage");
				mAddressId = bundle.getInt("addressId");
				mServicePrice = bundle.getInt("serverPrice");
				mTotalPrice = bundle.getInt("totalPrice");
				mOrderComeIn = bundle.getBoolean("orderComeIn");
			}
		}
		setResult(0);
		initView();
		initData();
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
		//mCanUseBeanView = findViewById(R.id.pay_can_use_bean);
		//mCanUserBeanViewIcon = (ImageView) findViewById(R.id.pay_can_use_bean_icon);
		mTicketText = (TextView) findViewById(R.id.pay_ticket);
		mPriceText = (TextView) findViewById(R.id.pay_price);
		mTicketArrowIcon = (ImageView) findViewById(R.id.pay_ticket_icon);
		mCanUseMoneyDeduct = (TextView) findViewById(R.id.pay_can_use_money_deduct);
		mCanUseMoneyText = (TextView) findViewById(R.id.pay_can_use_money_text);
		//mCanUseBeanText = (TextView) findViewById(R.id.pay_can_user_bean_text);
		
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
		
		//mCanUseBeanView.setOnClickListener(this);
		mCanUseMoneyView.setOnClickListener(this);
		
		findViewById(R.id.pay_server_fee_rule).setOnClickListener(this);
		mTicketView = findViewById(R.id.pay_ticket_choose);
		mTicketView.setOnClickListener(this);
		findViewById(R.id.pay_button).setOnClickListener(this);
		
		mTotalPriceText.setText(UnitConvertUtil.getSwitchedMoney(mTotalPrice) + "元");
		mServerPriceText.setText(UnitConvertUtil.getSwitchedMoney(mServicePrice) + "元");
		mPriceText.setText(UnitConvertUtil.getSwitchedMoney(mTotalPrice + mServicePrice) + "元");
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		//mOrderModel.requestOrderInfo(this, mOrderId);
		int addressId = ConfigManager.getInstance().getChoosedAddressId(this);
		if(addressId == -1) return;
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(this);
		if(token == null) return;
		//获取支付方式
		mPayModel.requestPayWays(this, addressId);
		mUserModel.requestAccountInfo(this, token, userId);
	}
	
	private long mBeginTimeMillis; //防止重复提交的时间记录
	
	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.pay_server_fee_rule: 	//服务费规则
			CustomDialog customDialog = new CustomDialog(
					PayActivity.this, R.layout.dialog_server_fee_rule);
			TextView ruleTitle = (TextView) customDialog.getViewById(R.id.service_fee_rule_title);
			TextView ruleContent = (TextView) customDialog.getViewById(R.id.service_fee_rule_content);
			ruleTitle.setText(ConfigManager.getInstance().getServiceFeeRuleTitle());
			ruleContent.setText(Html.fromHtml(ConfigManager.getInstance().getServiceFeeRuleContent()));
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
		/*
		case R.id.pay_can_use_bean:	   //扣除菜豆选择
			if(mCanUserBeanViewIcon == null) return;
			mBeanChecked = exchangeIcon(mCanUserBeanViewIcon);
			setPayPriceText();
			break;
		*/
		case R.id.pay_way_zhifubao:
			ConfigManager.getInstance().setDefaultPayWay(1);
			payWayExchangeIcon(payZhifubaoIcon, PayWay.ZHI_FU_BAO);
			break;
		case R.id.pay_way_weixin:
			ConfigManager.getInstance().setDefaultPayWay(2);
			payWayExchangeIcon(payWeixinIcon, PayWay.WEI_XIN);
			break;
		case R.id.pay_way_wangying:
			ConfigManager.getInstance().setDefaultPayWay(3);
			payWayExchangeIcon(payWangyingIcon, PayWay.WANG_YING);
			break;
		case R.id.pay_way_huodaofukuan:
			ConfigManager.getInstance().setDefaultPayWay(4);
			payWayExchangeIcon(payHuodaofukuanIcon, PayWay.HUO_DAO_FU_KUAN);
			break;
		}
	}
	
	private int mAccountRemainDeduct = 0; //扣除的余额
	//private int mBeanrDeduct = 0;		  //扣除菜豆

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
				//mBeanrDeduct = 0;
				//mCanUseBeanView.setVisibility(View.GONE);
				mTicketView.setVisibility(View.GONE);
			}
			mCanUseMoneyDeduct.setVisibility(View.VISIBLE);
			mCanUseMoneyDeduct.setText(" 扣除" + UnitConvertUtil.getSwitchedMoney(mAccountRemainDeduct) + "元");
		}else{
			mCanUseMoneyDeduct.setVisibility(View.GONE);
			//mCanUseBeanView.setVisibility(View.VISIBLE);
			mTicketView.setVisibility(View.VISIBLE);
		}
		/*
		if(mBeanChecked){
			payPrice = payPrice - mBeanNum;
			if(payPrice < 0){
				payPrice = 0;
			}
			mBeanrDeduct = mBeanNum;
		}else{
			mBeanrDeduct = 0;
		}
		*/
		
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
		String payWayId = getPayWayId();
		if(payWayId == null) return;
		String token = ConfigManager.getInstance().getUserToken(this);
		if(token == null) return;
		int userId = ConfigManager.getInstance().getUserId();
		if(mOrderId == -1){
			mOrderModel.requestOrderNomalInsert(this, token, userId, mAddressId, mFromArriveTime, mToArriveTime, mMemoMessage);
		}else{
			requestPay();
		}
		//mOrderModel.requestOrderNomalWithPay(this, token, userId, mOrderId, mAccountRemainDeduct, mTicketId, mBeanrDeduct, Integer.parseInt(payWayId), addressId, mFromeTime, mToTime, mMemo);
		//mPayModel.requestPreparePay(this, token, userId, mOrderId, mTicketId, Integer.parseInt(payWayId), mAccountRemainDeduct, mBeanrDeduct);
	}

	private String getPayWayId() {
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
			return null;
		}
		return payWayId;
	}
	
	private void requestPay(){
		String payWayId = getPayWayId();
		if(payWayId == null) return;
		String token = ConfigManager.getInstance().getUserToken(this);
		if(token == null) return;
		int userId = ConfigManager.getInstance().getUserId();
		mOrderModel.requestPayCount(this, token, userId, mOrderId, mAccountRemainDeduct, mTicketId, Integer.parseInt(payWayId));
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
						mOrder.getOrderNo(), 
						"爱掌勺订单" + mOrder.getOrderNo(), 
						mOrder.getMemo(), 
						UnitConvertUtil.getSwitchedMoney(mOrder.getPayPrice()));
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
						mOrder.getOrderNo(), 
						"爱掌勺订单" + mOrder.getOrderNo(),
						mOrder.getMemo(), 
						mOrder.getPayPrice());
				wxpayManager.requestPay(this);
			}else{
				ToastUtil.showToast(PayActivity.this, "请选择支付方式");
			}
			break;
		case HUO_DAO_FU_KUAN:
			LogUtil.e("货到付款。。。");
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
	
	class MyOrderModelResult extends OrderModelResult{
		/*
		@Override
		public void requestOrderInforResult(int totalPrice, int serviceFee) {
			mTotalPrice = totalPrice;
			mServicePrice = serviceFee;
			mHandler.sendEmptyMessage(HAND_MAIN_SIGNLE_COMPLETED);			
		}
		*/
		@Override
		public void requestPayCountResult(OrderResult orderResult) {
			if(orderResult == null) {
				ToastUtil.showToast(PayActivity.this, "生成订单数据异常");
				return;
			}
			if(BaseModel.REQUEST_SUCCESS.equals(orderResult.getReturnCode())){
				String flag = orderResult.getNeedPayFlag();
				if("0".equals(flag)){
					mHandler.sendEmptyMessage(HAND_PAY_SUCCESSED);
				}else if("1".equals(flag)){
					mOrder = orderResult.getOrder();
					if(mOrder != null){
						if(mOrder.getPayPrice() <= 0){
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
				ToastUtil.showToast(PayActivity.this, orderResult.getMsg());
			}
		}
		
		@Override
		public void requestOrderNomalInsertResult(String returnCode,
				String msg, int orderId) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				mOrderId = orderId;
				mHandler.sendEmptyMessage(HAND_CREATED_ORDER);
			}else{
				ToastUtil.showToast(PayActivity.this, msg);
			}
		}
	}
	
	class MyPayModelResult extends PayModelResult{
		@Override
		public void requestPayWaysResult(List<PayWayEntry> payWayList) {
			mPayWayList = payWayList;
			mHandler.sendEmptyMessage(HAND_PAY_WAY_COMPLETED);
		}
		
		/*
		@Override
		public void requestPreparePayResult(PayValidateResult payValidateResult) {
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
		*/
	}
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestAccountInfoResult(AccountInfo accountInfo) {
			if(accountInfo == null) return;
			mAccountInfo = accountInfo;
			mHandler.sendEmptyMessage(HAND_USER_INFO_COMPLETED);
		}
	}
}
