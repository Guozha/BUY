package com.guozha.buy.test;

import com.guozha.buy.activity.global.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
	
	private MainActivity mMainActivity;

	public MainActivityTest() {
		super(MainActivity.class);
		
	}
	
	/**
	 * 调用测试方法之前调用（比如初始化对象）
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mMainActivity = getActivity();
		if(mMainActivity == null) return;
	}
	
	/**
	 * 测试后的清理工作
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
	}
	
	/**
	 * 测试前提（保证实例初始化正确）
	 */
	public void testPreconditions() {  
	    assertNotNull("mMainActivity is null", mMainActivity);  
	}  
	
	/**
	 * 测试主界面的时令图片的更换
	 */
	public void testMainActivity_mPage_seasonImage(){
		
	}
	
}
