package com.guozha.buy.view;

import com.guozha.buy.util.LogUtil;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ��дViewPager(��ʱĿ����Ϊ�������¼�)Oss
 * @author Administrator
 *
 */
public class CustomViewPager extends ViewPager{
	
	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		LogUtil.e("customViewPager_onInterceptTouchEvent");
		boolean isValidate = mOnInterceptTouchListener.interceptTouched(
				event.getY(), event.getY());
		if(isValidate){ //����
			return super.onInterceptTouchEvent(event);
		}else{
			return false;
		}
	}
	
	private OnInterceptTouchListener mOnInterceptTouchListener;
	
	/**
	 * �����ṩ���¼����ؽӿ�
	 * @author Administrator
	 *
	 */
	public interface OnInterceptTouchListener{
		public boolean interceptTouched(float eventX, float eventY);
	}
	
	/**
	 * ��InterceptTouch�¼��Ļص��������ж����꣩
	 * @param onInterceptTouchListener
	 */
	public void setOnInterceptTouchListener(OnInterceptTouchListener onInterceptTouchListener){
		this.mOnInterceptTouchListener = onInterceptTouchListener;
	}
}
