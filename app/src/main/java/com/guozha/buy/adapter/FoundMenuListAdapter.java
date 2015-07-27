package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.found.MenuItemListActivity;
import com.guozha.buy.entry.found.MenuFirstType;
import com.guozha.buy.entry.found.MenuSecondType;

/**
 * 发现-菜谱 适配器
 * @author PeggyTong
 *
 */
public class FoundMenuListAdapter extends BaseAdapter implements OnItemClickListener{
	
	private LayoutInflater mInflater;
	private Context mContext;
	private List<MenuFirstType> mMenuFirstTypes;
	
	public FoundMenuListAdapter(Context context, List<MenuFirstType> menuFirstType){
		mMenuFirstTypes = menuFirstType;
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public int getCount() {
		if(mMenuFirstTypes == null) return 0;
		return mMenuFirstTypes.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuFirstTypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_found_menu_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.found_menu_item_title);
			holder.gridTags = (GridView) convertView.findViewById(R.id.found_menu_item_tag_grid);
			holder.gridTags.setOnItemClickListener(this);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.gridTags.setTag(position);
		MenuFirstType menuFirstType = mMenuFirstTypes.get(position);
		holder.title.setText("-" + menuFirstType.getMenuTypeName() + "-");
		holder.gridTags.setAdapter(new FoundMenuSecondListAdapter(
				mContext, menuFirstType.getMenuTypeList()));
		return convertView;
	}
	
	static class ViewHolder{
		private TextView title;
		private GridView gridTags;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int firstPosition = (Integer) parent.getTag();
		MenuSecondType menuSecondType = mMenuFirstTypes.get(firstPosition).getMenuTypeList().get(position);
		Intent intent = new Intent(mContext, MenuItemListActivity.class);
		intent.putExtra("menuTypeId", menuSecondType.getMenuTypeId());
		intent.putExtra("menuTypeName", menuSecondType.getMenuTypeName());
		mContext.startActivity(intent);
	}

}
