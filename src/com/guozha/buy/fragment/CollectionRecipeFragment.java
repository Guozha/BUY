package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.mpage.CookBookDetailActivity;
import com.guozha.buy.adapter.CollectionRecipeExpandAdapter;
import com.guozha.buy.adapter.CollectionRecipeExpandAdapter.UpdateRecipeListener;
import com.guozha.buy.entry.mine.collection.CollectionDir;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.view.AnimatedExpandableListView;
import com.umeng.analytics.MobclickAgent;

/**
 * 食谱收藏
 * @author PeggyTong
 *
 */
public class CollectionRecipeFragment extends Fragment{
	
	private static final String PAGE_NAME = "CollectionRecipePage";
	private static final int HAND_DIR_DATA_COMPLETED = 0x0001;  //收藏文件夹数据请求完毕
	
	private AnimatedExpandableListView mCollectionRecipeList;
	
	private List<CollectionDir> mCollectionDir = new ArrayList<CollectionDir>();
	private CollectionRecipeExpandAdapter mCollectionRecipeAdapter;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DIR_DATA_COMPLETED:
				if(mCollectionDir == null) return;
				updateView();
				break;
			}
		};
	};
	
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
				Intent intent = new Intent(
						CollectionRecipeFragment.this.getActivity(), CookBookDetailActivity.class);
				startActivity(intent);
				return false;
			}
		});
	}
	
	private void updateView(){
		if(mCollectionRecipeAdapter != null){
			mCollectionRecipeAdapter.notifyDataSetChanged();
		}else{
			mCollectionRecipeAdapter = new CollectionRecipeExpandAdapter(getActivity(), mCollectionDir);
			mCollectionRecipeList.setAdapter(mCollectionRecipeAdapter);
			mCollectionRecipeAdapter.setOnUpdateRecipeListener(new UpdateRecipeListener() {
				@Override
				public void update() {
					requestCollectionRecipeData();
				}
			});
		}
	}
	
	/**
	 * 请求收藏菜谱数据
	 */
	private void requestCollectionRecipeData(){
		int userId = ConfigManager.getInstance().getUserId();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("account/myfavo/listMenuFavo")
		.setParams("userId", userId)
		.setParams("addressId", addressId);
		HttpManager.getInstance(getActivity()).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<CollectionDir> collectionDir = gson.fromJson(response, new TypeToken<List<CollectionDir>>() { }.getType());
					mCollectionDir.clear();
					mCollectionDir.addAll(collectionDir);
					handler.sendEmptyMessage(HAND_DIR_DATA_COMPLETED);
				}
			});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		requestCollectionRecipeData();
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
