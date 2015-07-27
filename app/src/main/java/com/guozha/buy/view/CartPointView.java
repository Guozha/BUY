package com.guozha.buy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.util.DimenUtil;

public class CartPointView extends View{
	private Paint mTextNumPaint;
	private Paint mCirclePaint;
	private Rect mTextNumBound;
	private String mTextNum = "8";
	public CartPointView(Context context) {
		this(context, null);
	}
	
	public CartPointView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CartPointView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	private void initView(Context context){
		mTextNumPaint = new Paint();
		mCirclePaint = new Paint();
		mTextNumBound = new Rect();
		
		mTextNumPaint.setColor(0Xffffffff);
		mTextNumPaint.setAntiAlias(true);
		mTextNumPaint.setTextSize(DimenUtil.dp2px(context, 10));
		
		mCirclePaint = new Paint();
		mCirclePaint.setColor(0Xffdf3411);
		mCirclePaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int cartNumber = ConfigManager.getInstance().getCartNumber();
		if(cartNumber <= 0) return;
		mTextNum = String.valueOf(cartNumber);
		drawNumText(canvas);
	}
	
	private void drawNumText(Canvas canvas){
		mTextNumPaint.getTextBounds(mTextNum, 0, mTextNum.length(), mTextNumBound);
		int x = getMeasuredWidth() / 2 + mTextNumBound.width();
		int y = getMeasuredHeight() / 3;
		
		canvas.drawCircle(x + mTextNumBound.width() / 2, y - mTextNumBound.height(), mTextNumBound.height(), mCirclePaint);
		canvas.drawText(mTextNum, x, y - mTextNumBound.height() / 2, mTextNumPaint);
	}

}
