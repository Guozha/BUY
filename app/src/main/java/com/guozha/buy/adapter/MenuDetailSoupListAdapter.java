package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.found.menu.MenuSeason;

/**
 * 菜谱详情 - 辅料 适配器
 * @author PeggyTong
 *
 */
public class MenuDetailSoupListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<MenuSeason> mMenuSeasons;
	
	public MenuDetailSoupListAdapter(Context context, List<MenuSeason> menuSeasons){
		mInflater = LayoutInflater.from(context);
		mMenuSeasons = menuSeasons;
	}

	@Override
	public int getCount() {
		if(mMenuSeasons == null) return 0;
		return mMenuSeasons.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuSeasons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_menu_detail_soup_item, null);
		}
		TextView name = (TextView) convertView.findViewById(R.id.menu_detail_soup_item_name);
		TextView weight = (TextView) convertView.findViewById(R.id.menu_detail_soup_item_weight);
		MenuSeason menuSeason = mMenuSeasons.get(position);
		name.setText(menuSeason.getSeasoningsName());
		weight.setText(menuSeason.getSeasoningsAmount());
		return convertView;
	}

}
