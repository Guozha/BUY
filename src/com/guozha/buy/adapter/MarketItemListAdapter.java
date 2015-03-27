package com.guozha.buy.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.dialog.WeightSelectDialog;
import com.guozha.buy.entry.market.ItemSaleInfo;
import com.guozha.buy.entry.market.MarketHomeItem;

/**
 * 逛菜场主界面条目列表
 * @author PeggyTong
 *
 */
public class MarketItemListAdapter extends BaseAdapter implements OnClickListener{
	
	private List<MarketHomeItem> mMarketHomeItems;
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	public MarketItemListAdapter(Context context, List<MarketHomeItem> marketHomeItems){
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mMarketHomeItems = marketHomeItems;
	}

	@Override
	public int getCount() {
		if(mMarketHomeItems == null) return 0;
		return mMarketHomeItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mMarketHomeItems.get(position);
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
			
			View cell1 = line1.findViewById(R.id.list_item_small_cell1);
		    View cell2 = line1.findViewById(R.id.list_item_small_cell2);
		    View cell3 = line1.findViewById(R.id.list_item_small_cell3);
		    
		    View cell4 = line2.findViewById(R.id.list_item_small_cell1);
		    View cell5 = line2.findViewById(R.id.list_item_small_cell2);
		    View cell6 = line2.findViewById(R.id.list_item_small_cell3);
		    
		    List<View> cells = new ArrayList<View>();
		    cells.add(cell1);
		    cells.add(cell2);
		    cells.add(cell3);
		    cells.add(cell4);
		    cells.add(cell5);
		    cells.add(cell6);
		 
		    HolderEntry holderEntry;
		    for(int i = 0; i < 6; i++){
		    	cells.get(i).setOnClickListener(this);
		    	holderEntry = new HolderEntry();
		    	holderEntry.buyedIcon = cells.get(i).findViewById(R.id.vegetable_cell_icon);
		    	holderEntry.image = (ImageView) cells.get(i).findViewById(R.id.vegetable_cell_image);
		    	holderEntry.productName = (TextView) cells.get(i).findViewById(R.id.vegetable_cell_name);
		    	holderEntry.price = (TextView) cells.get(i).findViewById(R.id.vegetable_cell_price);
		    	holder.cells.add(holderEntry);
		    }
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		MarketHomeItem marketHomeItem = mMarketHomeItems.get(position);
		List<ItemSaleInfo> itemSaleInfos = marketHomeItem.getGoodsList();
		for(int i = 0; i < itemSaleInfos.size(); i++){
			HolderEntry holderEntry = holder.cells.get(i);
			//holderEntry.image.setImageBitmap(BitmapFactory.decodeByteArray(data, offset, length));
			holderEntry.productName.setText(itemSaleInfos.get(i).getGoodsName());
			holderEntry.price.setText(itemSaleInfos.get(i).getUnitPrice()
					+ itemSaleInfos.get(i).getUnit());
		}
		
		return convertView;
	}
	
	@Override
	public void onClick(View view) {
		Intent intent = new Intent(mContext, WeightSelectDialog.class);
		mContext.startActivity(intent);
	}
	
	
	static class ViewHolder{
		List<HolderEntry> cells;
	}
	
	static class HolderEntry{
		private View buyedIcon;
		private ImageView image;
		private TextView productName;
		private TextView price;
	}






}
