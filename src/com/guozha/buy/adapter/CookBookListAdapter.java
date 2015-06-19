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
import com.guozha.buy.entry.market.RelationRecipe;
import com.guozha.buy.entry.market.RelationRecipeMaterial;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.ShopCartModel;
import com.guozha.buy.model.result.ShopCartModelResult;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;

public class CookBookListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<RelationRecipe> mRelationRecipe;
	//private Context mContext;
	private BitmapCache mBitmapCache;
	public CookBookListAdapter(Context context, List<RelationRecipe> relationRecipe, BitmapCache bitmapCache){
		mInflater = LayoutInflater.from(context);
		mRelationRecipe = relationRecipe;
		mBitmapCache = bitmapCache;
	}

	@Override
	public int getCount() {
		if(mRelationRecipe == null) return 0;
		return mRelationRecipe.size();
	}

	@Override
	public Object getItem(int position) {
		return mRelationRecipe.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_cook_book_list_cell, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.cook_book_imag);
			holder.name = (TextView) convertView.findViewById(R.id.cooke_book_name);
			//holder.material = (TextView) convertView.findViewById(R.id.cook_book_material);
			//holder.collectionButton = (ImageView) convertView.findViewById(R.id.cook_book_collection);
			//holder.collectionButton.setOnClickListener(this);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		RelationRecipe relationRecipe = mRelationRecipe.get(position);
		holder.image.setImageResource(R.drawable.default_icon);
		mBitmapCache.loadBitmaps(holder.image, relationRecipe.getMenuImg());
		holder.name.setText(relationRecipe.getMenuName());
		//holder.material.setText(getRecipeDescript(relationRecipe.getGoodsList()));
		//holder.collectionButton.setTag(relationRecipe.getMenuId());
		return convertView;
	}
	
	/**
	 * 获取主料拼接串
	 * @param materials
	 * @return
	 */
	private String getRecipeDescript(List<RelationRecipeMaterial> materials){
		StringBuffer buffer = new StringBuffer("主料:");
		if(materials == null) return buffer.toString();
		for(int i = 0; i < materials.size(); i++){
			RelationRecipeMaterial material = materials.get(i);
			buffer.append(material.getGoodsName());
			buffer.append(UnitConvertUtil.getSwitchedWeight(material.getAmount(), material.getUnit()));
			if(buffer.length() > 30) break;
			buffer.append("、");
		}
		return buffer.toString();
	}
	
    static class ViewHolder{
    	private ImageView image;
    	private TextView name;
    	//private TextView material;
    	//private ImageView collectionButton;
    }

    
	/**
	 * 请求添加购物车
	 * @param menuId
	 */
    /*
	private void requestCollectionRecipe(int menuId) {
		String token = ConfigManager.getInstance().getUserToken();
		if(token == null) return;  //TODO 先登录
		int userId = ConfigManager.getInstance().getUserId();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		mShopCartModel.requestAddCart(mContext, userId, menuId, "01", 1, token, addressId);
	}
	
	class MyShopCartModelResult extends ShopCartModelResult{
		@Override
		public void requestAddCartResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				ToastUtil.showToast(mContext, "添加成功");
			}else{
				ToastUtil.showToast(mContext, msg);
			}
		}
	}
	*/
}
