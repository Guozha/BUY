package com.guozha.buy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.ListView;

public class CustomListView extends ListView{
	
	public CustomListView(Context context) {
		super(context);
	}
	
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	private VelocityTracker vTracker;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int vTrackY;
		obtainVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			vTracker.computeCurrentVelocity(1000);
			vTrackY = (int) vTracker.getYVelocity();
			
			if(Math.abs(vTrackY) < 1000) 
				return super.onTouchEvent(event);
			
			if(vTrackY > 0){
				if(mOnListViewEventListener == null)
					return super.onTouchEvent(event);
				mOnListViewEventListener.upDownEvent(false);
			}else{
				if(mOnListViewEventListener == null)
					return super.onTouchEvent(event);
				mOnListViewEventListener.upDownEvent(true);
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	private void obtainVelocityTracker(MotionEvent event) {
        if (vTracker == null) {
        		vTracker = VelocityTracker.obtain();
        }
        vTracker.addMovement(event);
	}
	
	public interface OnListViewEventListener{
		public void upDownEvent(boolean isUp);
	}
	
	private OnListViewEventListener mOnListViewEventListener;
	
	public void setOnListViewEventListener(OnListViewEventListener onListViewEventListener){
		this.mOnListViewEventListener = onListViewEventListener;
	}

}
