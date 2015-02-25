package com.guozha.buy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.guozha.buy.R;
import com.guozha.buy.util.LogUtil;

/**
 * 闪屏界面
 * @author lixiaoqiang
 *
 */
public class SplashActivity extends BaseActivity{
	
	private static final int SPLASH_TIME = 2000; //闪屏持续时间
	private static final int MSG_START_INIT = 0x0001;  //开始初始化
	private static final int MSG_TURN_MAIN = 0x0002;  //转向MainActivity
	
	private long mInitStartTime;
	private boolean mHasInit;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_START_INIT:
				mInitStartTime = SystemClock.elapsedRealtime();
				doInit();
				int remainDelay =(int)(SPLASH_TIME - 
						(SystemClock.elapsedRealtime() - mInitStartTime));
				if(remainDelay <= 0){
					turnMainActivity();
				}else{
					handler.sendEmptyMessageDelayed(MSG_TURN_MAIN, remainDelay);
				}
				break;
			case MSG_TURN_MAIN:
				turnMainActivity();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mHasInit = false;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		LogUtil.e("onWindowFocusChanged");
		if(hasFocus && !mHasInit){
			handler.sendEmptyMessage(MSG_START_INIT);
		}
	}
	
	/**
	 * 跳转到主界面
	 */
	private void turnMainActivity(){
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		SplashActivity.this.finish();
	}
	
	/**
	 * 做初始化操作
	 */
	private void doInit(){
		//TODO 做一些业务逻辑（比如加载资源）
		
	}
}
