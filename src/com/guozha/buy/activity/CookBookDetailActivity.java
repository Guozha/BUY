package com.guozha.buy.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.DetailMaterialListAdapter;
import com.guozha.buy.adapter.DetailSeasonListAdapter;
import com.guozha.buy.adapter.DetailStepListAdapter;

/**
 * 菜谱详情
 * @author PeggyTong
 *
 */
public class CookBookDetailActivity extends BaseActivity{
	
	private ListView mMaterialList;//食材
	private ListView mSeasonList;  //调料
	private ListView mStepList;    //做法步骤

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cookbook_detail);
		customActionBarStyle("菜品详情");
		
		initView();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mMaterialList = (ListView) findViewById(R.id.cookbook_detail_material);
		mSeasonList = (ListView) findViewById(R.id.cookbook_detail_seasoning);
		mStepList = (ListView) findViewById(R.id.cookbook_detail_step);
		
		mMaterialList.setAdapter(new DetailMaterialListAdapter(this));
		mSeasonList.setAdapter(new DetailSeasonListAdapter(this));
		mStepList.setAdapter(new DetailStepListAdapter(this));
		
		measureListHeight(mMaterialList);
		measureListHeight(mSeasonList);
		measureListHeight(mStepList);
		
		//解决scrollview进入时位置不在最顶部问题
		mMaterialList.post(new Runnable() {
			
			@Override
			public void run() {
				findViewById(R.id.scrollview).scrollTo(0, 0);
			}
		});
		
	}
	
	/**
	 *测量ListView的高度，为了解决和ScrollView滑动冲突问题 
	 * @param listView
	 */
	private void measureListHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if(listAdapter != null){
			int totalHeight = 0; 
		    for (int i = 0; i < listAdapter.getCount(); i++) { 
		        View listItem = listAdapter.getView(i, null, listView); 
		        listItem.measure(0, 0); 
		        totalHeight += listItem.getMeasuredHeight(); 
		    } 
		    ViewGroup.LayoutParams params = listView.getLayoutParams(); 
			params.height = totalHeight + (listView.getDividerHeight() * ( listAdapter.getCount() - 1)); 
			((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
			listView.setLayoutParams(params); 
		}
	}
}
