package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
			
			holder.vegetableImage1 = (ImageView) convertView.findViewById(R.id.vegetable_cell_image1);
			holder.vegetableImage2 = (ImageView) convertView.findViewById(R.id.vegetable_cell_image2);
			holder.vegetableImage3 = (ImageView) convertView.findViewById(R.id.vegetable_cell_image3);
			
			holder.vegetableName1 = (TextView) convertView.findViewById(R.id.vegetable_cell_name1);
			holder.vegetableName2 = (TextView) convertView.findViewById(R.id.vegetable_cell_name2);
			holder.vegetableName3 = (TextView) convertView.findViewById(R.id.vegetable_cell_name3);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		VegetableInfo[] infos = mVegetables.get(position);
		
		if(infos[0] == null){
			holder.vegetableImage1.setImageResource(0);
			holder.vegetableName1.setText("");
		}else{
			holder.vegetableImage1.setImageResource(infos[0].getImageId());
			holder.vegetableName1.setText(infos[0].getVegetableName());
		}
		
		if(infos[1] == null){
			holder.vegetableImage2.setImageResource(0);
			holder.vegetableName2.setText("");
		}else{
			holder.vegetableImage2.setImageResource(infos[1].getImageId());
			holder.vegetableName2.setText(infos[1].getVegetableName());
		}
		
		if(infos[2] == null){
			holder.vegetableImage3.setImageResource(0);
			holder.vegetableName3.setText("");
		}else{
			holder.vegetableImage3.setImageResource(infos[2].getImageId());
			holder.vegetableName3.setText(infos[2].getVegetableName());
		}
		
		return convertView;
	}

	
	static class ViewHolder{	
		private ImageView vegetableImage1; 
		private ImageView vegetableImage2;
		private ImageView vegetableImage3;
		
		private TextView vegetableName1;
		private TextView vegetableName2;
		private TextView vegetableName3;
	}
}
