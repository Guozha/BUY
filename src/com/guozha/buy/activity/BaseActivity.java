package com.guozha.buy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.guozha.buy.R;

/**
 * Activity的基类
 * @author lixiaoqiang
 *
 */
abstract class BaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initActionBar(getActionBar());
	}
	
	/**
	 * 默认的ActionBar样式初始化
	 * @param actionbar
	 */
	private void initActionBar(ActionBar actionbar){
		if(actionbar == null) return;
		CharSequence title = actionbar.getTitle();
		if(title == null){
			customActionBarStyle(actionbar, null);
		}else{
			customActionBarStyle(actionbar, title.toString());
		}
	}
	
	/**
	 * 自定义自己的ActionBar样式
	 */
	protected void customActionBarStyle(ActionBar actionbar, String title){
		if(title != null){
			actionbar.setDisplayHomeAsUpEnabled(true);
			actionbar.setDisplayShowHomeEnabled(true);
			actionbar.setDisplayShowTitleEnabled(true);
			actionbar.setDisplayShowCustomEnabled(false);
			actionbar.setTitle(title);
		}else{
			actionbar.setDisplayHomeAsUpEnabled(false);
			actionbar.setDisplayShowHomeEnabled(false);
			actionbar.setDisplayShowTitleEnabled(false);
			actionbar.setDisplayShowCustomEnabled(true);
			View customView = customActionBarView(R.layout.actionbar_base_view);
			if(customView == null) return;
			actionbar.setCustomView(customView);
		}
	}
	
	/**
	 * 自定义自己的ActionBar视图
	 * @param layoutId
	 * @return
	 */
	protected View customActionBarView(int layoutId){
		View view = null;
		try{
			view = LayoutInflater.from(this).inflate(layoutId, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return view;
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
}



	