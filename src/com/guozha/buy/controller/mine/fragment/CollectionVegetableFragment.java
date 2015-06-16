package com.guozha.buy.controller.mine.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.CollectionVegetableListAdapter;
import com.guozha.buy.adapter.CollectionVegetableListAdapter.UpdateVegetableListener;
import com.guozha.buy.controller.BaseFragment;
import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.controller.dialog.WeightSelectDialog;
import com.guozha.buy.entry.mine.collection.GoodsListItem;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.CollectionModel;
import com.guozha.buy.model.result.CollectionModelResult;
import com.umeng.analytics.MobclickAgent;

/**
 * 菜品收藏
 * @author PeggyTong
 *
 */
public class CollectionVegetableFragment extends BaseFragment{
	
	private static final String PAGE_NAME = "CollectionVegetablePage";
	
	private static final int HAND_DATA_COMPLETED = 0x0001;

	private ListView mCollectionVegetableList;
	private CollectionVegetableListAdapter mCollectionVegetableAdapter;
	
	private List<GoodsListItem> mGoodsListItems = new ArrayList<GoodsListItem>();
	private BitmapCache mBitmapCache = BitmapCache.getInstance();
	private View mEmptyView;
	
	private CollectionModel mCollectionModel;
	
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
		mCollectionModel = new CollectionModel(new MyCollectionModelResult());
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
				//TODO 再判断当前选择的地址是否为NULL
				if(ConfigManager.getInstance().getChoosedAddressId() == -1) return;
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
		mCollectionModel.requestGoodsCollectList(getActivity(), userId, addressId);
	}
	
	/**
	 * 更新界面
	 */
	private void updateView(){
		if(mCollectionVegetableAdapter == null){
			mCollectionVegetableAdapter = new CollectionVegetableListAdapter(getActivity(), mGoodsListItems, mBitmapCache);
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
	
	class MyCollectionModelResult extends CollectionModelResult{
		@Override
		public void requestGoodsCollectList(List<GoodsListItem> goodsListItem) {
			mGoodsListItems.clear();
			mGoodsListItems.addAll(goodsListItem);
			handler.sendEmptyMessage(HAND_DATA_COMPLETED);
		}
	}
}
