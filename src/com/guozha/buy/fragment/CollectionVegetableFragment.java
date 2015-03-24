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
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.CollectionVegetableListAdapter;
import com.guozha.buy.dialog.WeightSelectDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 菜品收藏
 * @author PeggyTong
 *
 */
public class CollectionVegetableFragment extends Fragment{
	
	private static final String PAGE_NAME = "CollectionVegetablePage";

	private ListView mCollectionVegetableList;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_collection_vegetable, container, false);
		initView(view);
		return view;
	}
	
	private void initView(View view){
		mCollectionVegetableList = (ListView) view.findViewById(R.id.collection_vegetable_list);
		mCollectionVegetableList.setAdapter(new CollectionVegetableListAdapter(getActivity()));
		
		mCollectionVegetableList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(
						CollectionVegetableFragment.this.getActivity(), WeightSelectDialog.class);
				startActivity(intent);
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
