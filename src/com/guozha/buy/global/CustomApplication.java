package com.guozha.buy.global;

import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * 全局的Application
 * @author Administrator
 *
 */
public class CustomApplication extends LitePalApplication{

	@Override
	public void onCreate() {
		super.onCreate();
		
		//极光推送相关
		//TODO 注意发布的时候修改Debug模式
		JPushInterface.setDebugMode(true); //设置为Debug模式
		JPushInterface.init(this); //初始化极光SDK
	}
}
