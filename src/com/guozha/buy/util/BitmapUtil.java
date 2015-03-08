package com.guozha.buy.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * 图片工具类
 * @author PeggyTong
 *
 */
public class BitmapUtil {

	/**
	 * 切割图片成圆形
	 * @param source
	 * @return
	 */
	public static Bitmap createCircleBitmap(Bitmap source){
		int width = source.getWidth();
		int height = source.getHeight();
		int min = width > height ? height : width;
		
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target =	 Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * 产生一个同样大的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN模式
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		
		return target;
	}
}
