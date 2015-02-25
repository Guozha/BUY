package com.guozha.buy.fragment;

import com.guozha.buy.R;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainTabFragmentMine extends Fragment{
	
	private static final String PAGE_NAME = "MinePage";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_maintab_mine, container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//友盟页面统计
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//友盟页面统计
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
}
