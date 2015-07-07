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
import com.guozha.buy.entry.market.RelationRecipe;
import com.guozha.buy.global.net.BitmapCache;

public class CookBookListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<RelationRecipe> mRelationRecipe;
	//private Context mContext;
	private BitmapCache mBitmapCache;
	public CookBookListAdapter(Context context, List<RelationRecipe> relationRecipe, BitmapCache bitmapCache){
		mInflater = LayoutInflater.from(context);
		mRelationRecipe = relationRecipe;
		mBitmapCache = bitmapCache;
	}

	@Override
	public int getCount() {
		if(mRelationRecipe == null) return 0;
		return mRelationRecipe.size();
	}

	@Override
	public Object getItem(int position) {
		return mRelationRecipe.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_cook_book_list_cell, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.cook_book_imag);
			holder.name = (TextView) convertView.findViewById(R.id.cooke_book_name);
			holder.cookeWay = (TextView) convertView.findViewById(R.id.cooke_way);
			holder.cookeTime = (TextView) convertView.findViewById(R.id.cooke_time);
			holder.cookeCalorie = (TextView) convertView.findViewById(R.id.cooke_calorie);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		RelationRecipe relationRecipe = mRelationRecipe.get(position);
		holder.image.setImageResource(R.drawable.default_160_160);
		mBitmapCache.loadBitmaps(holder.image, relationRecipe.getMenuImg());
		holder.name.setText(relationRecipe.getMenuName());
		holder.cookeWay.setText(relationRecipe.getCookieWay());
		holder.cookeTime.setText(relationRecipe.getCookieTime() + "min");
		holder.cookeCalorie.setText(relationRecipe.getCalories() + "大卡/100g");

		return convertView;
	}
	
    static class ViewHolder{
    	private ImageView image;
    	private TextView name;
    	private TextView cookeWay;
    	private TextView cookeTime;
    	private TextView cookeCalorie;
    }
}
