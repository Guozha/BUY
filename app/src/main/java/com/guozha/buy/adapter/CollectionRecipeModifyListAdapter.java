package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mine.collection.CollectionDir;

/**
 * 菜谱分类修改列表适配器
 * @author PeggyTong
 *
 */
public class CollectionRecipeModifyListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<CollectionDir> mCollectionDirs;
	public CollectionRecipeModifyListAdapter(Context context, List<CollectionDir> collectionRecip){
		mInflater = LayoutInflater.from(context);
		mCollectionDirs = collectionRecip;
	}

	@Override
	public int getCount() {
		if(mCollectionDirs == null) return 0;
		return mCollectionDirs.size();
	}

	@Override
	public Object getItem(int position) {
		return mCollectionDirs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_collection_recipe_modify_item_cell, null);
		}
		TextView folderName = (TextView) convertView.findViewById(R.id.collection_recipe_folder_name);
		folderName.setText(mCollectionDirs.get(position).getDirName());
		return convertView;
	}

}
