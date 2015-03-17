package com.guozha.buy.adapter;

import com.guozha.buy.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 菜谱详情的调料列表适配器
 * @author PeggyTong
 *
 */
public class DetailSeasonListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	
	public DetailSeasonListAdapter(Context context){
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 5;
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
			convertView = mInflater.inflate(R.layout.detail_season_list_item_cell, null);
		}else{
			
		}
		return convertView;
	}

}
