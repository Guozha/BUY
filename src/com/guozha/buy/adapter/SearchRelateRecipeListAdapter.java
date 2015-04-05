package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.guozha.buy.R;
import com.guozha.buy.entry.mine.collection.RecipeListItem;

/**
 * 查找结果菜品相关菜谱列表适配器
 * @author PeggyTong
 *
 */
public class SearchRelateRecipeListAdapter extends BaseAdapter{
	
	private List<RecipeListItem> mRecipeListItem;
	
	private LayoutInflater mInflater;
	
	public SearchRelateRecipeListAdapter(Context context, List<RecipeListItem> recipeListItems){
		mInflater = LayoutInflater.from(context);
		mRecipeListItem = recipeListItems;
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
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_search_recipe_cell, null);
		}
		return convertView;
	}
	
	static class ViewHolder{
		
	}

}
