package com.guozha.buy.util;

import android.content.Context;
import android.view.WindowManager;

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
	
	/**
	 * ��ȡ��Ļ�Ŀ�Ⱥ͸߶�
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
}
