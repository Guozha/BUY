package com.guozha.buy.activity.mpage;

import android.os.Bundle;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.SeasonItemListAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 时令界面
 * @author PeggyTong
 *
 */
public class SeasonActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SeasonPage";
	
	private ListView mSeasonItemList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_season);
		customActionBarStyle("时令养生");
		initView();
	}
	
	private void initView(){
		mSeasonItemList = (ListView) findViewById(R.id.season_vegetable_list);
		mSeasonItemList.setAdapter(new SeasonItemListAdapter(this));
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
