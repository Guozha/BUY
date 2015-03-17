package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.guozha.buy.R;
import com.guozha.buy.entry.VegetableInfo;


/**
 * 菜品列表适配器
 * @author PeggyTong
 *
 */
public class VegetableListAdapter extends BaseAdapter{
	
	private List<VegetableInfo[]> mVegetables;
	private LayoutInflater mInflater;
	
	public VegetableListAdapter(Context context, List<VegetableInfo[]> vegetables){
		mInflater = LayoutInflater.from(context);
		this.mVegetables = vegetables;
	}
	
	@Override
	public int getCount() {
		return mVegetables.size();
	}

	@Override
	public Object getItem(int position) {
		return mVegetables.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_cell_maintab_market, null);
			
			//holder.vegetableImage1 = (ImageView) convertView.findViewById(R.id.vegetable_cell_image1);
			//holder.vegetableImage2 = (ImageView) convertView.findViewById(R.id.vegetable_cell_image2);
			//holder.vegetableImage3 = (ImageView) convertView.findViewById(R.id.vegetable_cell_image3);
			
			//holder.vegetableName1 = (TextView) convertView.findViewById(R.id.vegetable_cell_name1);
			//holder.vegetableName2 = (TextView) convertView.findViewById(R.id.vegetable_cell_name2);
			//holder.vegetableName3 = (TextView) convertView.findViewById(R.id.vegetable_cell_name3);
			//convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//VegetableInfo[] infos = mVegetables.get(position);
		
		
		return convertView;
	}

	
	static class ViewHolder{	
		
	}
}
