package com.guozha.buy.activity;

import android.os.Bundle;
import android.view.View;

import com.guozha.buy.R;

/**
 * 应用主界面
 * @author lixiaoqiang
 *
 */
public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected View customActionBarView(int layoutId) {
		return super.customActionBarView(R.layout.actionbar_main_view);
	}
	
	
}
