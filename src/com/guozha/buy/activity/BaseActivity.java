package com.guozha.buy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.guozha.buy.R;

/**
 * Activity�Ļ���
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
	 * Ĭ�ϵ�ActionBar��ʽ��ʼ��
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
	 * �Զ����Լ���ActionBar��ʽ
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
	 * �Զ����Լ���ActionBar��ͼ
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
}

	