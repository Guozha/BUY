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
import com.guozha.buy.entry.mpage.season.SeasonAdviceItem;

public class SeasonItemListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<SeasonAdviceItem> mAdviceItem;
	
	public SeasonItemListAdapter(Context context, List<SeasonAdviceItem> adviceItem){
		mInflater = LayoutInflater.from(context);
		mAdviceItem = adviceItem;
	}

	@Override
	public int getCount() {
		if(mAdviceItem == null) return 0;
		return mAdviceItem.size();
	}

	@Override
	public Object getItem(int position) {
		return mAdviceItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_season_item_cell, null);
			holder = new ViewHolder();
			holder.mItemIcon = (ImageView) convertView.findViewById(R.id.season_list_item_icon);
			holder.mItemDescrip = (TextView) convertView.findViewById(R.id.season_list_item_description);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mItemDescrip.setText(mAdviceItem.get(position).getMemo());
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView mItemIcon;
		private TextView mItemDescrip;
	}

}
