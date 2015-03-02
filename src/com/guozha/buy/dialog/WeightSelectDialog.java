package com.guozha.buy.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.guozha.buy.R;
import com.guozha.buy.entry.ItemSaleInfo;
import com.guozha.buy.entry.ItemSaleInfo.WeightOption;
import com.guozha.buy.view.scroll.WheelView;
import com.guozha.buy.view.scroll.adapter.AbstractWheelTextAdapter;

/**
 * 重量选择对话框
 * @author PeggyTong
 *
 */
public class WeightSelectDialog extends Activity implements OnClickListener{
	
	private WheelView mWheelView;
	
	private List<ItemSaleInfo.WeightOption> options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_weight_select);
		//让Dialog全屏
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		/*
		findViewById(R.id.diloag_outside).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				WeightSelectDialog.this.finish();
			}
		});
		*/
		
		mWheelView = (WheelView) findViewById(R.id.select_weight_wheelview);
		mWheelView.setVisibleItems(5); // Number of items
		mWheelView.setWheelBackground(R.drawable.wheel_bg_holo);
		mWheelView.setWheelForeground(R.drawable.wheel_val_holo);
		mWheelView.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		
		addData();
		
		mWheelView.setViewAdapter(new WeightOptionAdapter(this, options));
		
		findViewById(R.id.select_weight_to_details).setOnClickListener(this);
		findViewById(R.id.select_weight_confirm).setOnClickListener(this);
		findViewById(R.id.select_weight_free_layout).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.select_weight_to_details:
			
			break;
		case R.id.select_weight_confirm:
			int currentItem = mWheelView.getCurrentItem();
			Toast.makeText(WeightSelectDialog.this, 
					"currentItem=" + currentItem, Toast.LENGTH_SHORT).show();
			break;
		case R.id.select_weight_free_layout:
			WeightSelectDialog.this.finish();
			break;
		default:
			break;
		}
	}
	
	private void addData(){
		options = new ArrayList<ItemSaleInfo.WeightOption>();
		
		
		for(int i=0; i<8; i++){
			WeightOption option = new WeightOption();
			option.setQuantity(i + 1);
			option.setMoneyUnit("块");
			option.setUnit("斤");
			option.setAmount(i + 2.1f);
			
			options.add(option);
		}
		
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
			return option.getQuantity() + option.getUnit() + " - "
					+ option.getAmount() + option.getMoneyUnit();
		}
	}

}
