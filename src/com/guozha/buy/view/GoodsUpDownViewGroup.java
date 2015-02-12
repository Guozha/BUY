package com.guozha.buy.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.LogUtil;

/**
 * 主视图可上下拉的视图组
 * @author Administrator
 *
 */
public class GoodsUpDownViewGroup extends ViewGroup{
	
	private static final String TAG = "MainUpDownViewGroup";
	private static int BOTTOM_MENU_HEIGHT;
	
	private static int mViewPointY;
	private int mViewHeight;
	private int mViewWidth;
	
	private View mCenterView;
	
	private Context context;
	
	private Scroller mScroller;

	public GoodsUpDownViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		addChild();

		setWillNotDraw(false);
		mScroller = new Scroller(context);
		BOTTOM_MENU_HEIGHT = DimenUtil.dp2px(context, 50);
	}
	
	private void addChild(){
		addCenterView();
	}
	
	/**
	 * 添加视图
	 */
	private void addCenterView(){
		View view = new ShopCarDownBar(context);
		view.setBackgroundColor(Color.YELLOW);
		mCenterView = view;
		addView(mCenterView);
	}
	
	/**
	 * 设置为初始状态
	 */
	public void setOriginStatus(){
		moveToBottom();
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mCenterView.layout(0, 0, mViewWidth, mViewHeight);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewPointY = DimenUtil.getViewOriginPoint(mCenterView)[1];
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mViewHeight = h;
		mViewWidth = w;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		//setOriginStatus();
	}
	
	private float mOldY;
	private VelocityTracker vTracker;
	private boolean invalidateEvent;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		LogUtil.e("onTouchEvent");
		int disY;
		int vTrackY;
		float eventY = event.getY();
		obtainVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mOldY = eventY;
			if(mOldY > mViewHeight - BOTTOM_MENU_HEIGHT || mOldY < BOTTOM_MENU_HEIGHT * 2){
				invalidateEvent = true;
				return true;
			}else{
				invalidateEvent = false;
				return super.onTouchEvent(event);
			}
		case MotionEvent.ACTION_MOVE:	
			if(!invalidateEvent) return super.onTouchEvent(event);
			disY = (int) (eventY - mOldY);
			if(disY > 0 && DimenUtil.getViewOriginPoint(mCenterView)[1] 
					>= mViewHeight){
				return true;
			}
			if(disY < 0 && DimenUtil.getViewOriginPoint(mCenterView)[1] <= mViewPointY){
				return true;
			}
			mOldY = eventY;
			scrollBy(0, -disY);
			return true;
		case MotionEvent.ACTION_UP:
			if(!invalidateEvent) return super.onTouchEvent(event);
			vTracker.computeCurrentVelocity(1000);
			vTrackY = (int) vTracker.getYVelocity();
			Log.e(TAG, "vTrack = " + vTrackY);
			if(Math.abs(vTrackY) > 6000){
				if(vTrackY > 0){
					moveToBottom();
				}else{
					moveToTop();
				}
			}else{
				int disPointY = DimenUtil.getViewOriginPoint(mCenterView)[1] - mViewPointY;
				if(disPointY > mViewHeight / 2){
					moveToBottom();
				}else{
					moveToTop();
				}
			}
			return true;
		default:
			return super.onTouchEvent(event);
		}
	}
	
	private void obtainVelocityTracker(MotionEvent event) {
        if (vTracker == null) {
        		vTracker = VelocityTracker.obtain();
        }
        vTracker.addMovement(event);
	}
	
	private void centerViewUpdate(){
		mCenterView.postInvalidate();
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
		centerViewUpdate();
	}

	/**
	 * 向下移动
	 */
	private void moveToBottom(){
		int dy = DimenUtil.getViewOriginPoint(mCenterView)[1] - 
				mViewHeight - mViewPointY + BOTTOM_MENU_HEIGHT;
		mScroller.startScroll(0, getScrollY(), 0, dy);
		invalidate();
	}
	
	/**
	 * 向上移动
	 */
	private void moveToTop(){
		int dy = DimenUtil.getViewOriginPoint(mCenterView)[1] - mViewPointY;
		mScroller.startScroll(0, getScrollY(), 0, dy);
		invalidate();
	}

}



