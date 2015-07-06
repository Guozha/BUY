package com.guozha.buy.controller;

import org.litepal.LitePalApplication;

import android.content.Context;

/**
 * 全局的Application
 * @author Administrator
 *
 */
public class CustomApplication extends LitePalApplication{
	
	private static CustomApplication instance;
    
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		//捕获错误日志
		//CrashHandler crashHandler = CrashHandler.getInstance();
		//crashHandler.init(getApplicationContext());
	}
	
	/**
	 * 获取上下文实例
	 * @return
	 */
	public static Context getContext(){
		return instance.getApplicationContext();
	}
}
