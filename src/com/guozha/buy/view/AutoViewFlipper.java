package com.guozha.buy.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;

/**
 * 自定播放图片View
 * @author lixiaoqiang
 *
 */
public class AutoViewFlipper extends FrameLayout{
	private static final int SLOP_DELAY_TIME = 3000; //滑动等待时间，单位ms
	private ViewFlipper mViewFlipper;       //滑动的视图
	private View mPointBar;					//指示点显示条
	private int mItemCount;  				//条目数
	private int mCurrentItem;				//当前的条目
	private int mTouchSlop;					//有效最短滑动距离
	private Context context;
	private List<ImageView> points = new ArrayList<ImageView>();
	private List<ImageView> mImageViews = null;
	
	private TimerTask mTimerTask;
	
	private static final int HANDLER_SLOP_LEFT = 0x0001;
	private Handler hander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_SLOP_LEFT:
				slopToLeft();
				break;
			}
		};
	};

	public AutoViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		mTouchSlop = ViewConfiguration.getTouchSlop();
		
		addChild(context);
		
		startAutoPlay();
	}
	
	/**
	 * 停止自动播放
	 */
	public void stopAutoPlay(){
		if(mTimerTask == null) return;
		mTimerTask.cancel();
		mTimerTask = null;
	}
	
	/**
	 * 开始自动播放
	 */
	public void startAutoPlay(){
		if(mTimerTask != null) return;
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				hander.sendEmptyMessage(HANDLER_SLOP_LEFT);
			}
		};
		new Timer().scheduleAtFixedRate(mTimerTask, SLOP_DELAY_TIME, SLOP_DELAY_TIME);
	}
	
	/**
	 * 添加子视图
	 * @param context
	 */
	private void addChild(Context context){
		
		mViewFlipper = getViewFlipper(context);
		this.addView(mViewFlipper);
		
		mPointBar = getPointBar(context);
		this.addView(mPointBar);
	}
	
	/**
	 * 获取ViewFlipper
	 * @param context
	 * @return
	 */
	private ViewFlipper getViewFlipper(Context context){
		ViewFlipper viewFlipper = new ViewFlipper(context);
		addImageViews(context, viewFlipper);
		return viewFlipper;
	}
	
	/**
	 * 获取指示点显示条
	 * @param context
	 * @return
	 */
	private View getPointBar(Context context){
		LinearLayout.LayoutParams pointBarParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		pointBarParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		LinearLayout pointBar = new LinearLayout(context);
		
		pointBar.setOrientation(LinearLayout.HORIZONTAL);
		pointBar.setLayoutParams(pointBarParams);
		pointBar.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		
		addPoints(context, pointBar);
		return pointBar;
	}
	
	/**
	 * 添加小圆点
	 * @param context
	 * @param pointBar
	 */
	private void addPoints(Context context, LinearLayout pointBar){
		LinearLayout.LayoutParams pointViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		pointViewParams.setMargins(DimenUtil.dp2px(context, 8), 0,
				DimenUtil.dp2px(context, 8), DimenUtil.dp2px(context, 8));
		
		ImageView pointView;
		for(int i = 0; i < mItemCount; i++){
			pointView = new ImageView(context);
			pointView.setScaleType(ScaleType.CENTER_INSIDE);
			pointView.setLayoutParams(pointViewParams);
			points.add(pointView);
			pointBar.addView(pointView);
		}
		setPointColorByCurrentItem();
	}
	
	/**
	 * 获取当前条目的下标
	 * @return
	 */
	public int getCurrentItemIndex(){
		return mCurrentItem;
	}
	
	/**
	 * 根据当前选中项来设置圆点
	 */
	private void setPointColorByCurrentItem(){
		mCurrentItem = (Integer)mViewFlipper.getCurrentView().getTag();
		Bitmap grayPointBitmap = getGrayPointBitmap(context);
		Bitmap lightPointBitmap = getLightPointBitmap(context);
		ImageView imageView;
		for(int i = 0; i < points.size(); i++){
			imageView = points.get(i);
			if(mCurrentItem == i){
				imageView.setImageBitmap(lightPointBitmap);
			}else{
				imageView.setImageBitmap(grayPointBitmap);
			}

		}
		if(mOnSlopTouchListener != null){
			mOnSlopTouchListener.scrollChanged(mCurrentItem);
		}
	}
	
	/**
	 * 添加图片资源
	 * @param context
	 * @param viewFlipper
	 */
	private void addImageViews(Context context, ViewFlipper viewFlipper){
		if(viewFlipper == null) return;
		List<Bitmap> bitmaps = getBitmaps();
		
		if(bitmaps == null) return;
		LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		
		mImageViews = new ArrayList<ImageView>();
		ImageView imageView;
		mItemCount = bitmaps.size();
		for(int i = 0; i < mItemCount; i++){
			imageView = new ImageView(context);
			imageView.setImageBitmap(bitmaps.get(i));
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(imageViewParams);
			imageView.setTag(i);
			viewFlipper.addView(imageView);
			mImageViews.add(imageView);
		}	
	}
	
	/**
	 * 获取ImageView对象
	 * @return
	 */
	public List<ImageView> getImageViews(){
		return mImageViews;
	}
	
	/**
	 * 设置图片资源
	 * @param bitmaps
	 */
	public void setImage(Bitmap bitmap, int position){
		if(mImageViews == null) return;
		mImageViews.get(position).setImageBitmap(bitmap);
	}
	
	//13761015601
	
	/**
	 * 获取图片资源
	 * @return
	 */
	private List<Bitmap> getBitmaps(){
		//TODO 从网络获取图片
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();	
		
		bitmaps.add(BitmapFactory.decodeResource(
				getResources(), R.drawable.default_icon_large));
		bitmaps.add(BitmapFactory.decodeResource(
				getResources(), R.drawable.default_icon_large));
		bitmaps.add(BitmapFactory.decodeResource(
				getResources(), R.drawable.default_icon_large));
		
		return bitmaps;
	}
	
	/**
	 * 获取选择圆点图片
	 * @param context
	 * @return
	 */
	private Bitmap getLightPointBitmap(Context context){
		return BitmapFactory.decodeResource(
				getResources(), R.drawable.main_plan_round_current);
	}
	
	/**
	 * 获取灰色圆点图片
	 * @param context
	 * @return
	 */
	private Bitmap getGrayPointBitmap(Context context){
		return BitmapFactory.decodeResource(
				getResources(), R.drawable.main_plan_round_normal);
	}
	
	
	private float mDownX;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float eventX = event.getX();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = eventX;
			break;
		case MotionEvent.ACTION_UP:
			
			float gap = eventX - mDownX;
			if(Math.abs(gap) > mTouchSlop){
				if(gap > 0){
					//向右滑动
					slopToRight();
				}else{
					//向左滑动
					slopToLeft();
				}
			}
			break;
		}
		
		return true;
	}
	
	/**
	 * 向右滑动
	 */
	private void slopToRight(){
		mViewFlipper.setInAnimation(context, R.anim.slide_in_left);
		mViewFlipper.setOutAnimation(context, R.anim.slide_out_right);
		mViewFlipper.showPrevious();
		setPointColorByCurrentItem();
	}
	
	/**
	 * 向左滑动
	 */
	private void slopToLeft(){
		mViewFlipper.setInAnimation(context, R.anim.slide_in_right);
		mViewFlipper.setOutAnimation(context, R.anim.slide_out_left);
		mViewFlipper.showNext();
		setPointColorByCurrentItem();
	}
	
	public void slopToCenter(){
		if(mCurrentItem == 0){
			slopToLeft();
		}
		if(mCurrentItem == 2){
			slopToRight();
		}
	}
	
	private OnSlopTouchListener mOnSlopTouchListener;
	
	/**
	 * 监听滑动等事件
	 * @author Administrator
	 *
	 */
	public interface OnSlopTouchListener{
		
		public void scrollChanged(int position);
		
		/**
		 * touch事件响应
		 */
		public void onTouchedView();
	}
	
	/**
	 * 设置滑动等事件的监听
	 * @param onSlopTouchListener
	 */
	public void setOnSlopTouchListener(OnSlopTouchListener onSlopTouchListener){
		this.mOnSlopTouchListener = onSlopTouchListener;
	}
}
