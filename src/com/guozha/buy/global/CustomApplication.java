package com.guozha.buy.global;

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
		/*
		//极光推送相关
		//TODO 注意发布的时候修改Debug模式
		JPushInterface.setDebugMode(true); //设置为Debug模式
		JPushInterface.init(this); //初始化极光SDK
		
		
		//设置别名和标签
		Set<String> tags = new HashSet<String>();
		tags.add("tag1");
		JPushInterface.setAliasAndTags(this, "别名", tags, new TagAliasCallback() {
			
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				
			}
		});
		
		//自定义Notification样式
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
		builder.statusBarDrawable = R.drawable.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; //设置为自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE
				| Notification.DEFAULT_LIGHTS; //设置为铃声和振动都要
		
		//设置默认样式
		JPushInterface.setPushNotificationBuilder(1, builder);
		JPushInterface.setDefaultPushNotificationBuilder(builder);
		*/

		//捕获错误日志
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}
	
	/**
	 * 获取上下文实例
	 * @return
	 */
	public static Context getContext(){
		return instance.getApplicationContext();
	}
	
}
