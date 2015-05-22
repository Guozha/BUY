package com.guozha.buy.view;

import com.guozha.buy.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 
 * @author 李小强
 *
 */
public class ViewPagerTab extends ViewGroup{
	
	private ViewPager mViewPager;
	private PageListener mPageListener = new PageListener();
	private Context mContext;
	
	private int mWidth;
	private int mHeight;
	private Scroller mScroller; 
	private int mTabNum;

	public ViewPagerTab(Context context, AttributeSet attrs) {
		super(context, attrs);	
		this.mContext = context;
		mScroller = new Scroller(mContext);  
		initAttr(context, attrs);
	}
	
	private void initAttr(Context context, AttributeSet attrs){
		TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerTab);
		mTabNum = typeArr.getInt(typeArr.getIndex(0), 2);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(getChildCount() > 0){
			getChildAt(0).layout(0, 0, mWidth / mTabNum, mHeight);  
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = MeasureSpec.getSize(widthMeasureSpec);  
        mHeight = MeasureSpec.getSize(heightMeasureSpec); 
	}
	
	public void setViewPager(ViewPager viewPager){
		this.mViewPager = viewPager;
		mViewPager.setOnPageChangeListener(mPageListener);
	}

	@Override
	public void computeScroll() {
		super.computeScroll();  
        if(mScroller.computeScrollOffset()){  
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
            postInvalidate();  
        }  
	}
	
	
	private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			scrollTo(- position * mWidth / mTabNum - Math.round(positionOffset * mWidth / mTabNum), 0);
		}

		@Override
		public void onPageSelected(int position) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
	
		}

	}
}
