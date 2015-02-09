package com.guozha.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guozha.buy.R;
import com.guozha.buy.view.SlidingSwitcherView;

public class MainTabFragmentMPage extends Fragment{

	private static final String TAG = "MainTabFragmentMPage";

	private View mView;
	private SlidingSwitcherView mSlidingSwitcherView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_maintab_mpage, container, false);
		mSlidingSwitcherView = 
				(SlidingSwitcherView) mView.findViewById(R.id.slidingLayout);
		return mView;
	}

	/**
	 * 自动滚动View停止
	 */
	public void stopSlideViewPlay(){
		if(mSlidingSwitcherView == null) return;
		mSlidingSwitcherView.stopAutoPlay();
	}
	
	/**
	 * 自动滚动View开始
	 */
	public void startSlideViewPlay(){
		if(mSlidingSwitcherView == null) return;
		mSlidingSwitcherView.startAutoPlay();
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
	}
	
	@Override
	public void onResume() {
		super.onResume();
		startSlideViewPlay();
	}
}
