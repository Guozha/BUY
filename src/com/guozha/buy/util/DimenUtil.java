package com.guozha.buy.util;

import android.content.Context;

/**
 * �ߴ繤��
 * @author Administrator
 *
 */
public class DimenUtil {

	/**
	 * dipתpix
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	} 
}
