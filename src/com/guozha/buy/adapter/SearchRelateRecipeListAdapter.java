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
import com.guozha.buy.entry.global.SearchRecipe;
import com.guozha.buy.entry.mine.collection.Material;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 查找结果菜品相关菜谱列表适配器
 * @author PeggyTong
 *
 */
public class SearchRelateRecipeListAdapter extends BaseAdapter{
	
	private List<SearchRecipe> mRecipeListItem;
	
	private LayoutInflater mInflater;
	private BitmapCache mBitmapCache;
	
	public SearchRelateRecipeListAdapter(Context context, List<SearchRecipe> searchRecipItems){
		mInflater = LayoutInflater.from(context);
		mRecipeListItem = searchRecipItems;
		mBitmapCache = new BitmapCache(context);
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
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		SearchRecipe searchRecipe = mRecipeListItem.get(position);
		mBitmapCache.loadBitmaps(holder.image, searchRecipe.getMenuImg());
		holder.name.setText(searchRecipe.getMenuName());
		holder.descript.setText(getRecipeDescript(searchRecipe.getGoodsList()));
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView image;
		private TextView name;
		private TextView descript;
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
}
