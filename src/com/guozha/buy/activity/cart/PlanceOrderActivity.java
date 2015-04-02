package com.guozha.buy.activity.cart;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.entry.cart.PointTime;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.view.scroll.WheelView;
import com.guozha.buy.view.scroll.WheelView.ItemChangeListener;
import com.guozha.buy.view.scroll.adapter.AbstractWheelTextAdapter;

/**
 * 下单界面
 * @author PeggyTong
 *
 */
public class PlanceOrderActivity extends BaseActivity{
	
	private WheelView mWheelView;
	
	private static final int HAND_TIMES_DATA_COMPLETED = 0x0001;
	
	private List<PointTime> options;
	
	private TextView mOrderName;
	private TextView mOrderPhone;
	private TextView mOrderAddress;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_TIMES_DATA_COMPLETED:
				if(mWheelView == null) return;
				mWheelView.setViewAdapter(new TimeOptionAdapter(PlanceOrderActivity.this, options));
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plance_order);
		customActionBarStyle("下单");
		
		initView();
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
				
			}
		});
		
		addData();
		
		findViewById(R.id.plance_order_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO 提交菜场订单
				int userId = ConfigManager.getInstance().getUserId();
				String token = ConfigManager.getInstance().getUserToken();
				int addressId = ConfigManager.getInstance().getChoosedAddressId();
				RequestParam paramPath = new RequestParam("order/insert")
				.setParams("token", token)
				.setParams("userId", userId);
				//.setParams("addressId", addressId)
				//.setParams("wantUpTime", value)
				//.setParams("wantDownTime", )
				//.setParams("memo", );
				HttpManager.getInstance(PlanceOrderActivity.this).volleyJsonRequestByPost(
						HttpManager.URL + paramPath, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
					
						//String returnCode = response.getString("returnCode");
					}
				});
				Intent intent = new Intent(PlanceOrderActivity.this, PayActivity.class);
				startActivity(intent);
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
	}
	
	private void addData(){
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("order/times")
		.setParams("addressId", addressId);
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				options = gson.fromJson(response, new TypeToken<List<PointTime>>() { }.getType());
				if(options != null){
					handler.sendEmptyMessage(HAND_TIMES_DATA_COMPLETED);
				}
			}
		});
	}
	
	private class TimeOptionAdapter extends AbstractWheelTextAdapter{
		private List<PointTime> options;

		protected TimeOptionAdapter(Context context,
				List<PointTime> options) {
			super(context, R.layout.item_select_weight, NO_RESOURCE);
			this.options = options;
			setItemTextResource(R.id.city_name);
		}
		
		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return options.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			PointTime option = options.get(index);
			return "今天       " + option.getFromTime() + "-" + option.getToTime();
		}
	}
}
