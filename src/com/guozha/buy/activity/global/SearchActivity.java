package com.guozha.buy.activity.global;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.umeng.analytics.MobclickAgent;

/**
 * 查找界面
 * @author PeggyTong
 *
 */
public class SearchActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SearchPage";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search);
		
		initActionBar(getActionBar());
		
		initView();
	}
	
	private void initView(){
		findViewById(R.id.search_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RequestParam paramPath = new RequestParam("/search")
				.setParams("word", "中文问题")
				.setParams("addressId", "");
				HttpManager.getInstance(SearchActivity.this).volleyRequestByPost(HttpManager.URL + paramPath, 
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							
						}
				});
			}
		});
	}
	
	/**
	 * 初始化ActionBar
	 * @param actionbar
	 */
	private void initActionBar(ActionBar actionbar){
		if(actionbar == null) return;
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.actionbar_search_custom_view);
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
