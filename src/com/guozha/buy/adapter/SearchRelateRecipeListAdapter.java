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
import com.guozha.buy.entry.global.SearchRecipe;
import com.guozha.buy.entry.mine.collection.Material;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.ShopCartModel;
import com.guozha.buy.model.result.ShopCartModelResult;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 查找结果菜品相关菜谱列表适配器
 * @author PeggyTong
 *
 */
public class SearchRelateRecipeListAdapter extends BaseAdapter implements OnClickListener{
	
	private List<SearchRecipe> mRecipeListItem;
	
	private LayoutInflater mInflater;
	private BitmapCache mBitmapCache;
	private Context mContext;
	private ShopCartModel mShopCartModel;
	
	public SearchRelateRecipeListAdapter(Context context, List<SearchRecipe> searchRecipItems, BitmapCache bitmapCache){
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mRecipeListItem = searchRecipItems;
		mBitmapCache = bitmapCache;
		mShopCartModel = new ShopCartModel(new MyShopCartModelResult());
	}

	@Override
	public int getCount() {
		if(mRecipeListItem == null) return 0;
		return mRecipeListItem.size();
	}

	@Override
	public Object getItem(int position) {
		return mRecipeListItem.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_search_recipe_cell, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.search_recipe_image);
			holder.name = (TextView) convertView.findViewById(R.id.search_recipe_name);
			holder.descript = (TextView) convertView.findViewById(R.id.search_recipe_descript);
			holder.addCart = (ImageView) convertView.findViewById(R.id.search_recipe_addcart_button);
			holder.addCart.setOnClickListener(this);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		SearchRecipe searchRecipe = mRecipeListItem.get(position);
		mBitmapCache.loadBitmaps(holder.image, searchRecipe.getMenuImg());
		holder.name.setText(searchRecipe.getMenuName());
		holder.descript.setText(getRecipeDescript(searchRecipe.getGoodsList()));
		holder.addCart.setTag(searchRecipe.getMenuId());
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView image;
		private TextView name;
		private TextView descript;
		private ImageView addCart;
	}

	/**
	 * 获取主料拼接串
	 * @param materials
	 * @return
	 */
	private String getRecipeDescript(List<Material> materials){
		StringBuffer buffer = new StringBuffer("主料:");
		if(materials == null) return buffer.toString();
		for(int i = 0; i < materials.size(); i++){
			Material material = materials.get(i);
			buffer.append(material.getGoodsName());
			buffer.append(UnitConvertUtil.getSwitchedWeight(material.getAmount(), material.getUnit()));
			if(buffer.length() > 30) break;
			buffer.append("、");
		}
		return buffer.toString();
	}

	@Override
	public void onClick(View view) {
		int menuId = (Integer) view.getTag();
		int userId = ConfigManager.getInstance().getUserId();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		String token = ConfigManager.getInstance().getUserToken(mContext);
		mShopCartModel.requestAddCart(mContext, userId, menuId, "01", 1, token, addressId);
	}
	
	class MyShopCartModelResult extends ShopCartModelResult{
		@Override
		public void requestAddCartResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				ToastUtil.showToast(mContext, "添加购物车成功");
			}else{
				ToastUtil.showToast(mContext, msg);
			}
		}
	}
}
