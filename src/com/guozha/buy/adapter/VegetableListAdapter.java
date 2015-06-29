package com.guozha.buy.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.LoginActivity;
import com.guozha.buy.controller.dialog.WeightSelectDialog;
import com.guozha.buy.controller.mine.AddAddressActivity;
import com.guozha.buy.entry.market.ItemSaleInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.UnitConvertUtil;


/**
 * 菜品列表适配器
 * @author PeggyTong
 *
 */
public class VegetableListAdapter extends BaseAdapter implements OnClickListener{
	
	private List<ItemSaleInfo[]> mVegetables;
	private LayoutInflater mInflater;
	private BitmapCache mBitmapCache;
	private Context mContext;
	
	public VegetableListAdapter(Context context, List<ItemSaleInfo[]> vegetables, View parentView, BitmapCache bitmapCache){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mVegetables = vegetables;
		mBitmapCache = bitmapCache;
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
				view.setOnClickListener(this);
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
		for(int i = 0; i < itemSaleInfo.length; i++){
			ItemSaleInfo saleInfo = itemSaleInfo[i];
			if(holder.itemHolder == null) continue;
			ViewItemHolder itemHolder = holder.itemHolder.get(i);
			if(saleInfo == null){
				itemHolder.vegetableName.setText("");
				itemHolder.vegetablePrice.setText("");
				itemHolder.vegetableIcon.setImageDrawable(null);
			}else{
				holder.items.get(i).setTag(saleInfo.getGoodsId() + ":" + saleInfo.getUnitPrice() + ":" + saleInfo.getUnit());
				String imgUrl = saleInfo.getGoodsImg();
				itemHolder.vegetableIcon.setImageResource(R.drawable.default_icon);
				mBitmapCache.loadBitmaps(itemHolder.vegetableIcon, imgUrl);
				itemHolder.vegetableName.setText(saleInfo.getGoodsName());
				itemHolder.vegetablePrice.setText(
						UnitConvertUtil.getSwitchedMoney(saleInfo.getUnitPrice()) + "元/" +
						UnitConvertUtil.getSwichedUnit(1000, saleInfo.getUnit()));
			}
		}
		
		return convertView;
	}
	
	@Override
	public void onClick(View view) {
		Intent intent;
		//先判断用户是否登录了
		if(ConfigManager.getInstance().getUserToken(mContext) == null)
			return;
		//TODO 再判断当前选择的地址是否为NULL
		if(ConfigManager.getInstance().getChoosedAddressId(mContext) == -1) 
			return;
		String tag= String.valueOf(view.getTag());
		String[] tags = tag.split(":");
		if(tags.length != 3) return;
		intent = new Intent(mContext, WeightSelectDialog.class);
		intent.putExtra("goodsId", tags[0]);
		intent.putExtra("unitPrice", tags[1]);
		intent.putExtra("unit", tags[2]);
		mContext.startActivity(intent);
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
