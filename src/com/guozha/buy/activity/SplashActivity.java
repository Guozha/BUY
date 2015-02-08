package com.guozha.buy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.guozha.buy.R;

/**
 * 闪屏界面
 * @author lixiaoqiang
 *
 */
public class SplashActivity extends BaseActivity{
	private static final int SPLASH_TIME = 2000; //闪屏持续时间
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
			}
		}, SPLASH_TIME);
	}
}
