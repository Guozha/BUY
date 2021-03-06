package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.mpage.CookBookDetailActivity;
import com.guozha.buy.adapter.CollectionRecipeExpandAdapter;
import com.guozha.buy.adapter.CollectionRecipeExpandAdapter.UpdateRecipeListener;
import com.guozha.buy.dialog.CollectionRecipeModifyDialog;
import com.guozha.buy.dialog.CustomDialog;
import com.guozha.buy.entry.mine.collection.CollectionDir;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.ToastUtil;
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
	
	private BitmapCache mBitmapCache = CustomApplication.getBitmapCache();
	
	private List<CollectionDir> mCollectionDir = new ArrayList<CollectionDir>();
	private CollectionRecipeExpandAdapter mCollectionRecipeAdapter;
	private View mEmptyView;
	
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
		mEmptyView = view.findViewById(R.id.empty_view);
		mCollectionRecipeList = 
				(AnimatedExpandableListView) view.findViewById(R.id.collection_recipe_list);
		mCollectionRecipeList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//TODO 跳到菜谱详情界面
				Intent intent = new Intent(
						CollectionRecipeFragment.this.getActivity(), CookBookDetailActivity.class);
				intent.putExtra("menuId", 
						mCollectionDir.get(groupPosition).getMenuInfoList().get(childPosition).getMenuId());
				startActivity(intent);
				return false;
			}
		});

		mCollectionRecipeList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final View groupView = view.findViewById(R.id.collection_recipe_dirname);
				if(groupView == null) return false;
				if(position == 0){
					ToastUtil.showToast(CollectionRecipeFragment.this.getActivity(), "默认收藏夹不允许删除");
					return true;
				}
				final CustomDialog deleteDialog = new CustomDialog(
						CollectionRecipeFragment.this.getActivity(), R.layout.dialog_delete_collection_folder);
				deleteDialog.setDismissButtonId(R.id.cancel_button);
				deleteDialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						deleteDialog.dismiss();
						requestDeleteCollectionFolder((Integer)groupView.getTag());
					}
				});
				return true;
			}
		});
	}
	
	private void updateView(){
		if(mCollectionDir.isEmpty()){
			mCollectionRecipeList.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.VISIBLE);
		}else{
			mCollectionRecipeList.setVisibility(View.VISIBLE);
			mEmptyView.setVisibility(View.GONE);
		}
		if(mCollectionRecipeAdapter != null){
			mCollectionRecipeAdapter.notifyDataSetChanged();
		}else{
			mCollectionRecipeAdapter = new CollectionRecipeExpandAdapter(getActivity(), mCollectionDir, mBitmapCache);
			mCollectionRecipeList.setAdapter(mCollectionRecipeAdapter);
			mCollectionRecipeAdapter.setOnUpdateRecipeListener(new UpdateRecipeListener() {
				@Override
				public void update() {
					requestCollectionRecipeData();
				}
			});
		}
		if(!mCollectionDir.isEmpty()){
			mCollectionRecipeList.expandGroup(0);
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
	
	/**
	 * 请求删除收藏夹
	 * @param dirId
	 */
	private void requestDeleteCollectionFolder(int dirId){
		String token = ConfigManager.getInstance().getUserToken(getActivity());
		if(token == null)return;
		RequestParam paramPath = new RequestParam("account/myfavo/deleteMyDir")
		.setParams("token", token)
		.setParams("myDirId", dirId);
		HttpManager.getInstance(getActivity()).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							requestCollectionRecipeData();
						}else{
							ToastUtil.showToast(getActivity(), response.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
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
	
	@Override
	public void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
	}
}
