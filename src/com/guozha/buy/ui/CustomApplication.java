package com.guozha.buy.ui;

import org.litepal.LitePalApplication;

import android.content.Context;

import com.guozha.buy.global.net.BitmapCache;

/**
 * 全局的Application
 * @author Administrator
 *
 */
public class CustomApplication extends LitePalApplication{
	
	private static CustomApplication instance;
	private static BitmapCache mBitmapCache;
    
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		//捕获错误日志
		//CrashHandler crashHandler = CrashHandler.getInstance();
		//crashHandler.init(getApplicationContext());
	}
	
	/**
	 * 获取图片缓存器
	 * @return
	 */
	public static BitmapCache getBitmapCache(){
		if(mBitmapCache == null){
			mBitmapCache = new BitmapCache(getContext());
		}
		return mBitmapCache;
	}
	
	/**
	 * 获取上下文实例
	 * @return
	 */
	public static Context getContext(){
		return instance.getApplicationContext();
	}
	
}
