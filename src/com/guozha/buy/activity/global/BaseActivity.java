package com.guozha.buy.activity.global;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Activity的基类
 * @author lixiaoqiang
 *
 */
public abstract class BaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 设置ActionBar
	 * @return
	 */
	protected ActionBar customActionBarStyle(){
		ActionBar actionbar = getActionBar();
		if(actionbar == null) return null;
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(false);
		return actionbar;
	}
	
	/**
	 * 设置ActionBarTitle
	 * @param title
	 */
	protected void customActionBarStyle(String title){
		ActionBar actionbar = customActionBarStyle();
		if(actionbar == null) return;
		actionbar.setTitle(title);
	}
	
	/**
	 * 自定义自己的ActionBar样式
	 */
	protected ActionBar customActionBarStyle(int customLayout){
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		View customView = customActionBarView(customLayout);
		if(customView == null) return null;
		actionbar.setCustomView(customView);
		return actionbar;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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



	