package com.guozha.buy.activity;

import com.guozha.buy.R;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;

/**
 * 时令界面
 * @author PeggyTong
 *
 */
public class SeasonActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SeasonPage";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_season);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//友盟界面统计
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
		
	}
}
