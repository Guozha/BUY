package com.guozha.buy.util;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * 尺寸工具
 * @author Administrator
 *
 */
public class DimenUtil {

	/**
	 * dip转pix
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	} 
	
	/**
	 * 获取屏幕的宽度和高度
	 * @param context
	 * @return
	 */
	public static int[] getScreenWidthAndHeight(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	 
		int[] screenDimen = new int[2];
	    int width = wm.getDefaultDisplay().getWidth();
	    int height = wm.getDefaultDisplay().getHeight();
	    screenDimen[0] = width;
	    screenDimen[1] = height;
	    
	    return screenDimen;
	}
	
	/**
	 * 获取View相对于屏幕的圆点坐标
	 * @param view
	 * @return
	 */
	public static int[] getViewOriginPoint(View view){
		int[] location = new int[2];  
	    view.getLocationOnScreen(location); 
	    return location;
	}
}
