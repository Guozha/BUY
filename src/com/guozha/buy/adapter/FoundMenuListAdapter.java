package com.guozha.buy.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.found.MenuItemListActivity;

/**
 * 发现-菜谱 适配器
 * @author PeggyTong
 *
 */
public class FoundMenuListAdapter extends BaseAdapter implements OnItemClickListener{
	
	private LayoutInflater mInflater;
	private Context mContext;
	private List<String> tempData;
	
	public FoundMenuListAdapter(Context context){
		mInflater = LayoutInflater.from(context);
		mContext = context;
		tempData = new ArrayList<String>();
		tempData.add("subject1");
		tempData.add("subject1");
		tempData.add("subject1");
		tempData.add("subject1");
		tempData.add("subject1");
		tempData.add("subject1");
		tempData.add("subject1");
		tempData.add("subject1");
		tempData.add("subject1");
		tempData.add("subject1");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
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
		holder.title.setText("-当季热门-");
		holder.gridTags.setAdapter(new ArrayAdapter<String>(
				mContext, R.layout.list_found_menu_item_grid_item, tempData));
		return convertView;
	}
	
	static class ViewHolder{
		private TextView title;
		private GridView gridTags;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(mContext, MenuItemListActivity.class);
		mContext.startActivity(intent);
	}

}
