package com.guozha.buy.adapter;

import java.util.List;

import com.guozha.buy.R;
import com.guozha.buy.dialog.WeightSelectDialog;
import com.guozha.buy.entry.VegetableInfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 逛菜场主界面条目列表
 * @author PeggyTong
 *
 */
public class MarketItemListAdapter extends BaseAdapter implements OnClickListener{
	
	private List<VegetableInfo[]> mItems;
	private List<String> titles;
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	public MarketItemListAdapter(Context context, List<String> titles, List<VegetableInfo[]> items){
		mInflater = LayoutInflater.from(context);
		mContext = context;
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
			View line1 = convertView.findViewById(R.id.list_item_cell_line1);
			View line2 = convertView.findViewById(R.id.list_item_cell_line2);
			
			holder.cell1 = line1.findViewById(R.id.list_item_small_cell1);
		    holder.cell2 = line1.findViewById(R.id.list_item_small_cell2);
		    holder.cell3 = line1.findViewById(R.id.list_item_small_cell3);
		    
		    holder.cell4 = line2.findViewById(R.id.list_item_small_cell1);
		    holder.cell5 = line2.findViewById(R.id.list_item_small_cell2);
		    holder.cell6 = line2.findViewById(R.id.list_item_small_cell3);
		    
		    holder.cell1.setOnClickListener(this);
		    holder.cell2.setOnClickListener(this);
		    holder.cell3.setOnClickListener(this);
		    holder.cell4.setOnClickListener(this);
		    holder.cell5.setOnClickListener(this);
		    holder.cell6.setOnClickListener(this);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}
	
	@Override
	public void onClick(View view) {
		Intent intent = new Intent(mContext, WeightSelectDialog.class);
		mContext.startActivity(intent);
	}
	
	
	static class ViewHolder{
		View cell1;
		View cell2;
		View cell3;
		View cell4;
		View cell5;
		View cell6;
	}






}
