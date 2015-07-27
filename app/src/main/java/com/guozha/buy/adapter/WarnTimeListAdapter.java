package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.global.WarnTime;

/**
 * 提醒设置时间列表适配器
 * @author PeggyTong
 *
 */
public class WarnTimeListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	
	private List<WarnTime> mWarnTimes;
	
	private String mChoosedTime;
	
	public WarnTimeListAdapter(Context context, String choosedTime, List<WarnTime> warnTimes){
		mInflater = LayoutInflater.from(context);
		mChoosedTime = choosedTime;
		mWarnTimes = warnTimes;
	}

	@Override
	public int getCount() {
		if(mWarnTimes == null) return 0;
		return mWarnTimes.size();
	}

	@Override
	public Object getItem(int position) {
		return mWarnTimes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_warntime_item_cell, null);
		}
		
		TextView warnTimeText = (TextView) convertView.findViewById(R.id.warn_time_text);
		ImageView choosedIcon = (ImageView) convertView.findViewById(R.id.choosed_icon);
		WarnTime warnTime = mWarnTimes.get(position);
		warnTimeText.setText(warnTime.getShowTime());
		if(warnTime.getShowTime().equals(mChoosedTime)){
			choosedIcon.setImageResource(R.drawable.button_tick);
		}else{
			choosedIcon.setImageResource(0);
		}
		return convertView;
	}
}
