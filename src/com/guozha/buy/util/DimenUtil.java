package com.guozha.buy.util;

import android.content.Context;

/**
 * ³ß´ç¹¤¾ß
 * @author Administrator
 *
 */
public class DimenUtil {

	/**
	 * dip×ªpix
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	} 
}
