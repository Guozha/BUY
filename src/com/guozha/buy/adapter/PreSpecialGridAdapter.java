package com.guozha.buy.adapter;

import com.guozha.buy.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 特供预售网格列表适配器
 * @author PeggyTong
 *
 */
public class PreSpecialGridAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	
	public PreSpecialGridAdapter(Context context){
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 8;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.grid_item_pre_special_cell, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	static class ViewHolder{
		
	}

}
