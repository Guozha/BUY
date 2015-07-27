package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;

/**
 * 设置菜单列表适配器
 * @author PeggyTong
 *
 */
public class SettingListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<String> mSettingItems;
	
	public SettingListAdapter(Context context, List<String> settingItems){
		mInflater = LayoutInflater.from(context);
		mSettingItems = settingItems;
	}

	@Override
	public int getCount() {
		if(mSettingItems == null) return 0;
		return mSettingItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mSettingItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_setting_cell, null);
		}
		TextView text = (TextView) convertView.findViewById(R.id.setting_item_text);
		text.setText(mSettingItems.get(position));
		return convertView;
	}

}
