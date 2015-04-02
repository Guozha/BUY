package com.guozha.buy.adapter;

import java.util.ArrayList;
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
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.UnitConvertUtil;


/**
 * 菜品列表适配器
 * @author PeggyTong
 *
 */
public class VegetableListAdapter extends BaseAdapter{
	
	private List<ItemSaleInfo[]> mVegetables;
	private LayoutInflater mInflater;
	private BitmapCache mBitmapCache;
	
	public VegetableListAdapter(Context context, List<ItemSaleInfo[]> vegetables, View parentView){
		mInflater = LayoutInflater.from(context);
		this.mVegetables = vegetables;
		mBitmapCache = new BitmapCache(context, parentView);
	}
	
	@Override
	public int getCount() {
		if(mVegetables == null) return 0;
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
			convertView = mInflater.inflate(R.layout.list_item_cell_maintab_market, null);
			holder = new ViewHolder();
			holder.items.add(convertView.findViewById(R.id.list_item_small_cell1));
			holder.items.add(convertView.findViewById(R.id.list_item_small_cell2));
			holder.items.add(convertView.findViewById(R.id.list_item_small_cell3));
			ViewItemHolder itemHolder;
			for(int i = 0; i < holder.items.size(); i++){
				itemHolder = new ViewItemHolder();
				View view = holder.items.get(i);
				itemHolder.chooseIcon = (ImageView) view.findViewById(R.id.vegetable_cell_icon);
				itemHolder.vegetableIcon = (ImageView) view.findViewById(R.id.vegetable_cell_image);
				itemHolder.vegetableName = (TextView) view.findViewById(R.id.vegetable_cell_name);
				itemHolder.vegetablePrice = (TextView) view.findViewById(R.id.vegetable_cell_price);
				holder.itemHolder.add(itemHolder);
			}
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		ItemSaleInfo[] itemSaleInfo = mVegetables.get(position);
		LogUtil.e("itemSaleInfo.length = " + itemSaleInfo.length);
		for(int i = 0; i < itemSaleInfo.length; i++){
			ItemSaleInfo saleInfo = itemSaleInfo[i];
			if(saleInfo == null || holder.itemHolder == null) continue;
			ViewItemHolder itemHolder = holder.itemHolder.get(i);
			String imgUrl = saleInfo.getGoodsImg();
			itemHolder.vegetableIcon.setTag(imgUrl);
			mBitmapCache.loadBitmaps(itemHolder.vegetableIcon, imgUrl);
			itemHolder.vegetableName.setText(saleInfo.getGoodsName());
			itemHolder.vegetablePrice.setText(
					UnitConvertUtil.getSwitchedMoney(saleInfo.getUnitPrice()) + "/" +
					UnitConvertUtil.getSwichedUnit(1000, saleInfo.getUnit()));
		}
		
		return convertView;
	}

	
	static class ViewHolder{	
		private List<View> items = new ArrayList<View>();
		private List<ViewItemHolder> itemHolder = 
				new ArrayList<VegetableListAdapter.ViewItemHolder>();
	}
	
	static class ViewItemHolder{
		private ImageView chooseIcon;
		private ImageView vegetableIcon;
		private TextView vegetableName;
		private TextView vegetablePrice;
	}
}
