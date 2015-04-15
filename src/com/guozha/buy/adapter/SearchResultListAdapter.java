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
import com.guozha.buy.entry.market.ItemSaleInfo;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 查找结果列表适配器
 * @author PeggyTong
 *
 */
public class SearchResultListAdapter extends BaseAdapter{
	
	private List<ItemSaleInfo> mItemSaleInfos;
	private LayoutInflater mInflater;
	private BitmapCache mBitmapCache;
	
	public SearchResultListAdapter(Context context, List<ItemSaleInfo> itemSaleInfos, BitmapCache bitmapCache){
		mInflater = LayoutInflater.from(context);
		mItemSaleInfos = itemSaleInfos;
		mBitmapCache = bitmapCache;
	}

	@Override
	public int getCount() {
		if(mItemSaleInfos == null) return 0;
		return mItemSaleInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemSaleInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_small_cell, null);
			holder = new ViewHolder();
			holder.buyedIcon = convertView.findViewById(R.id.vegetable_cell_icon);
	    	holder.image = (ImageView) convertView.findViewById(R.id.vegetable_cell_image);
	    	holder.productName = (TextView) convertView.findViewById(R.id.vegetable_cell_name);
	    	holder.price = (TextView) convertView.findViewById(R.id.vegetable_cell_price);
	    	convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		ItemSaleInfo itemSaleInfo = mItemSaleInfos.get(position);
		
		mBitmapCache.loadBitmaps(holder.image, itemSaleInfo.getGoodsImg());
		holder.productName.setText(itemSaleInfo.getGoodsName());
		holder.price.setText(
				UnitConvertUtil.getSwitchedMoney(itemSaleInfo.getUnitPrice()) + "/" +
				UnitConvertUtil.getSwichedUnit(1000, itemSaleInfo.getUnit()));
		
		return convertView;
	}
	
	static class ViewHolder{
		private View buyedIcon;
		private ImageView image;
		private TextView productName;
		private TextView price;
	}

}
