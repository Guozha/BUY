package com.guozha.buy.adapter;

import com.guozha.buy.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
		return 0;
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
		return convertView;
	}

}
