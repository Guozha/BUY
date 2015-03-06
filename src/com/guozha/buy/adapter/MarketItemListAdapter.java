package com.guozha.buy.adapter;

import java.util.List;

import com.guozha.buy.R;
import com.guozha.buy.entry.VegetableInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 逛菜场主界面条目列表
 * @author PeggyTong
 *
 */
public class MarketItemListAdapter extends BaseAdapter{
	
	private List<VegetableInfo[]> mItems;
	private List<String> titles;
	
	private LayoutInflater mInflater;
	
	public MarketItemListAdapter(Context context, List<String> titles, List<VegetableInfo[]> items){
		mInflater = LayoutInflater.from(context);
		this.mItems = items;
		this.titles = titles;
	}

	@Override
	public int getCount() {
		//if(titles == null || mItems == null) return 0;
		//if(titles.size() != mItems.size()) return 0;
		//return titles.size();
		
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_cell_market, null);
			holder = new ViewHolder();
			//TODO
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}
	
	
	static class ViewHolder{
	
	}

}
