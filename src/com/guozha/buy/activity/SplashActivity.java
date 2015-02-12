package com.guozha.buy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.guozha.buy.R;
import com.guozha.buy.util.LogUtil;

/**
 * ��������
 * @author lixiaoqiang
 *
 */
public class SplashActivity extends BaseActivity{
	
	private static final int SPLASH_TIME = 2000; //��������ʱ��
	private static final int MSG_START_INIT = 0x0001;  //��ʼ��ʼ��
	private static final int MSG_TURN_MAIN = 0x0002;  //ת��MainActivity
	
	private long mInitStartTime;
	
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
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		LogUtil.e("onWindowFocusChanged");
		handler.sendEmptyMessage(MSG_START_INIT);
	}
	
	/**
	 * ��ת��������
	 */
	private void turnMainActivity(){
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		SplashActivity.this.finish();
	}
	
	/**
	 * ����ʼ������
	 */
	private void doInit(){
		//TODO ��һЩҵ���߼������������Դ��
		
	}
}
