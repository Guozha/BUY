package com.guozha.buy.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.guozha.buy.controller.dialog.AddCartAnimDialog;

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
		if(context == null || message == null) return;
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	public static void showBottomAddCartAnim(Context context){
		Intent intent = new Intent(context, AddCartAnimDialog.class);
		intent.putExtra("showText", "+1");
		intent.putExtra("isTop", false);
		context.startActivity(intent);
	}
	
	public static void showTopAddCartAnim(Context context){
		Intent intent = new Intent(context, AddCartAnimDialog.class);
		intent.putExtra("showText", "+1");
		intent.putExtra("isTop", true);
		context.startActivity(intent);
	}
	
	public static void showCollectionAnim(Context context){
		Intent intent = new Intent(context, AddCartAnimDialog.class);
		intent.putExtra("showText", "收藏成功");
		intent.putExtra("isTop", true);
		context.startActivity(intent);
	}
}
