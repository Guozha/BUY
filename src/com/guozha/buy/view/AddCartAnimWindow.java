package com.guozha.buy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;

public class AddCartAnimWindow extends View{
	
	private int mXGAP;
	private int mYGAP;
	
	public static int maxWindowDimen;
	private static int minWindowDimen;
	private static final int SPEED = 20;
	private static int X_SPEED;
	private static int Y_SPEED;
	private String mDrawText = "+1";
	
	private WindowManager.LayoutParams mParams;
	private WindowManager mWindowManager;
	private int mTextSize;
	private int mPointX;
	private int mPointY;
	private Paint circlePaint;
	private Paint textPaint;
	private Status mStatus;
	private Rect mTextBound;
	private float mCurrentDimen;
	
	private int mScreenWidth;
	private int mScreenHeight;
	private boolean mMoveable = true;

	public AddCartAnimWindow(String showText, WindowManager windowManager, Context context) {
		super(context);
		if(!"+1".equals(showText)){
			mMoveable = false;
		}
		mWindowManager = windowManager;
		mStatus = new ChangeBigStatus();
		circlePaint = new Paint();
		circlePaint.setColor(context.getResources().getColor(R.color.color_app_base_33));
		circlePaint.setAntiAlias(true);
		textPaint = new Paint();
		textPaint.setColor(context.getResources().getColor(R.color.color_app_base_24));
		textPaint.setAntiAlias(true);
		mTextSize = DimenUtil.dp2px(context, 14);
		textPaint.setTextSize(mTextSize);
		mDrawText = showText;
		mTextBound = new Rect();
		textPaint.getTextBounds(mDrawText, 0, mDrawText.length(), mTextBound);
		
		maxWindowDimen = DimenUtil.dp2px(context, 120);
		minWindowDimen = DimenUtil.dp2px(context, 18);
		
		mScreenWidth = DimenUtil.getScreenWidth(context);
		mScreenHeight = DimenUtil.getScreenHeight(context);
		mXGAP = DimenUtil.dp2px(context, 23);
		mYGAP = DimenUtil.dp2px(context, 23);
		mPointX = mScreenWidth / 2;
		mPointY = mScreenHeight / 2;
		mCurrentDimen = mTextSize + 10;
		
		X_SPEED = (int)((float)mScreenWidth / 2 / SPEED);
		Y_SPEED = (int)((float)mScreenHeight / 2 / SPEED);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mStatus.draw(canvas);
	}
	
	abstract class Status{
		
		abstract public void draw(Canvas canvas);
		
		abstract public void toNextStatus();
		
	}

	class ChangeBigStatus extends Status{
		@Override
		public void draw(Canvas canvas) {
			if(mCurrentDimen >= maxWindowDimen) {
				mCurrentDimen = maxWindowDimen;
				toNextStatus();
			}
			canvas.drawCircle(mPointX, mPointY, mCurrentDimen / 2, circlePaint);
			if(mCurrentDimen > mTextBound.width()){
			canvas.drawText(mDrawText, mPointX - mTextBound.width() / 2, mPointY + mTextBound.height() / 2, textPaint);
			}
			if(mCurrentDimen < maxWindowDimen) {
				postInvalidateDelayed(30);
				mCurrentDimen = mCurrentDimen * 1.2f;
			}
		}

		@Override
		public void toNextStatus() {
			mStatus = new ChangeMinStatus();
			postInvalidateDelayed(500);
		}
	}
	
	class ChangeMinStatus extends Status{
		private int drawCount = 0;
		@Override
		public void draw(Canvas canvas) {
			if(mCurrentDimen <= minWindowDimen) {
				mCurrentDimen = minWindowDimen;
				if(!mMoveable){
					toNextStatus();
				}
			}
			if(mPointX >= mScreenWidth - mXGAP){
				mPointX = mScreenWidth - mXGAP;
			}
			if(mPointY <= mYGAP){
				mPointY = mYGAP;
			}
			canvas.drawCircle(mPointX, mPointY, mCurrentDimen / 2, circlePaint);
			if(mCurrentDimen > mTextBound.width()){
				canvas.drawText(mDrawText, mPointX - mTextBound.width() / 2, mPointY + mTextBound.height() / 2, textPaint);
			}
			
			if(mPointX >= mScreenWidth - mXGAP && mPointY <= mYGAP){
				toNextStatus();
			}else{
				postInvalidateDelayed(15);
				mCurrentDimen = mCurrentDimen * 0.85f;
				if(mMoveable){
					int ySpeed;
					if(drawCount < SPEED / 2){
						ySpeed = Y_SPEED + (SPEED - drawCount * 2);
					}else{
						//ySpeed = Y_SPEED - (drawCount - SPEED / 2) * 5;
						ySpeed = Y_SPEED;
					}
					mPointX = mPointX + X_SPEED;
					mPointY = mPointY - ySpeed;
					drawCount++;
				}
			}
			//updateViewPosition();
		}

		@Override
		public void toNextStatus() {
			if(mOnAnimEndListener != null){
				mOnAnimEndListener.animEnd();
			}
			//mStatus = new PushStatus();
			//postInvalidate();
		}
	}
	
	class PushStatus extends Status{

		@Override
		public void draw(Canvas canvas) {
			if(mPointX < maxWindowDimen - minWindowDimen){
				mPointX = mPointX + X_SPEED;
			}else{
				mPointX = maxWindowDimen - minWindowDimen;
			}
			if(mPointY > minWindowDimen){
				mPointY = mPointY - Y_SPEED;
			}else{
				mPointY = minWindowDimen;
			}
			canvas.drawCircle(mPointX, mPointY, minWindowDimen / 2, circlePaint);
			if(mCurrentDimen > mTextSize){
				canvas.drawText(mDrawText, mPointX - mTextBound.width() / 2, mPointY + mTextBound.height() / 2, textPaint);
			}
			if(mPointX >= maxWindowDimen - minWindowDimen && mPointY >= minWindowDimen){
				toNextStatus();
			}else{
				postInvalidateDelayed(10, mPointX, mPointY, maxWindowDimen, maxWindowDimen);
			}
		}

		@Override
		public void toNextStatus() {
			if(mOnAnimEndListener != null){
				mOnAnimEndListener.animEnd();
			}
		}
	}
	
	private OnAnimEndListener mOnAnimEndListener;
	
	public void setOnAnimEndListener(OnAnimEndListener animEndListener){
		mOnAnimEndListener = animEndListener;
	}
	
	public interface OnAnimEndListener{
		public void animEnd();
	}
	
	/**
	 * 更新参数移动位置
	 */
	private void updateViewPosition(){
		//int gap = (int) Math.floor(maxWindowDimen - mCurrentDimen) / 2;
		//int currentDimen = (int) Math.ceil(mCurrentDimen);
		if(mParams.x < mScreenWidth){
			mParams.x = mParams.x + X_SPEED;
		}else{
			mParams.x = mScreenWidth;
		}
		
		if(mParams.y > 0){
			mParams.y = mParams.y - Y_SPEED;
		}else{
			mParams.y = 0;
		}
		
		//mParams.width = currentDimen;
		//mParams.height = currentDimen;
		mWindowManager.updateViewLayout(this, mParams);
	}
	
    /**
     * 将参数传入
     * @param params
     */
    public void setParams(WindowManager.LayoutParams params){
    	mParams = params;
    }
}
