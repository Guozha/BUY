package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.dialog.CustomDialog;
import com.guozha.buy.entry.mine.collection.GoodsListItem;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 收集菜品列表适配器
 * @author PeggyTong
 *
 */
public class CollectionVegetableListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	
	private DeleteClickListener mDeletedClickListener;
	private List<GoodsListItem> mGoodsListItems;
	private Context mContext;
	
	public CollectionVegetableListAdapter(Context context, List<GoodsListItem> goodsListItems){
		mContext = context;
		mDeletedClickListener = new DeleteClickListener();
		mGoodsListItems = goodsListItems;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(mGoodsListItems == null) return 0;
		return mGoodsListItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mGoodsListItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_fragment_collection_vegetable_item_cell, null);
			holder = new ViewHolder();
			holder.goodsName = (TextView) convertView.findViewById(R.id.goods_name);
			holder.goodsPrice = (TextView) convertView.findViewById(R.id.goods_price);
			holder.deleteButton = (ImageView) convertView.findViewById(R.id.delete_button);
			holder.deleteButton.setOnClickListener(mDeletedClickListener);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		GoodsListItem goodsItem = mGoodsListItems.get(position);
		holder.goodsName.setText(goodsItem.getGoodsName());
		holder.goodsPrice.setText(UnitConvertUtil.getSwitchedMoney(goodsItem.getPrice()) +
				"/" + UnitConvertUtil.getSwitchedWeight(1000, goodsItem.getUnit()));
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView deleteButton;
		private TextView goodsName;
		private TextView goodsPrice;
	}
	
	
	/**
	 * 删除按钮监听
	 * @author PeggyTong
	 *
	 */
	class DeleteClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			CustomDialog dialog = new CustomDialog(mContext, R.layout.dialog_delete_notify);
			dialog.setDismissButtonId(R.id.cancel_button);
		}
	}

}
