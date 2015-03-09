package com.guozha.buy.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;

/**
 * 购物车列表适配器
 * @author PeggyTong
 *
 */
public class CartItemListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private int mTitleIndex;
	private Resources mResource;
	
	public CartItemListAdapter(Context context, int titleIndex){
		mResource = context.getResources();
		mInflater = LayoutInflater.from(context);
		mTitleIndex = titleIndex;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 20;
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
			convertView = mInflater.inflate(R.layout.list_item_cell_cart_list, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.cart_list_cell_title);
			holder.minus = (ImageView) convertView.findViewById(R.id.cart_list_cell_minus);
			holder.num = (TextView) convertView.findViewById(R.id.cart_list_cell_num);
			holder.plus = (ImageView) convertView.findViewById(R.id.cart_list_cell_plus);
			holder.price = (TextView) convertView.findViewById(R.id.cart_list_cell_price);
			holder.close = (ImageView) convertView.findViewById(R.id.cart_list_cell_close);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position == 0 || position == mTitleIndex){
			holder.minus.setVisibility(View.INVISIBLE);
			holder.num.setVisibility(View.INVISIBLE);
			holder.plus.setVisibility(View.INVISIBLE);
			holder.price.setVisibility(View.INVISIBLE);
			holder.close.setVisibility(View.INVISIBLE);
			holder.title.setTextColor(mResource.getColor(R.color.color_app_base_1));
			if(position == 0){
				holder.title.setText("菜谱");
				
			}else{
				holder.title.setText("食材");
			}
		}else{
			holder.minus.setVisibility(View.VISIBLE);
			holder.num.setVisibility(View.VISIBLE);
			holder.plus.setVisibility(View.VISIBLE);
			holder.price.setVisibility(View.VISIBLE);
			holder.close.setVisibility(View.VISIBLE);
			holder.title.setTextColor(mResource.getColor(R.color.color_app_base_4));
			holder.title.setText("小炒肉");
		}
		
		return convertView;
	}

	static class ViewHolder{
		private TextView title;
		private ImageView minus;
		private TextView num;
		private ImageView plus;
		private TextView price;
		private ImageView close;
	}

}
