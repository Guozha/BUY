package com.guozha.buy.controller;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.guozha.buy.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 查找界面
 * @author PeggyTong
 *
 */
public class SearchActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SearchPage";
	private EditText mSearchKeyWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search);
		
		initActionBar(getActionBar());
		
		initView();
	}
	
	private void initView(){
		mSearchKeyWord = (EditText) findViewById(R.id.search_keyword);
		findViewById(R.id.search_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String keyWord = mSearchKeyWord.getText().toString();
				if(!keyWord.isEmpty()){
					Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
					intent.putExtra("KeyWord", keyWord);
					startActivity(intent);
				}
			}
		});
		
		findViewById(R.id.back_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.this.finish();
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
