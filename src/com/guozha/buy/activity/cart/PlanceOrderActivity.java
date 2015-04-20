package com.guozha.buy.activity.cart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.guozha.buy.entry.cart.PointTime;
import com.guozha.buy.entry.cart.ShowTime;
import com.guozha.buy.entry.cart.TimeList;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.RegularUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.guozha.buy.view.scroll.WheelView;
import com.guozha.buy.view.scroll.WheelView.ItemChangeListener;
import com.guozha.buy.view.scroll.adapter.AbstractWheelTextAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 下单界面
 * @author PeggyTong
 *
 */
public class PlanceOrderActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SubmitOrderPage";
	
	private WheelView mWheelView;
	
	private static final int HAND_TIMES_DATA_COMPLETED = 0x0001;
	private static final int HAND_CHANGE_QUICK_MENU = 0x0002;
	
	private List<ShowTime> mShowTimes = new ArrayList<ShowTime>();
	
	private int mTotalPrice = -1;
	private int mServicePrice = -1;
	private TextView mTotalPriceText;
	
	private TextView mOrderName;
	private TextView mOrderPhone;
	private TextView mOrderAddress;
	private EditText mLeaveMessage;
	private ImageView mQuickTimeChooseIcon;
	private TextView mQuickTimeChooseText;
	private View mQuickTimeChoose;
	private String mTodayEarliestTime = null;  //当天最早的时间
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_TIMES_DATA_COMPLETED:
				if(mWheelView == null) return;
				mWheelView.setViewAdapter(new TimeOptionAdapter(PlanceOrderActivity.this));
				if(mShowTimes.size() >= 2){
					mWheelView.setCurrentItem(1);
				}
				mWheelView.setVisibility(View.VISIBLE);
				break;
			case HAND_CHANGE_QUICK_MENU:
				if(mQuickTimeChoose != null){
					mQuickTimeChoose.setVisibility(View.GONE);
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plance_order);
		customActionBarStyle("下单");
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mTotalPrice = bundle.getInt("totalPrice");
				mServicePrice = bundle.getInt("serverPrice");
			}
		}
		initView();
		initData();
		//重新获取一下服务器时间
		MainPageInitDataManager.getInstance(
				CustomApplication.getContext()).getTodayInfo(null);
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mWheelView = (WheelView) findViewById(R.id.select_time_wheelview);
		mWheelView.setVisibleItems(5);
		//设置背景颜色
		mWheelView.setWheelBackground(R.drawable.wheel_bg_white);
		mWheelView.setWheelForeground(R.drawable.wheel_val_white);
		mWheelView.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mWheelView.setLastItemListener(new ItemChangeListener() {
			@Override
			public void itemChanged(int index) {
				if(index == 0){
					if(!regularCanOneHourArrive()) return;
					setChooseOneHourArrive();
				}else{
					setCancelOneHourArrive();
				}
			}
		});

		
		findViewById(R.id.plance_order_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//提交菜场订单
				requestSubmitOrder();
			}
		});
		
		mOrderName = (TextView) findViewById(R.id.order_name);
		mOrderPhone = (TextView) findViewById(R.id.order_phone);
		mOrderAddress = (TextView) findViewById(R.id.order_address);
		
		//设置地址信息
		List<AddressInfo> addressInfos = 
				MainPageInitDataManager.getInstance(this).getAddressInfos(null);
		if(addressInfos != null){
			for(int i = 0; i < addressInfos.size(); i++){
				AddressInfo addressInfo = addressInfos.get(i);
				if(addressInfo.getAddressId() == ConfigManager.getInstance().getChoosedAddressId()){
					mOrderName.setText(addressInfo.getReceiveName());
					mOrderPhone.setText(addressInfo.getMobileNo());
					mOrderAddress.setText(addressInfo.getBuildingName() + addressInfo.getDetailAddr());
				}
			}
		}
		
		mLeaveMessage = (EditText) findViewById(R.id.leave_message);
		mQuickTimeChooseIcon = (ImageView) findViewById(R.id.quick_time_choose_icon);
		mQuickTimeChooseText = (TextView) findViewById(R.id.quick_time_choose_text);
		mQuickTimeChoose = findViewById(R.id.quick_time_choose);
		mQuickTimeChoose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String tag = String.valueOf(mQuickTimeChooseIcon.getTag());
				if(tag == null || "null".equals(tag) ||  "0".equals(tag)){
					if(!regularCanOneHourArrive()) {
						ToastUtil.showToast(PlanceOrderActivity.this, "当前时间不支持1小时送达");
						return;
					}
					setChooseOneHourArrive();
					if(!mShowTimes.isEmpty()){
						mWheelView.setCurrentItem(0, true);
					}
				}else{
					setCancelOneHourArrive();
					if(mShowTimes.size() >=2){
						mWheelView.setCurrentItem(1, true);
					}
				}
			}
		});
		
		mTotalPriceText = (TextView) findViewById(R.id.plance_order_total_price);
		mTotalPriceText.setText(UnitConvertUtil.getSwitchedMoney(mTotalPrice) + "元");
	}
	
	private void setCancelOneHourArrive() {
		mQuickTimeChooseIcon.setTag("0");
		mQuickTimeChooseIcon.setImageResource(R.drawable.truck_unselected);
		mQuickTimeChooseText.setText("1小时速达");
		mQuickTimeChooseText.setTextColor(getResources().getColor(R.color.color_app_base_4));
	}

	private void setChooseOneHourArrive() {
		mQuickTimeChooseIcon.setTag("1");
		mQuickTimeChooseIcon.setImageResource(R.drawable.truck);
		mQuickTimeChooseText.setText("1小时之内给您送到");
		mQuickTimeChooseText.setTextColor(getResources().getColor(R.color.color_app_base_1));
	}

	/**
	 * 验证是否可以支持一小时送达
	 * @return
	 */
	private boolean regularCanOneHourArrive() {
		long todayDate = ConfigManager.getInstance().getTodayDate();
		if(todayDate == -1) return false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		calendar.setTimeInMillis(todayDate);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if(mTodayEarliestTime == null) return false;
		String earliesHour = mTodayEarliestTime.split(":")[0];
		if(!RegularUtil.regularNumber(earliesHour)) return false;
		int eHour = Integer.parseInt(earliesHour);
		if(eHour - hour > 1) return false;
		return true;
	}
	
	/**
	 * 请求提交订单
	 */
	private void requestSubmitOrder() {
		String tag = String.valueOf(mQuickTimeChooseIcon.getTag());
		int chooseItem;
		if("1".equals(tag)){
			chooseItem = 0;
		}else{
			chooseItem = mWheelView.getCurrentItem();
		}
		if(mShowTimes == null || mShowTimes.isEmpty()) {
			ToastUtil.showToast(PlanceOrderActivity.this, "请选择时间段");
			return;
		}
		ShowTime pointTime = mShowTimes.get(chooseItem);
		if(pointTime == null) {
			ToastUtil.showToast(PlanceOrderActivity.this, "请选择时间段");
			return;
		}
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(PlanceOrderActivity.this);
		if(token == null) return;
		int addressId = ConfigManager.getInstance().getChoosedAddressId(PlanceOrderActivity.this);
		if(addressId == -1) return;
		long todayDate = ConfigManager.getInstance().getTodayDate();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		calendar.setTimeInMillis(todayDate);
		if("明天".equals(pointTime.getDayname())){
			calendar.add(Calendar.DATE, 1);
		}
		String timeStr = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+ 1)+ "-" + calendar.get(Calendar.DAY_OF_MONTH);
		RequestParam paramPath = new RequestParam("order/insert")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("addressId", addressId)
		.setParams("wantUpTime", timeStr + "$" + pointTime.getFromTime())
		.setParams("wantDownTime", timeStr + "$" + pointTime.getToTime())
		.setParams("memo", mLeaveMessage.getText().toString());
		
		HttpManager.getInstance(PlanceOrderActivity.this).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode");
					if("1".equals(returnCode)){
						Intent intent = new Intent(PlanceOrderActivity.this, PayActivity.class);
						intent.putExtra("orderId", response.getInt("orderId"));
						intent.putExtra("serverPrice", mServicePrice);
						startActivity(intent);
						MainPageInitDataManager.mCartItemsUpdated = true;
						PlanceOrderActivity.this.finish();
					}else{
						ToastUtil.showToast(PlanceOrderActivity.this, response.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	/**
	 * 添加数据
	 */
	private void initData(){
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("order/times")
		.setParams("addressId", addressId);
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				TimeList timeList = gson.fromJson(response, new TypeToken<TimeList>() { }.getType());
				if(timeList != null){
					List<PointTime> todayPointTime = timeList.getTodayTimeList();
					if(todayPointTime == null || todayPointTime.isEmpty()){
						handler.sendEmptyMessage(HAND_CHANGE_QUICK_MENU);
					}else{
					mTodayEarliestTime = null;
					for(int i = 0; i < todayPointTime.size(); i++){
							if(i == 0){
								mTodayEarliestTime = todayPointTime.get(i).getFromTime();
							}
							mShowTimes.add(new ShowTime("今天", 
									todayPointTime.get(i).getFromTime(), todayPointTime.get(i).getToTime()));
						}
					}
					List<PointTime> tomorrowPointTime = timeList.getTomorrowTimeList();
					for(int i = 0; i < tomorrowPointTime.size(); i++){
						mShowTimes.add(new ShowTime("明天", 
								tomorrowPointTime.get(i).getFromTime(), tomorrowPointTime.get(i).getToTime()));
					}
					handler.sendEmptyMessage(HAND_TIMES_DATA_COMPLETED);
				}
			}
		});
	}
	
	private class TimeOptionAdapter extends AbstractWheelTextAdapter{
		
		protected TimeOptionAdapter(Context context) {
			super(context, R.layout.item_select_weight, NO_RESOURCE);
			setItemTextResource(R.id.city_name);
		}
		
		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			if(mShowTimes == null) return 0;
			return mShowTimes.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			ShowTime showTime = mShowTimes.get(index);
			return showTime.getDayname() + "    " + 
					showTime.getFromTime() + "-" + showTime.getToTime();
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
