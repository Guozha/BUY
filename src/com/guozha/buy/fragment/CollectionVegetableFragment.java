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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.adapter.CollectionVegetableListAdapter;
import com.guozha.buy.adapter.CollectionVegetableListAdapter.UpdateVegetableListener;
import com.guozha.buy.dialog.WeightSelectDialog;
import com.guozha.buy.entry.mine.collection.GoodsListItem;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 菜品收藏
 * @author PeggyTong
 *
 */
public class CollectionVegetableFragment extends Fragment{
	
	private static final String PAGE_NAME = "CollectionVegetablePage";
	
	private static final int HAND_DATA_COMPLETED = 0x0001;

	private ListView mCollectionVegetableList;
	private CollectionVegetableListAdapter mCollectionVegetableAdapter;
	
	private List<GoodsListItem> mGoodsListItems = new ArrayList<GoodsListItem>();
	private View mEmptyView;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateView();
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_collection_vegetable, container, false);
		initView(view);
		initData();
		return view;
	}
	
	/**
	 * 初始化View
	 * @param view
	 */
	private void initView(View view){
		mEmptyView = view.findViewById(R.id.empty_view);
		mCollectionVegetableList = (ListView) view.findViewById(R.id.collection_vegetable_list);
		mCollectionVegetableList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GoodsListItem goodsListItem = mGoodsListItems.get(position);
				Intent intent = new Intent(
						CollectionVegetableFragment.this.getActivity(), WeightSelectDialog.class);
				intent.putExtra("goodsId", String.valueOf(goodsListItem.getGoodsId()));
				intent.putExtra("unitPrice", String.valueOf(goodsListItem.getUnitPrice()));
				intent.putExtra("unit", goodsListItem.getUnit());
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("account/myfavo/listGoodsFavo")
		.setParams("userId", userId)
		.setParams("addressId", addressId);
		
		HttpManager.getInstance(getActivity()).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<GoodsListItem> goods = gson.fromJson(response, new TypeToken<List<GoodsListItem>>() { }.getType());
					mGoodsListItems.clear();
					mGoodsListItems.addAll(goods);
					handler.sendEmptyMessage(HAND_DATA_COMPLETED);
				}
			});
	}
	
	/**
	 * 更新界面
	 */
	private void updateView(){
		if(mCollectionVegetableAdapter == null){
			mCollectionVegetableAdapter = new CollectionVegetableListAdapter(getActivity(), mGoodsListItems);
			mCollectionVegetableAdapter.setOnUpdateVegetableListener(new UpdateVegetableListener() {
				@Override
				public void update() {
					initData();
				}
			});
			mCollectionVegetableList.setAdapter(mCollectionVegetableAdapter);
		}else{
			mCollectionVegetableAdapter.notifyDataSetChanged();
		}
		if(mEmptyView == null) return;
		if(mGoodsListItems == null || mGoodsListItems.isEmpty()){
			mCollectionVegetableList.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.VISIBLE);
		}else{
			mEmptyView.setVisibility(View.GONE);
			mCollectionVegetableList.setVisibility(View.VISIBLE);
		}
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
