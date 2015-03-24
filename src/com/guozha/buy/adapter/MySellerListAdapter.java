package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mine.seller.Seller;

/**
 * 我的卖家列表适配器
 * @author PeggyTong
 *
 */
public class MySellerListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<Seller> mSellers;
	
	public MySellerListAdapter(Context context, List<Seller> sellers){
		mInflater = LayoutInflater.from(context);
		mSellers = sellers;
	}

	@Override
	public int getCount() {
		if(mSellers == null) return 0;
		return mSellers.size();
	}

	@Override
	public Object getItem(int position) {
		return mSellers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_my_seller_item_cell, null);
			holder = new ViewHolder();
			holder.sellerDescript = (TextView) convertView.findViewById(R.id.seller_descript);
			holder.sellerVisitCount = (TextView) convertView.findViewById(R.id.seller_visit_count);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		Seller seller = mSellers.get(position);
		holder.sellerDescript.setText(seller.getSellerName() + " " + seller.getMainBusi());
		holder.sellerVisitCount.setText("已有" + seller.getTransCount() + "人光顾");
		return convertView;
	}
	
	static class ViewHolder{
		private TextView sellerDescript;
		private TextView sellerVisitCount;
	}

}
