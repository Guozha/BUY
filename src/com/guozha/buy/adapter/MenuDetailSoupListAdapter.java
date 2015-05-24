package com.guozha.buy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;

/**
 * 菜谱详情 - 辅料 适配器
 * @author PeggyTong
 *
 */
public class MenuDetailSoupListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	
	public MenuDetailSoupListAdapter(Context context){
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_menu_detail_soup_item, null);
		}
		TextView name = (TextView) convertView.findViewById(R.id.menu_detail_soup_item_name);
		TextView weight = (TextView) convertView.findViewById(R.id.menu_detail_soup_item_weight);
		name.setText("葱");
		weight.setText("100g");
		return convertView;
	}

}
