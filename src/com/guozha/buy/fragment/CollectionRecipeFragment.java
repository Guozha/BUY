package com.guozha.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.guozha.buy.R;
import com.guozha.buy.view.AnimatedExpandableListView;

/**
 * 食谱收藏
 * @author PeggyTong
 *
 */
public class CollectionRecipeFragment extends Fragment{
	
	private AnimatedExpandableListView mCollectionRecipeList;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_collection_recipe, container, false);
		initView(view);
		return view;
	}
	
	private void initView(View view){
		mCollectionRecipeList = 
				(AnimatedExpandableListView) view.findViewById(R.id.collection_recipe_list);
		
		mCollectionRecipeList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//TODO 跳到菜谱详情界面
				return false;
			}
		});
	}

}
