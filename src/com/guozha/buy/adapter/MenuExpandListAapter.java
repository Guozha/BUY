package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.GoodsSecondItemType;
import com.guozha.buy.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

/**
 * 可展开的菜单列表适配器
 * @author PeggyTong
 *
 */
public class MenuExpandListAapter extends AnimatedExpandableListAdapter{
	
	private List<GoodsItemType> mGoodsItemType;
	private LayoutInflater mInflate;
	
	public MenuExpandListAapter(Context context, List<GoodsItemType> goodsItemType){
		if(context == null) return;
		mGoodsItemType = goodsItemType;
		mInflate = LayoutInflater.from(context);
	}

	@Override
	public int getGroupCount() {
		if(mGoodsItemType == null || mGoodsItemType.isEmpty()) return 0;
		return mGoodsItemType.size();
	}
	
	@Override
	public int getRealChildrenCount(int groupPosition) {
		if(getGroupCount() == 0) return 0;
		GoodsItemType goodsItemType = mGoodsItemType.get(groupPosition);
		if(goodsItemType == null) return 0;
		List<GoodsSecondItemType> goodsSecondItemTypes = goodsItemType.getFrontTypeList();
		if(goodsSecondItemTypes == null || goodsSecondItemTypes.isEmpty()) return 0;
		return goodsSecondItemTypes.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGoodsItemType.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		GoodsItemType goodsItemType = mGoodsItemType.get(groupPosition);
		if(goodsItemType == null) return null;
		return goodsItemType.getFrontTypeList().get(childPosition);
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
		GroupViewHolder holder;
		if(convertView == null){
			convertView = mInflate.inflate(R.layout.menu_list_group_cell, null);
			holder = new GroupViewHolder();
			holder.menuText = (TextView) 
					convertView.findViewById(R.id.menu_list_group_cell_text);
			holder.arrowIcon = (ImageView)
					convertView.findViewById(R.id.menu_list_group_cell_arrow);
			convertView.setTag(holder);
		}else{
			holder = (GroupViewHolder) convertView.getTag();
		}
		
		GoodsItemType itemType = mGoodsItemType.get(groupPosition);
		holder.menuText.setText(itemType.getTypeName());
		
		if(isExpanded){ //展开了
			//holder.arrowIcon.setImageResource(R.drawable.main_menu_up);
		}else{
			//holder.arrowIcon.setImageResource(R.drawable.main_menu_down);
		}
		
		return convertView;
	}
	

	@Override
	public View getRealChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		if(convertView == null){
			convertView = mInflate.inflate(R.layout.menu_list_child_cell, null);
			holder = new ChildViewHolder();
			holder.menuText = (TextView)
					convertView.findViewById(R.id.menu_list_child_cell_text);
			convertView.setTag(holder);
		}else{
			holder = (ChildViewHolder) convertView.getTag();
		}
		
		GoodsSecondItemType goodsSecondItemType = 
				mGoodsItemType.get(groupPosition).getFrontTypeList().get(childPosition);
		holder.menuText.setTag(goodsSecondItemType.getFrontTypeId() + ":" + goodsSecondItemType.getShortName());
		holder.menuText.setText(goodsSecondItemType.getTypeName());
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	static class GroupViewHolder{
		private TextView menuText;
		private ImageView arrowIcon;
	}
	
	static class ChildViewHolder{
		private TextView menuText;
	}
	
}
