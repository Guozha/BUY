package com.guozha.buy.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 重写ViewPager(暂时目的是为了拦截事件)
 * @author Administrator
 *
 */
public class CustomViewPager extends ViewPager{
	
	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		/*
		boolean isValidate = mOnInterceptTouchListener.interceptTouched(
				event.getY(), event.getY());
		if(isValidate){ //拦截
			return super.onInterceptTouchEvent(event);
		}
		*/
		
		return super.onInterceptTouchEvent(event);
		
	}
	
	private OnInterceptTouchListener mOnInterceptTouchListener;
	
	/**
	 * 对外提供的事件拦截接口
	 * @author Administrator
	 *
	 */
	public interface OnInterceptTouchListener{
		public boolean interceptTouched(float eventX, float eventY);
	}
	
	/**
	 * 对InterceptTouch事件的回调（用于判断坐标）
	 * @param onInterceptTouchListener
	 */
	public void setOnInterceptTouchListener(OnInterceptTouchListener onInterceptTouchListener){
		this.mOnInterceptTouchListener = onInterceptTouchListener;
	}
}
