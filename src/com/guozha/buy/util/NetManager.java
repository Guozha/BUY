package com.guozha.buy.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络管理类
 * @author PeggyTong
 *
 */
public class NetManager {

	/**
	 * 判断网络是否连接
	 * @param context
	 * @return
	 */
	public static boolean isNetConnection(Context context){
		ConnectivityManager connectivityManager = 
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(!mobileInfo.isConnected() && !wifiInfo.isConnected()){
			//网络不可用
			return false;
		}else{
			return true;
		}
	}
}

