package com.guozha.buy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.guozha.buy.R;
import com.guozha.buy.activity.PlanMenuActivity;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMPage extends Fragment implements OnClickListener{
	
	private static final String TAG = "MainTabFragmentMPage";
	private static final String PAGE_NAME = "MainPage";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View mView = inflater.inflate(R.layout.fragment_maintab_mpage, container, false);	
		initView(mView);
		return mView;
	}
	
	/**
	 * 初始化视图
	 * @param view
	 */
	private void initView(View view){
		view.findViewById(R.id.fragment_mpage_season).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		//菜谱计划
		case R.id.fragment_mpage_season:
			Intent intent = new Intent(getActivity(), PlanMenuActivity.class);
			startActivity(intent);
			break;

		default:
			break;
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
	
	@Override
	public void onPause() {
		super.onPause();
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
			
	}

}
