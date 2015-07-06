package com.guozha.buy.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

import com.guozha.buy.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 查找界面
 * @author PeggyTong
 *
 */
public class SearchActivity extends Activity{
	
	private static final String PAGE_NAME = "查找";
	private EditText mSearchKeyWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		//让Dialog全屏
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		//initActionBar(getActionBar());
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
		
		findViewById(R.id.search_blank_area).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SearchActivity.this.finish();
			}
		});
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
