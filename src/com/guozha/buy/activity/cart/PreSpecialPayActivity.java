package com.guozha.buy.activity.cart;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import com.guozha.buy.entry.cart.PayOrderMesg;
import com.guozha.buy.entry.cart.PayWayEntry;
import com.guozha.buy.entry.cart.PreSpecialOrder;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.server.AlipayManager;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.PayResult;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 特供预售支付（特供预售不需要加入购物车)
 * 
 * 
 * 界面逻辑说明：
 * 1、5.2接口，本地服务器支付
 * 2、请求第三方支付平台
 * 3、添加订单
 * @author PeggyTong
 *
 */
public class PreSpecialPayActivity extends BaseActivity implements OnClickListener{
	
	private static final int REQUEST_CODE = 0x0001;
	
	private static final int HAND_PAY_WAY_COMPLETED = 0x0004;		//支付方式请求完毕
	private static final int HAND_PAY_SUCCESSED = 0x0007;			//支付成功
	private static final int HAND_ORDER_COMPLETED = 0x008;			//订单提交成功
	private static final int HAND_CHOOSED_TICKET_COMPLETED = 0x0009; //选择菜谱完成
	
	private int mGoodsId;  //商品Id
	private int mUnitPrice;		//商品单价
	private String mGoodsName; //商品名称
	private String mGoodsType;	//01 普通商品 02 特供 03 预售
	private int mAmount = 1;   //商品数量
	
	private int mOrderId = -1; //订单Id
	
	private int mTicketId = -1;			//菜票Id
	private int mTicketPrice;		//菜票面额
	
	private PayOrderMesg mPayOrderMesg;		//支付信息
	
	/**
	 * 地址相关
	 */
	private TextView mAddressNameText;
	private TextView mAddressMobileText;
	private TextView mAddressDetailText;
	/**
	 * 商品信息
	 */
	private TextView mItemNameText;
	private View mItemMinus;
	private TextView mItemNumText;
	private View mItemPlus;
	private TextView mItemPriceText;
	
	private EditText mPreSpeMemoText;	//留言
	
	/**
	 * 付款信息相关
	 */
	private View mAccountRemainView;
	private TextView mAccountRemainText;
	private ImageView mAccountRemainIcon;
	private View mBeanRemainView;
	private TextView mBeanRemainText;
	private ImageView mBeanRemainIcon;
	
	private View mTicketView;
	private TextView mTicketText;
	private ImageView mTicketArrowIcon;	//菜票右边箭头
	
	private TextView mNeedPayPriceText;
	
	private TextView mCanUseMoneyDeduct;	//扣除余额
	
	/**
	 * 支付相关
	 */
	
	private View mPayZhifubaoView;
	private View mPayWeixinView;
	private View mPayWangyingView;
	private View mPayHuodaofukuanView;
	
