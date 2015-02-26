package com.guozha.buy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.guozha.buy.R;
import com.guozha.buy.activity.ListVegetableActivity;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMarket extends Fragment{
	
	private static final String PAGE_NAME = "MarketPage";
	
	private int mMaxDataNum;
	
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_maintab_market, container, false);
		initView(mView);
		
		return mView;
	}
	
	/**
	 * 初始化View
	 * @param view
	 */
	private void initView(View view){
		Button button = (Button) view.findViewById(R.id.load_list);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), ListVegetableActivity.class);
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
			LogUtil.e("Goods_inVisible");
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
}
