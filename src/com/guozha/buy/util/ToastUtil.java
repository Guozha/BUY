package com.guozha.buy.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * @author PeggyTong
 *
 */
public class ToastUtil {

	/**
	 * 显示默认的Toast
	 * @param context
	 * @param message
	 */
	public static void showToast(Context context, String message){
		if(message == null) return;
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
