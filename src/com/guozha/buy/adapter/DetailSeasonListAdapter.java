package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mpage.plan.Seasoning;

/**
 * 菜谱详情的调料列表适配器
 * @author PeggyTong
 *
 */
public class DetailSeasonListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<Seasoning> mSeasoning;
	
	public DetailSeasonListAdapter(Context context, List<Seasoning> seasoning){
		mInflater = LayoutInflater.from(context);
		mSeasoning = seasoning;
	}

	@Override
	public int getCount() {
		if(mSeasoning == null) return 0;
		return mSeasoning.size();
	}

	@Override
	public Object getItem(int position) {
		return mSeasoning.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.detail_season_list_item_cell, null);
		}
		TextView seasonName = (TextView) convertView.findViewById(R.id.season_name);
		TextView seasonAmount = (TextView) convertView.findViewById(R.id.season_amount);
		Seasoning season = mSeasoning.get(position);
		seasonName.setText(season.getSeasoningsName());
		seasonAmount.setText(season.getSeasoningsAmount());
		return convertView;
	}

}
