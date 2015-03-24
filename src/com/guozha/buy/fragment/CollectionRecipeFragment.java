package com.guozha.buy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

import com.guozha.buy.R;
import com.guozha.buy.activity.mpage.CookBookDetailActivity;
import com.guozha.buy.adapter.CollectionRecipeExpandAdapter;
import com.guozha.buy.view.AnimatedExpandableListView;
import com.umeng.analytics.MobclickAgent;

/**
 * 食谱收藏
 * @author PeggyTong
 *
 */
public class CollectionRecipeFragment extends Fragment{
	
	private static final String PAGE_NAME = "CollectionRecipePage";
	
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
		mCollectionRecipeList.setAdapter(new CollectionRecipeExpandAdapter(getActivity()));
		mCollectionRecipeList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//TODO 跳到菜谱详情界面
				Intent intent = new Intent(
						CollectionRecipeFragment.this.getActivity(), CookBookDetailActivity.class);
				startActivity(intent);
				return false;
			}
		});
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见	
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}

}
