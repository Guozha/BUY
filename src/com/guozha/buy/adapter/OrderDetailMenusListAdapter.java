package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mine.order.ExpandListData;
import com.guozha.buy.entry.mine.order.OrderDetailGoods;
import com.guozha.buy.entry.mine.order.OrderDetailMenus;

public class OrderDetailMenusListAdapter extends BaseExpandableListAdapter{
	
	private LayoutInflater mInflater;
	private List<ExpandListData> mExpandListDatas;
	
	public OrderDetailMenusListAdapter(Context context, List<ExpandListData> expandListData){
		mInflater = LayoutInflater.from(context);
		mExpandListDatas = expandListData;
	}

	@Override
	public int getGroupCount() {
		if(mExpandListDatas == null) return 0 ;
		return mExpandListDatas.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(mExpandListDatas == null) return 0;
		ExpandListData expandListData = mExpandListDatas.get(groupPosition);
		if(expandListData == null) return 0;
		List<OrderDetailGoods> orderDetailMenus = expandListData.getMenuslist();
		if(orderDetailMenus == null) return 0;
		return orderDetailMenus.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mExpandListDatas.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return  mExpandListDatas.get(groupPosition).getMenuslist().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_order_detail_goods_cell, null);
		}
		TextView groupTitle = (TextView) convertView.findViewById(R.id.group_title);
		TextView groupAmount = (TextView) convertView.findViewById(R.id.group_amount);
		TextView groupPrice = (TextView) convertView.findViewById(R.id.group_price);
		ExpandListData expandListData = mExpandListDatas.get(groupPosition);
		groupTitle.setText(expandListData.getName());
		//groupAmount.setText(expandListData.getAmount());
		//groupPrice.
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_order_detail_menu_cell, null);
		}
		TextView materailTitle = (TextView) convertView.findViewById(R.id.cart_list_cell_material_title);
		OrderDetailGoods orderDetailMenus = 
				mExpandListDatas.get(groupPosition).getMenuslist().get(childPosition);
		materailTitle.setText(orderDetailMenus.getGoodsName());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
