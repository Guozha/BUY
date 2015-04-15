package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mpage.plan.MenuGoods;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 菜谱详情的食材适配器
 * @author PeggyTong
 *
 */
public class DetailMaterialListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<MenuGoods> mMenuGoods;
	
	public DetailMaterialListAdapter(Context context, List<MenuGoods> menuGoods){
		mInflater = LayoutInflater.from(context);
		mMenuGoods = menuGoods;
	}

	@Override
	public int getCount() {
		if(mMenuGoods == null) return 0;
		return mMenuGoods.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuGoods.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.detail_material_list_item_cell, null);
		}
		TextView menuGoodsName = (TextView) convertView.findViewById(R.id.menu_goods_name);
		TextView menuGoodsWeight = (TextView) convertView.findViewById(R.id.menu_goods_weight);
		MenuGoods menuGoods = mMenuGoods.get(position);
		menuGoodsName.setText(menuGoods.getGoodsName());
		menuGoodsWeight.setText(UnitConvertUtil.getSwitchedWeight(menuGoods.getAmount(), menuGoods.getUnit()));
		return convertView;
	}

}
