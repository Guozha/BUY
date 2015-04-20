package com.guozha.buy.dialog;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.market.VegetableDetailActivity;
import com.guozha.buy.entry.global.WeightOption;
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
 * 重量选择对话框
 * @author PeggyTong
 *
 */
public class WeightSelectDialog extends Activity implements OnClickListener{
	
	private static final String PAGE_NAME = "WeightSelectPage";
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private WheelView mWheelView;
	
	private View mCustomWeightArea;
	private EditText mCustomWeight;
	private TextView mCustomWeightUnit;
	
	private List<WeightOption> options;
	
	private String mGoodsId = null;
	private String mUnitPrice = null;
	private String mUnit = null;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateView();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_weight_select);
		//让Dialog全屏
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mGoodsId = bundle.getString("goodsId");
				mUnitPrice = bundle.getString("unitPrice");
				mUnit = bundle.getString("unit");
			}
		}
		setResult(0);
		
		mWheelView = (WheelView) findViewById(R.id.select_weight_wheelview);
		mWheelView.setVisibleItems(5); // Number of items
		mWheelView.setWheelBackground(R.drawable.wheel_bg_holo);
		mWheelView.setWheelForeground(R.drawable.wheel_val_holo);
		mWheelView.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mWheelView.setLastItemListener(new ItemChangeListener() {
			@Override
			public void itemChanged(int index) {
				if(index >= options.size() - 1){
					mCustomWeightArea.setVisibility(View.VISIBLE);
				}else{
					mCustomWeightArea.setVisibility(View.GONE);
				}
			}
		});
		
		requestWeightData();
		
		findViewById(R.id.select_weight_to_details).setOnClickListener(this);
		findViewById(R.id.select_weight_confirm).setOnClickListener(this);
		findViewById(R.id.select_weight_free_layout).setOnClickListener(this);
		
		mCustomWeightArea = findViewById(R.id.custom_weight_area);
		mCustomWeight = (EditText) findViewById(R.id.select_custom_weight_text);
		mCustomWeightUnit = (TextView) findViewById(R.id.select_custom_weight_unit);
	}
	
	/**
	 * 更新当前界面
	 */
	private void updateView() {
		mWheelView.setViewAdapter(
				new WeightOptionAdapter(WeightSelectDialog.this, options));
		for(int i = 0; i < options.size(); i++){
			if(1 == options.get(i).getDefaultFlag()){
				mWheelView.setCurrentItem(i);
			}
		}
		
		if(options != null){
			if(options.isEmpty()) return;
			WeightOption weightOption = options.get(options.size()-1);
			String textUnit = UnitConvertUtil.getSwichedUnit(
					weightOption.getAmount(), mUnit);
			mCustomWeight.setText(UnitConvertUtil.getSwitchWeightNum(weightOption.getAmount(), mUnit));
			mCustomWeightUnit.setText(
					textUnit);
		}
	};
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.select_weight_to_details:
			Intent intent = new Intent(this, VegetableDetailActivity.class);
			intent.putExtra("goodsId", mGoodsId);
			startActivity(intent);
			this.finish();
			break;
		case R.id.select_weight_confirm:
			int quantity = obtainQuantity();
			if(quantity == -1) return;
			requestAddCart(quantity);
			break;
		case R.id.select_weight_free_layout:
			WeightSelectDialog.this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取数量
	 * @return
	 */
	private int obtainQuantity() {
		int currentItem = mWheelView.getCurrentItem();
		int quantity = -1;
		if(options == null) return -1;
		if(currentItem == options.size() - 1){
			//自定义的
			String quntityStr = mCustomWeight.getText().toString().trim();
			if(RegularUtil.regularNumber(quntityStr)){
				quantity = Integer.parseInt(quntityStr);
				quantity = UnitConvertUtil.getCommitWeight(
						quantity, mCustomWeightUnit.getText().toString());
				if(quantity < options.get(0).getAmount()){
					ToastUtil.showToast(this, "不能小于最小起送量");
					return -1;
				}
			}else{
				ToastUtil.showToast(this, "只允许填写数字");
				return -1;
			}
		}else{
			quantity = options.get(currentItem).getAmount();
		}
		return quantity;
	}

	/**
	 * 请求添加到购物车
	 * @param quantity
	 */
	private void requestAddCart(int quantity) {
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(WeightSelectDialog.this);
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		
		RequestParam paramPath = new RequestParam("cart/insert")
		.setParams("userId", userId)
		.setParams("id", mGoodsId)
		.setParams("productType", "02")  //02是商品
		.setParams("amount", quantity)
		.setParams("token", token)
		.setParams("addressId", addressId);
		
		LogUtil.e(HttpManager.URL + paramPath);
		HttpManager.getInstance(this).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode");
					if("1".equals(returnCode)){
						ToastUtil.showToast(WeightSelectDialog.this, "已添加到购物车");
						MainPageInitDataManager.mCartItemsUpdated = true;
						WeightSelectDialog.this.finish();
					}else{
						ToastUtil.showToast(WeightSelectDialog.this, response.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void requestWeightData(){
		RequestParam paramPath = new RequestParam("goods/amount")
		.setParams("goodsId", mGoodsId);
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				options = gson.fromJson(response, new TypeToken<List<WeightOption>>() { }.getType());
				if(options != null){
					handler.sendEmptyMessage(HAND_DATA_COMPLETED);
				}
			}
		});
	}
	
	private class WeightOptionAdapter extends AbstractWheelTextAdapter {
		private List<WeightOption> options;

		protected WeightOptionAdapter(Context context,
				List<WeightOption> options) {
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
			WeightOption option = options.get(index);
			if(index >= getItemsCount() - 1){
				return "自定义重量";
			}else{
				return UnitConvertUtil.getSwitchedWeight(option.getAmount(), mUnit) 
						+ "-" + UnitConvertUtil.getPriceByAmount(option.getAmount(), Integer.parseInt(mUnitPrice), mUnit)
						+ "元";
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
