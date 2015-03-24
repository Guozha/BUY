package com.guozha.buy.activity.mpage;

import android.os.Bundle;
import android.widget.GridView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.PreSpecialGridAdapter;
import com.guozha.buy.global.net.HttpManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 预售/特供
 * @author PeggyTong
 *
 */
public class PreSpecialActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "PreSpecialPage";
	
	private GridView mGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_special);
		customActionBarStyle("特供");
		initData();
		initView();
	}
	
	private void initData(){
		String paramPath = "goods/special?frontTypeId=";
		HttpManager.getInstance(this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				
			}
		});
		
	}
	
	private void initView(){
		
		mGridView = (GridView) findViewById(R.id.pre_special_gridlist);
		
		mGridView.setAdapter(new PreSpecialGridAdapter(this));
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
