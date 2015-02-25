package com.guozha.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guozha.buy.R;
import com.guozha.buy.view.AutoViewFlipper;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMPage extends Fragment{
	
	private static final String TAG = "MainTabFragmentMPage";
	private static final String PAGE_NAME = "MainPage";

	private View mView;
	private AutoViewFlipper mAutoViewFlipper;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_maintab_mpage, container, false);
		mAutoViewFlipper = 
				(AutoViewFlipper) mView.findViewById(R.id.main_page_auto_flipper_view);		
		return mView;
	}
	
	public int getBeginYPoint(){
		return (int)mAutoViewFlipper.getY();
	}
	
	public int getEndYPoint(){
		return (int) mAutoViewFlipper.getY() + mAutoViewFlipper.getHeight();
	}

	/**
	 * 自动滚动View停止
	 */
	public void stopSlideViewPlay(){
		if(mAutoViewFlipper == null) return;
		mAutoViewFlipper.stopAutoPlay();
	}
	
	/**
	 * 自动滚动View开始
	 */
	public void startSlideViewPlay(){
		if(mAutoViewFlipper == null) return;
		mAutoViewFlipper.startAutoPlay();
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			startSlideViewPlay();
		}else{
			//View不可见
			stopSlideViewPlay();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		stopSlideViewPlay();
		//友盟页面统计
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		startSlideViewPlay();
		//友盟页面统计
		MobclickAgent.onPageStart(PAGE_NAME);
	}
}
