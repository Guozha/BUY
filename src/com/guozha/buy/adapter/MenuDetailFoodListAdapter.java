package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.found.menu.MenuGoods;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 菜谱详情-食材  适配器
 * @author PeggyTong
 *
 */
public class MenuDetailFoodListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<MenuGoods> mMenuGoodss;
	public MenuDetailFoodListAdapter(Context context, List<MenuGoods> menuGoods){
		mInflater = LayoutInflater.from(context);
		mMenuGoodss = menuGoods;
	}

	@Override
	public int getCount() {
		if(mMenuGoodss == null) return 0;
		return mMenuGoodss.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuGoodss.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_menu_detail_food_item, null);
		}
		TextView name = (TextView) convertView.findViewById(R.id.menu_detail_list_item_name);
		TextView weight = (TextView) convertView.findViewById(R.id.menu_detail_list_item_weight);
		CheckBox check = (CheckBox) convertView.findViewById(R.id.menu_detail_list_item_check);
		MenuGoods menuGoods = mMenuGoodss.get(position);
		name.setText(menuGoods.getGoodsName());
		weight.setText(UnitConvertUtil.getSwitchedWeight(menuGoods.getAmount(), menuGoods.getUnit()));
		return convertView;
	}
}
