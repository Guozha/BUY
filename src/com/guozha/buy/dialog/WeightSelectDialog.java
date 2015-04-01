package com.guozha.buy.dialog;

import java.util.List;

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
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.market.VegetableDetailActivity;
import com.guozha.buy.entry.global.WeightOption;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.RegularUtil;
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
	
	private String mGoodsId;
	
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
			}
		}
		
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
		if(options != null){
			mCustomWeight.setText(
					String.valueOf(10));
			WeightOption weightOption = options.get(0);
			mCustomWeightUnit.setText(
					UnitConvertUtil.getSwichedUnit(
							weightOption.getAmount(), weightOption.getUnit()));
		}
	};
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.select_weight_to_details:
			Intent intent = new Intent(this, VegetableDetailActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.select_weight_confirm:
			int currentItem = mWheelView.getCurrentItem();
			int quantity = -1;
			if(currentItem == options.size() - 1){
				//自定义的
				String quntityStr = mCustomWeight.getText().toString().trim();
				if(RegularUtil.regularNumber(quntityStr)){
					quantity = Integer.parseInt(quntityStr);
				}
			}else{
				quantity = options.get(currentItem).getAmount();
			}
			
			Toast.makeText(this, "选择的重量是 = " + quantity, Toast.LENGTH_SHORT).show();
			

			break;
		case R.id.select_weight_free_layout:
			WeightSelectDialog.this.finish();
			break;
		default:
			break;
		}
	}
	
	private void requestWeightData(){
		RequestParam paramPath = new RequestParam("goods/price")
		.setParams("goodsId", mGoodsId)
		.setParams("addressId", 0);
		HttpManager.getInstance(this).volleyRequestByPost(HttpManager.URL + paramPath, new Listener<String>() {

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
				return UnitConvertUtil.getSwitchedWeight(option.getAmount(), 
						option.getUnit()) + "-" + UnitConvertUtil.getSwitchedMoney(option.getPrice());
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
