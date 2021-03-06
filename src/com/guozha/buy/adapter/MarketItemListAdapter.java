package com.guozha.buy.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.market.ListVegetableActivity;
import com.guozha.buy.dialog.WeightSelectDialog;
import com.guozha.buy.entry.market.ItemSaleInfo;
import com.guozha.buy.entry.market.MarketHomeItem;
import com.guozha.buy.fragment.MainTabFragmentMarket;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 逛菜场主界面条目列表
 * @author PeggyTong
 *
 */
public class MarketItemListAdapter extends BaseAdapter implements OnClickListener{
	
	private List<MarketHomeItem> mMarketHomeItems;
	private Activity mContext;
	
	private LayoutInflater mInflater;
	private BitmapCache mBitmapCache;
	
	public MarketItemListAdapter(Activity context, List<MarketHomeItem> marketHomeItems, BitmapCache bitmapCache){
		if(context == null) return;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mMarketHomeItems = marketHomeItems;
		mBitmapCache = bitmapCache;
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
		    holder.cells = new ArrayList<MarketItemListAdapter.HolderEntry>();
		    for(int i = 0; i < cells.size(); i++){
		    	View itemVegetable = cells.get(i);
		    	itemVegetable.setOnClickListener(this);
		    	holderEntry = new HolderEntry();
		    	holderEntry.itemVegetable = itemVegetable;
		    	//holderEntry.buyedIcon = itemVegetable.findViewById(R.id.vegetable_cell_icon);
		    	holderEntry.image = (ImageView) itemVegetable.findViewById(R.id.vegetable_cell_image);
		    	holderEntry.productName = (TextView) itemVegetable.findViewById(R.id.vegetable_cell_name);
		    	holderEntry.price = (TextView) itemVegetable.findViewById(R.id.vegetable_cell_price);
		    	holder.cells.add(holderEntry);
		    }
		    holder.typeName = (TextView) convertView.findViewById(R.id.item_type_name);
		    holder.itemMore = (ImageView) convertView.findViewById(R.id.item_more);
		    holder.itemMore.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					//TODO 暂时这样写着，为了测试接口
					String tag = String.valueOf(view.getTag());
					String[] itemType = tag.split(":");
					Intent intent = new Intent(mContext, ListVegetableActivity.class);
					//将商品类别传给列表
					if(itemType.length == 2){
						intent.putExtra("frontTypeId", itemType[0]);
						intent.putExtra("frontTypeName", itemType[1]);
					}
					mContext.startActivity(intent);
				}
			});
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		MarketHomeItem marketHomeItem = mMarketHomeItems.get(position);
		holder.typeName.setText(marketHomeItem.getTypeName());
		holder.itemMore.setTag(marketHomeItem.getFrontTypeId() + ":" + marketHomeItem.getTypeName());
		List<ItemSaleInfo> itemSaleInfos = marketHomeItem.getGoodsList();
		for(int i = 0; i < itemSaleInfos.size(); i++){
			HolderEntry holderEntry = holder.cells.get(i);
			ItemSaleInfo itemSaleInfo = itemSaleInfos.get(i);
			holderEntry.productName.setVisibility(View.VISIBLE);
			holderEntry.price.setVisibility(View.VISIBLE);
			
			//设置TAG
			holderEntry.itemVegetable.setTag(itemSaleInfo.getGoodsId() 
					+ ":" + itemSaleInfo.getUnitPrice() + ":" + itemSaleInfo.getUnit());
			//holderEntry.image.setImageBitmap(BitmapFactory.decodeByteArray(data, offset, length));
			holderEntry.productName.setText(itemSaleInfo.getGoodsName());
			holderEntry.price.setText(
					UnitConvertUtil.getSwitchedMoney(itemSaleInfo.getUnitPrice()) + "元/" +
					UnitConvertUtil.getSwichedUnit(1000, itemSaleInfo.getUnit()));
			String imgUrl = itemSaleInfo.getGoodsImg();
			holderEntry.image.setImageResource(R.drawable.default_icon);
			mBitmapCache.loadBitmaps(holderEntry.image, imgUrl);
		}
		
		for(int i = itemSaleInfos.size(); i < holder.cells.size(); i++){
			HolderEntry holderEntry = holder.cells.get(i);
			holderEntry.itemVegetable.setTag("-1");
			holderEntry.productName.setVisibility(View.INVISIBLE);
			holderEntry.price.setVisibility(View.INVISIBLE);
			holderEntry.image.setImageResource(R.drawable.default_icon);
		}
		
		return convertView;
	}
	
	@Override
	public void onClick(View view) {
		Intent intent;
		//先判断用户是否登录了
		if(ConfigManager.getInstance().getUserToken(mContext) == null){
			return;
		}
		//TODO 再判断当前选择的地址是否为NULL
		if(ConfigManager.getInstance().getChoosedAddressId(mContext) == -1) return;
		
		String tag = String.valueOf(view.getTag());
		String[] tags = tag.split(":");
		if(tags.length != 3){
			return;
		}
		if("-1".equals(tags[0])){
			return;
		}
		intent = new Intent(mContext, WeightSelectDialog.class);
		intent.putExtra("goodsId", tags[0]);
		intent.putExtra("unitPrice", tags[1]);
		intent.putExtra("unit", tags[2]);
		mContext.startActivityForResult(intent, MainTabFragmentMarket.REQUEST_CODE_CART);
	}
	
	
	static class ViewHolder{
		private TextView typeName;
		private ImageView itemMore;
		private List<HolderEntry> cells;
	}
	
	static class HolderEntry{
		private View itemVegetable;
		//private View buyedIcon;
		private ImageView image;
		private TextView productName;
		private TextView price;
	}

}
