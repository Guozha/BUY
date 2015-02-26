package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.VegetableInfo;

public class VegetableListAdapter extends BaseAdapter{
	
	private List<VegetableInfo> mVegetables;
	private LayoutInflater mInflater;
	
	public VegetableListAdapter(Context context, List<VegetableInfo> vegetables){
		mInflater = LayoutInflater.from(context);
		this.mVegetables = vegetables;
	}

	@Override
	public int getCount() {
		return mVegetables.size();
	}

	@Override
	public Object getItem(int position) {
		return mVegetables.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_cell_maintab_market, null);
			holder.mTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.mContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		VegetableInfo info = mVegetables.get(position);
		
		holder.mTitle.setText(info.getName());
		holder.mContent.setText(info.getDescription());
		
		return convertView;
	}

	
	static class ViewHolder{
		private TextView mTitle;
		private TextView mContent;
	}
}
