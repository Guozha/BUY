package com.guozha.buy.alarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 计划菜单闹钟
 * @author PeggyTong
 *
 */
public class PlanMenuAlarm {
	
	private AlarmManager mAlarmManager;

	public PlanMenuAlarm(Context context){
		mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		
	}
	
	/**
	 * 设置闹钟
	 * @param context
	 * @param hour 小时
	 * @param minute 分钟
	 */
	public void setAlarm(Context context, int hour, int minute){
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);  
		Calendar c = Calendar.getInstance();  
        c.setTimeInMillis(System.currentTimeMillis());  
        c.set(Calendar.HOUR, hour);  
        c.set(Calendar.MINUTE, minute); 
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
		
		Toast.makeText(context, "闹钟设置成功", Toast.LENGTH_SHORT).show();
	}
}
