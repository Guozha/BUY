package com.guozha.buy.activity.cart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.entry.cart.PointTime;
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
	
	private List<PointTime> options;

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
		
		mWheelView.setViewAdapter(new TimeOptionAdapter(this, options));
		
		findViewById(R.id.plance_order_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PlanceOrderActivity.this, PayActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void addData(){
		options = new ArrayList<PointTime>();
		options.add(new PointTime("09:00", "10:00", "", "今天"));
		options.add(new PointTime("10:00", "11:00", "", "今天"));
		options.add(new PointTime("11:00", "12:00", "", "今天"));
		options.add(new PointTime("12:00", "13:00", "", "今天"));
		options.add(new PointTime("13:00", "14:00", "", "今天"));
		options.add(new PointTime("14:00", "15:00", "", "今天"));
		options.add(new PointTime("15:00", "16:00", "", "今天"));
		options.add(new PointTime("16:00", "17:00", "", "今天"));
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
			return option.getTimeTitle() + "    " 
					+ option.getDownTime() + "-" + option.getUpTime();
		}
	}
}
