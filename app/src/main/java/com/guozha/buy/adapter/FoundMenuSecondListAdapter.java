package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.found.MenuSecondType;

public class FoundMenuSecondListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<MenuSecondType> mMenuSecondTypes;
	public FoundMenuSecondListAdapter(Context context, List<MenuSecondType> menuSecondTypes){
		mInflater = LayoutInflater.from(context);
		mMenuSecondTypes = menuSecondTypes;
	}

	@Override
	public int getCount() {
		if(mMenuSecondTypes == null) return 0;
		return mMenuSecondTypes.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuSecondTypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_found_menu_item_grid_item, null);
			holder = new ViewHolder();
			holder.textview = (TextView) convertView.findViewById(R.id.menu_item_text);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		MenuSecondType secondType = mMenuSecondTypes.get(position);
		holder.textview.setText(secondType.getMenuTypeName());
		return convertView;
	}
	
	static class ViewHolder{
		TextView textview;
	}
}
