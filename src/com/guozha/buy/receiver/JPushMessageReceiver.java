package com.guozha.buy.receiver;

import com.guozha.buy.activity.global.SplashActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

/**
 * 接收极光推送的消息
 * @author PeggyTong
 *
 */
public class JPushMessageReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context content, Intent intent) {
		Bundle bundle = intent.getExtras();
		
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            System.out.println("通知的内容是" + bundle.getString(JPushInterface.EXTRA_ALERT));
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent newIntent = new Intent(content, SplashActivity.class);  //自定义打开的界面
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            content.startActivity(newIntent);
        } else {
            //Log.d(TAG, "Unhandled intent - " + intent.getAcion());
        }
	}

}
