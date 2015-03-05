package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.guozha.buy.R;

/**
 * 可展开的菜单列表适配器
 * @author PeggyTong
 *
 */
public class MenuExpandListAapter extends BaseExpandableListAdapter{
	
	private String[] mGroupMenus;
	
	private List<String>[] mChildMenus;
	
	private LayoutInflater mInflate;
	
	public MenuExpandListAapter(Context context, 
			String[] groupMenus, List<String>[] childMenus){
		this.mGroupMenus = groupMenus;
		this.mChildMenus = childMenus;
		mInflate = LayoutInflater.from(context);
	}

	@Override
	public int getGroupCount() {
		if(mGroupMenus.length != mChildMenus.length) return 0;
		return mGroupMenus.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(mGroupMenus.length != mChildMenus.length) return 0;
		return mChildMenus[groupPosition].size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroupMenus[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mChildMenus[groupPosition].get(childPosition);
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
		return false;
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
			convertView.setTag(holder);
		}
		
		holder = (GroupViewHolder) convertView.getTag();
		
		holder.menuText.setText(mGroupMenus[groupPosition]);
		
		if(isExpanded){
			
		}else{
			
		}
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		if(convertView == null){
			convertView = mInflate.inflate(R.layout.menu_list_child_cell, null);
			holder = new ChildViewHolder();
			holder.menuText = (TextView)
					convertView.findViewById(R.id.menu_list_child_cell_text);
			convertView.setTag(holder);
		}
		
		holder = (ChildViewHolder) convertView.getTag();
		
		holder.menuText.setText(mChildMenus[groupPosition].get(childPosition));
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	
	static class GroupViewHolder{
		private TextView menuText;
	}
	
	static class ChildViewHolder{
		private TextView menuText;
	}
	
}
