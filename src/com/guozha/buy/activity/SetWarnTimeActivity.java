package com.guozha.buy.activity;

import com.guozha.buy.R;

import android.os.Bundle;

/**
 * 设置提醒
 * @author PeggyTong
 *
 */
public class SetWarnTimeActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setwarntime);
		
		customActionBarStyle("提醒");
	}

}
