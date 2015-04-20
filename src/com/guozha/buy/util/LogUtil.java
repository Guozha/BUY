package com.guozha.buy.util;

import android.util.Log;

/**
 * Log工具类
 * @author Administrator
 *
 */
public class LogUtil {
	
	private static final String TAG = "BUY_TAG";
	
	private static final boolean DEBUG = false; //LOG开关
	
    public static void d(String TAG, String msg){  
    	if(!DEBUG) return;
    	if(TAG == null || msg == null) return;
        Log.d(TAG, msg);  
    }  
      
    public static void d(String msg){  
        if(!DEBUG) return;
        if(msg == null) return;
        Log.d(TAG, msg);  
    }  
      
    public static void e(String TAG, String msg){  
    	if(!DEBUG) return;
    	if(TAG == null || msg == null) return;
        Log.e(TAG, msg);  
    }  
      
    public static void e(String msg){  
        if(!DEBUG) return;
        if(msg == null) return;
        Log.e(TAG, msg);  
    }  
}
