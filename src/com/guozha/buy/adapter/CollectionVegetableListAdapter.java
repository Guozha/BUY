package com.guozha.buy.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.entry.mine.collection.GoodsListItem;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.ToastUtil;
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
	private BitmapCache mBitmapCache;
	
	public CollectionVegetableListAdapter(Context context, List<GoodsListItem> goodsListItems, BitmapCache bitmapCache){
		mContext = context;
		mDeletedClickListener = new DeleteClickListener();
		mGoodsListItems = goodsListItems;
		mInflater = LayoutInflater.from(context);
		mBitmapCache = bitmapCache;
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
			holder.goodsIcon = (ImageView) convertView.findViewById(R.id.goods_icon);
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
		holder.goodsPrice.setText(UnitConvertUtil.getSwitchedMoney(goodsItem.getUnitPrice()) +
				"元/" + UnitConvertUtil.getSwichedUnit(1000, goodsItem.getUnit()));
		holder.deleteButton.setTag(goodsItem.getMyGoodsId());
		holder.goodsIcon.setImageResource(R.drawable.default_icon);
		mBitmapCache.loadBitmaps(holder.goodsIcon, goodsItem.getGoodsImg());
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView goodsIcon;
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
		public void onClick(View view) {
			final int goodsId = (Integer) view.getTag();
			final CustomDialog dialog = new CustomDialog(mContext, R.layout.dialog_delete_notify);
			dialog.setDismissButtonId(R.id.cancel_button);
			dialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					requestDeleteVegetable(goodsId);
					dialog.dismiss();
				}
			});
		}
	}
	
	/**
	 * 请求删除菜品
	 * @param goodsId
	 */
	private void requestDeleteVegetable(final int goodsId) {
		String token = ConfigManager.getInstance().getUserToken();
		RequestParam paramPath = new RequestParam("account/myfavo/deleteMyGoods")
		.setParams("token", token)
		.setParams("myGoodsId", goodsId);
		HttpManager.getInstance(mContext).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							if(mUpdateRecipeListener != null){
								mUpdateRecipeListener.update();
							}
						}else{
							ToastUtil.showToast(mContext, response.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
	
	private UpdateVegetableListener mUpdateRecipeListener;
	
	public interface UpdateVegetableListener{
		public void update();
	}
	
	public void setOnUpdateVegetableListener(UpdateVegetableListener updateVegetableListener){
		this.mUpdateRecipeListener = updateVegetableListener;
	}

}
