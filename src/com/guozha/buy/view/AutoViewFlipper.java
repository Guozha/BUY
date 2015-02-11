package com.guozha.buy.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;

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

/**
 * �Զ�����ͼƬView
 * @author lixiaoqiang
 *
 */
public class AutoViewFlipper extends FrameLayout{
	private static final int SLOP_DELAY_TIME = 3000; //�����ȴ�ʱ�䣬��λms
	private ViewFlipper mViewFlipper;       //��������ͼ
	private View mPointBar;					//ָʾ����ʾ��
	private int mItemCount;  				//��Ŀ��
	private int mCurrentItem;				//��ǰ����Ŀ
	private int mTouchSlop;					//��Ч��̻�������
	private Context context;
	private List<ImageView> points = new ArrayList<ImageView>();
	
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
	 * ֹͣ�Զ�����
	 */
	public void stopAutoPlay(){
		if(mTimerTask == null) return;
		mTimerTask.cancel();
		mTimerTask = null;
	}
	
	/**
	 * ��ʼ�Զ�����
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
	 * �������ͼ
	 * @param context
	 */
	private void addChild(Context context){
		
		mViewFlipper = getViewFlipper(context);
		this.addView(mViewFlipper);
		
		mPointBar = getPointBar(context);
		this.addView(mPointBar);
	}
	
	/**
	 * ��ȡViewFlipper
	 * @param context
	 * @return
	 */
	private ViewFlipper getViewFlipper(Context context){
		ViewFlipper viewFlipper = new ViewFlipper(context);
		addImageViews(context, viewFlipper);
		return viewFlipper;
	}
	
	/**
	 * ��ȡָʾ����ʾ��
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
	 * ���СԲ��
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
	 * ���ݵ�ǰѡ����������Բ��
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
	}
	
	/**
	 * ���ͼƬ��Դ
	 * @param context
	 * @param viewFlipper
	 */
	private void addImageViews(Context context, ViewFlipper viewFlipper){
		if(viewFlipper == null) return;
		List<Bitmap> bitmaps = getBitmaps();
		
		if(bitmaps == null) return;
		LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		
		ImageView imageView;
		mItemCount = bitmaps.size();
		for(int i = 0; i < mItemCount; i++){
			imageView = new ImageView(context);
			imageView.setImageBitmap(bitmaps.get(i));
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(imageViewParams);
			imageView.setTag(i);
			viewFlipper.addView(imageView);
		}	
	}
	
	/**
	 * ��ȡͼƬ��Դ
	 * @return
	 */
	private List<Bitmap> getBitmaps(){
		//TODO �������ȡͼƬ
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();	
		
		bitmaps.add(BitmapFactory.decodeResource(
				getResources(), R.drawable.main_page_scroll_image1));
		bitmaps.add(BitmapFactory.decodeResource(
				getResources(), R.drawable.main_page_scroll_image2));
		bitmaps.add(BitmapFactory.decodeResource(
				getResources(), R.drawable.main_page_scroll_image3));
		
		return bitmaps;
	}
	
	/**
	 * ��ȡѡ��Բ��ͼƬ
	 * @param context
	 * @return
	 */
	private Bitmap getLightPointBitmap(Context context){
		return BitmapFactory.decodeResource(
				getResources(), R.drawable.main_page_scorll_point_selected);
	}
	
	/**
	 * ��ȡ��ɫԲ��ͼƬ
	 * @param context
	 * @return
	 */
	private Bitmap getGrayPointBitmap(Context context){
		return BitmapFactory.decodeResource(
				getResources(), R.drawable.main_page_scroll_point_unselected);
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
					//���һ���
					slopToRight();
				}else{
					//���󻬶�
					slopToLeft();
				}
			}
			break;
		}
		
		return true;
	}
	
	/**
	 * ���һ���
	 */
	private void slopToRight(){
		mViewFlipper.setInAnimation(context, R.anim.slide_in_left);
		mViewFlipper.setOutAnimation(context, R.anim.slide_out_right);
		mViewFlipper.showPrevious();
		setPointColorByCurrentItem();
	}
	
	/**
	 * ���󻬶�
	 */
	private void slopToLeft(){
		mViewFlipper.setInAnimation(context, R.anim.slide_in_right);
		mViewFlipper.setOutAnimation(context, R.anim.slide_out_left);
		mViewFlipper.showNext();
		setPointColorByCurrentItem();
	}
	
	private OnSlopTouchListener mOnSlopTouchListener;
	
	/**
	 * �����������¼�
	 * @author Administrator
	 *
	 */
	interface OnSlopTouchListener{
		
		/**
		 * touch�¼���Ӧ
		 */
		public void onTouchedView();
	}
	
	/**
	 * ���û������¼��ļ���
	 * @param onSlopTouchListener
	 */
	public void setOnSlopTouchListener(OnSlopTouchListener onSlopTouchListener){
		this.mOnSlopTouchListener = onSlopTouchListener;
	}
}
