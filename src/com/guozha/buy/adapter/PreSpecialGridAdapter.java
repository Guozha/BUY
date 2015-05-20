package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mpage.prespecial.PreSpecialItem;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 特供预售网格列表适配器
 * @author PeggyTong
 *
 */
public class PreSpecialGridAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<PreSpecialItem> mPreSpecialItems;
	private BitmapCache mBitmapCache;
	private Context mContext;
	
	public PreSpecialGridAdapter(Context context, List<PreSpecialItem> preSpecialItems, BitmapCache bitmapCache){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mPreSpecialItems = preSpecialItems;
		mBitmapCache = bitmapCache;
	}

	@Override
	public int getCount() {
		if(mPreSpecialItems == null) return 0;
		return mPreSpecialItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mPreSpecialItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.grid_item_pre_special_cell, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.prespecial_image);
			holder.name = (TextView) convertView.findViewById(R.id.prespecial_name);
			holder.price = (TextView) convertView.findViewById(R.id.prespecial_price);
			holder.sellNum = (TextView) convertView.findViewById(R.id.prespecial_sell_num);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		PreSpecialItem preSpecialItem = mPreSpecialItems.get(position);
		mBitmapCache.loadBitmaps(holder.image, preSpecialItem.getGoodsImg());
		holder.name.setText(preSpecialItem.getGoodsName());
		holder.price.setText(UnitConvertUtil.getSwitchedMoney(preSpecialItem.getUnitPrice()) 
				+ "元/" + UnitConvertUtil.getSwichedUnit(1000, preSpecialItem.getUnit()));
		int iconId = -1;
		if("02".equals(preSpecialItem.getGoodsProp())){
			iconId = R.drawable.sale_tag_01;
		}else if("03".equals(preSpecialItem.getGoodsProp())){
			iconId = R.drawable.sale_tag_02;
		}
		if(iconId != -1){
			Drawable drawable = mContext.getResources().getDrawable(iconId);
			holder.name.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		}else{
			holder.name.setCompoundDrawables(null, null, null, null);
		}
		holder.sellNum.setText(preSpecialItem.getBuyTimes() + "人已购买");
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView image;
		private TextView name;
		private TextView price;
		private TextView sellNum;
	}

}