	private ImageView mPayZhifubaoIcon;
	private ImageView mPayWeixinIcon;
	private ImageView mPayWangyingIcon;
	private ImageView mPayHuodaofukuanIcon;
	
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
							ToastUtil.showToast(PreSpecialPayActivity.this, "支付结果确认中");
							Intent intent = new Intent(PreSpecialPayActivity.this, MyOrderActivity.class);
							startActivity(intent);
							PreSpecialPayActivity.this.finish();
						} else {
							ToastUtil.showToast(PreSpecialPayActivity.this, "支付失败");
						}
					}
					break;
				case AlipayManager.SDK_CHECK_FLAG: 
					ToastUtil.showToast(PreSpecialPayActivity.this, "检查结果为：" + msg.obj);
					break;
				case HAND_PAY_WAY_COMPLETED:
					for(int i = 0; i < mPayWayList.size(); i++){
						switch (mPayWayList.get(i).getPayWayId()) {
						case 1:			//支付宝支付
							mPayZhifubaoView.setVisibility(View.VISIBLE);
							break;
						case 2:			//微信支付
							mPayWeixinView.setVisibility(View.VISIBLE);
							break;
						case 3:			//网银支付
							mPayWangyingView.setVisibility(View.VISIBLE);
							break;
						case 4:			//货到付款
							mPayHuodaofukuanView.setVisibility(View.VISIBLE);
							break;
						};
					}
					break;
				case HAND_PAY_SUCCESSED:
					ToastUtil.showToast(PreSpecialPayActivity.this, "支付成功");
					Intent intent = new Intent(PreSpecialPayActivity.this, MyOrderActivity.class);
					startActivity(intent);
					PreSpecialPayActivity.this.finish();
					break;
				case HAND_ORDER_COMPLETED:
					requestPayMoney();
					break;
				case HAND_CHOOSED_TICKET_COMPLETED: //选择菜谱成功
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
		setContentView(R.layout.activity_prespecial_pay);
		customActionBarStyle("订单确认");
		
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mGoodsId = bundle.getInt("goodsId");
				mUnitPrice = bundle.getInt("unitPrice");
				mGoodsName = bundle.getString("goodsName");
				mGoodsType = bundle.getString("goodsType");
			}
		}
		mTotalPrice = mUnitPrice;
		
		initView();
		
		initData();
	}
	
	private void initView(){
		//地址相关
		mAddressNameText = (TextView) findViewById(R.id.prespecial_pay_address_name);
		mAddressMobileText = (TextView) findViewById(R.id.prespecial_pay_address_mobile);
		mAddressDetailText = (TextView) findViewById(R.id.prespecial_pay_address_detail);
		
		//商品相关
		mItemNameText = (TextView) findViewById(R.id.cart_list_cell_title);
		mItemMinus = findViewById(R.id.cart_list_cell_minus);
		mItemNumText = (TextView) findViewById(R.id.cart_list_cell_num);
		mItemPlus = findViewById(R.id.cart_list_cell_plus);
		mItemPriceText = (TextView) findViewById(R.id.cart_list_cell_price);
		
		mItemMinus.setOnClickListener(this);
		mItemPlus.setOnClickListener(this);
		
		//留言
		mPreSpeMemoText = (EditText) findViewById(R.id.prespecial_memo);
		
		//付款信息相关
		mAccountRemainView = findViewById(R.id.prespecial_account_remain_view);
		mAccountRemainText = (TextView) findViewById(R.id.prespecial_account_remain_text);
		mAccountRemainIcon = (ImageView) findViewById(R.id.prespecial_account_remain_icon);
		mBeanRemainView = findViewById(R.id.prespecial_bean_remain_view);
		mBeanRemainText = (TextView) findViewById(R.id.prespecial_bean_remain_text);
		mBeanRemainIcon = (ImageView) findViewById(R.id.prespecial_bean_remain_icon);
		mTicketView = findViewById(R.id.my_ticket_view);
		mTicketText = (TextView) findViewById(R.id.my_ticket_text);
		mTicketArrowIcon = (ImageView) findViewById(R.id.pay_ticket_icon);
		mCanUseMoneyDeduct = (TextView) findViewById(R.id.pay_can_use_money_deduct);
		
		mTicketView.setOnClickListener(this);
		
		mNeedPayPriceText = (TextView) findViewById(R.id.prespecial_need_pay_text);
		
		mNeedPayPriceText.setText(UnitConvertUtil.getSwitchedMoney(mTotalPrice + mServicePrice) + "元");
		
		findViewById(R.id.pay_way_zhifubao);
		
		mAccountRemainView.setOnClickListener(this);
		mBeanRemainView.setOnClickListener(this);
		
		
		//支付相关
		mPayZhifubaoView = findViewById(R.id.pre_pay_way_zhifubao_view);
		mPayWeixinView = findViewById(R.id.pre_pay_way_weixin_view);
		mPayWangyingView = findViewById(R.id.pre_pay_way_wangying_view);
		mPayHuodaofukuanView = findViewById(R.id.pre_pay_way_huodaofukuan_view);
		
		mPayZhifubaoIcon = (ImageView) findViewById(R.id.pre_pay_way_zhifubao_icon);
		mPayWeixinIcon = (ImageView) findViewById(R.id.pre_pay_way_weixin_icon);
		mPayWangyingIcon = (ImageView) findViewById(R.id.pre_pay_way_wangying_icon);
		mPayHuodaofukuanIcon = (ImageView) findViewById(R.id.pre_pay_way_huodaofukuan_icon);
		
		mPayZhifubaoView.setOnClickListener(this);
		mPayWeixinView.setOnClickListener(this);
		mPayWangyingView.setOnClickListener(this);
		mPayHuodaofukuanView.setOnClickListener(this);
		
		findViewById(R.id.prespecial_pay_button).setOnClickListener(this);
		
		mItemNameText.setText(mGoodsName);
		mItemPriceText.setText("￥" + UnitConvertUtil.getSwitchedMoney(mUnitPrice));
	}
	
	private boolean mBeanChecked = false;				//是否选择使用菜豆
	private boolean mAccountRemainChecked = false;		//是否选择使用账户余额
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.prespecial_account_remain_view:
			if(mAccountRemainIcon == null) return;
			mAccountRemainChecked = exchangeIcon(mAccountRemainIcon);
			setPayPriceText();
			break;
		case R.id.prespecial_bean_remain_view:
			if(mBeanRemainIcon == null) return;
			mBeanChecked = exchangeIcon(mBeanRemainIcon);
			setPayPriceText();
			break;
		case R.id.pre_pay_way_zhifubao_view:
			payWayExchangeIcon(mPayZhifubaoIcon, PayWay.ZHI_FU_BAO);
			break;
		case R.id.pre_pay_way_weixin_view:
			payWayExchangeIcon(mPayWeixinIcon, PayWay.WEI_XIN);
			break;
		case R.id.pre_pay_way_wangying_view:
			payWayExchangeIcon(mPayWangyingIcon, PayWay.WANG_YING);
			break;
		case R.id.pre_pay_way_huodaofukuan_view:
			payWayExchangeIcon(mPayHuodaofukuanIcon, PayWay.HUO_DAO_FU_KUAN);
			break;
		case R.id.my_ticket_view:	//选择菜票
			Intent intent = new Intent(PreSpecialPayActivity.this, ChooseTicketActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
			break;
		case R.id.cart_list_cell_minus:
			if(mAmount <= 1){
				mAmount = 1;
			}else{
				mAmount--;
			}
			mItemNumText.setText(mAmount + "份");
			mTotalPrice = mUnitPrice * mAmount;
			mItemPriceText.setText("￥" + UnitConvertUtil.getSwitchedMoney(mTotalPrice));
			setPayPriceText();
			break;
		case R.id.cart_list_cell_plus:
			mAmount++;
			mItemNumText.setText(mAmount + "份");
			mTotalPrice = mUnitPrice * mAmount;
			mItemPriceText.setText("￥" + UnitConvertUtil.getSwitchedMoney(mTotalPrice));
			setPayPriceText();
			break;
		case R.id.prespecial_pay_button:
			if("02".equals(mGoodsType)){
				requestAddOrder("order/insertSupply"); 		//特供
			}else{ 
				requestAddOrder("order/insertPrepare");		//预售
			}
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
		mPayZhifubaoIcon.setImageResource(R.drawable.pay_selectbox_normal);
		mPayWangyingIcon.setImageResource(R.drawable.pay_selectbox_normal);
		mPayWeixinIcon.setImageResource(R.drawable.pay_selectbox_normal);
		mPayHuodaofukuanIcon.setImageResource(R.drawable.pay_selectbox_normal);
		
		mPayZhifubaoIcon.setTag("0");
		mPayWangyingIcon.setTag("0");
		mPayWeixinIcon.setTag("0");
		mPayHuodaofukuanIcon.setTag("0");
	}
	
	private int mAccountRemainDeduct = 0; //扣除的余额
	private int mBeanrDeduct = 0;		  //扣除菜豆
	
	/**
	 * 设置应付多少款文字
	 */
	private void setPayPriceText() {
		LogUtil.e("mTotalPrice = " + mTotalPrice);
		LogUtil.e("mServicePrice = " + mServicePrice);
		int payPrice = mTotalPrice + mServicePrice;
		if(mAccountRemainChecked){
			if(payPrice > mAccountRemain){
				payPrice = payPrice - mAccountRemain;
				mAccountRemainDeduct = mAccountRemain;
			}else{
				payPrice = 0;
				mAccountRemainDeduct = payPrice;
				mTicketId = -1;
				mBeanrDeduct = 0;
				mBeanRemainView.setVisibility(View.GONE);
				mTicketView.setVisibility(View.GONE);
			}
			mCanUseMoneyDeduct.setText(" 扣除" + UnitConvertUtil.getSwitchedMoney(mAccountRemainDeduct) + "元");
		}else{
			mBeanRemainView.setVisibility(View.VISIBLE);
			mTicketView.setVisibility(View.VISIBLE);
		}
		if(mBeanChecked){
			payPrice = payPrice - mBeanNum;
			if(payPrice < 0){
				payPrice = 0;
			}
			mBeanrDeduct = mBeanNum;
		}
		LogUtil.e("payPrice == " + payPrice);
		mNeedPayPriceText.setText(UnitConvertUtil.getSwitchedMoney(payPrice) + "元");
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
	
	
	private int mTotalPrice;
	private int mServicePrice;
	private int mAccountRemain;
	private int mBeanNum;
	private List<PayWayEntry> mPayWayList;  //支付方式
	private void initData(){
		//设置地址信息
		List<AddressInfo> addressInfos = 
				MainPageInitDataManager.getInstance(this).getAddressInfos(null);
		if(addressInfos != null){
			for(int i = 0; i < addressInfos.size(); i++){
				AddressInfo addressInfo = addressInfos.get(i);
				if(addressInfo.getAddressId() == ConfigManager.getInstance().getChoosedAddressId()){
					mAddressNameText.setText(addressInfo.getReceiveName());
					mAddressMobileText.setText(addressInfo.getMobileNo());
					mAddressDetailText.setText(addressInfo.getBuildingName() + addressInfo.getDetailAddr());
				}
			}
		}
		
		//设置商品信息
		//获取账户信息(菜豆、菜票）
		AccountInfo accountInfo = 
				MainPageInitDataManager.getInstance(CustomApplication.getContext()).getAccountInfo(null);
		if(accountInfo != null){
			mAccountRemain = accountInfo.getBalance();
			mBeanNum = accountInfo.getBeanAmount();
			mAccountRemainText.setText("可用账户余额" +
						UnitConvertUtil.getSwitchedMoney(mAccountRemain));	 //账户余额
			mBeanRemainText.setText("可用菜豆数" + mBeanNum + "个" + UnitConvertUtil.getBeanMoney(mBeanNum) + "元"); //菜豆数
		}
		
		//获取支付方式
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("payment/listPayWay")
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE){
			if(data == null) return;
			Bundle bundle = data.getExtras();
			if(bundle != null){
				mTicketId = bundle.getInt("ticktId");
				mTicketPrice = bundle.getInt("ticketPrice");
			} 
		}
	}
	
	/**
	 * 请求提交订单
	 */
	private void requestAddOrder(String uri){
		
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
			ToastUtil.showToast(PreSpecialPayActivity.this, "请选择支付方式");
			return;
		}
		
		String token = ConfigManager.getInstance().getUserToken();
		int userId = ConfigManager.getInstance().getUserId();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		
		RequestParam paramPath = new RequestParam(uri)
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("orderId", "0")
		.setParams("balanceDecPrice", mAccountRemainDeduct)
		.setParams("useBeanAmount", mBeanrDeduct)
		.setParams("useTicketId", mTicketId == -1 ? "0": String.valueOf(mTicketId))
		.setParams("payWayId", payWayId)
		.setParams("addressId", addressId)
		.setParams("goodsId", mGoodsId)
		.setParams("amount", mAmount)
		.setParams("memo", mPreSpeMemoText.getText().toString());
		
		HttpManager.getInstance(PreSpecialPayActivity.this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					PreSpecialOrder preSpecialOrder = 
							gson.fromJson(response, new TypeToken<PreSpecialOrder>() { }.getType());
					if(preSpecialOrder != null){
						String flag = preSpecialOrder.getNeedPayFlag();
						String returnCode = preSpecialOrder.getReturnCode();
						mPayOrderMesg = preSpecialOrder.getOrder();
						if("1".equals(returnCode)){
							if("0".equals(flag)){
								mHandler.sendEmptyMessage(HAND_PAY_SUCCESSED);
							}else{
								mHandler.sendEmptyMessage(HAND_ORDER_COMPLETED);
							}
						}else{
							ToastUtil.showToast(PreSpecialPayActivity.this, preSpecialOrder.getMsg());
						}
					}
				}
		});
	}
	
	/**
	 * 请求支付
	 */
	private void requestPayMoney() {
		if(mPayWay == null) {
			ToastUtil.showToast(PreSpecialPayActivity.this, "请选择支付方式");
			return;
		}
		switch (mPayWay) {
		case ZHI_FU_BAO:
			String tag = String.valueOf(mPayZhifubaoIcon.getTag());
			if("1".equals(tag)){
				//AlipayManager alipayManager = new AlipayManager(mPayOrderMesg.getOrderNo(), 
				//		mPayOrderMesg.getFirstShowName() + "_特供预售", mPayOrderMesg.getMemo(), UnitConvertUtil.getSwitchedMoney(mPayOrderMesg.getPayPrice()));
				//alipayManager.requestPay(this, mHandler);
				ToastUtil.showToast(PreSpecialPayActivity.this, "支付了");
				LogUtil.e("orderNum = " + mPayOrderMesg.getOrderNo());
				LogUtil.e("subject =  " + mPayOrderMesg.getFirstShowName() + "等" + mPayOrderMesg.getQuantity() + "件商品");
				LogUtil.e("object = " + mPayOrderMesg.getMemo());
				LogUtil.e("payMoney = " + UnitConvertUtil.getSwitchedMoney(mPayOrderMesg.getPayPrice()) + "元");
			}else{
				ToastUtil.showToast(PreSpecialPayActivity.this, "请选择支付方式");
			}
			break;
		case WEI_XIN:
			
			break;
		case HUO_DAO_FU_KUAN:
			
			break;
		case WANG_YING:
			
			break;
		}
	}
}
