package com.guozha.buy.view;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.util.LogUtil;

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
	private List<TextView> mTabTexts;
	private Resources mResource;
	private int mCurrentChoosed;
	public ViewPagerTab(Context context, AttributeSet attrs) {
		super(context, attrs);	
		this.mContext = context;
		mResource = context.getResources();
		mScroller = new Scroller(mContext);  
		//initAttr(context, attrs);
		mTabNum = 2;
		setUpTab(context);
	}
	
	private void setUpTab(Context context){
		ImageView childView = new ImageView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		childView.setImageResource(R.drawable.tabline_1px_red);
		childView.setScaleType(ScaleType.FIT_XY);
		childView.setLayoutParams(params);
		addView(childView);
	}
	
	//private void initAttr(Context context, AttributeSet attrs){
	//	TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerTab);
	//	mTabNum = typeArr.getInt(typeArr.getIndex(0), 2);
	//}
	
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
	
	public void setViewPageAndTabTexts(ViewPager viewPager, List<TextView> tabTexts){
		setViewPage(viewPager);
		setTabTexts(tabTexts);
	}
	
	private void setViewPage(ViewPager viewPager){
		this.mViewPager = viewPager;
		mViewPager.setOnPageChangeListener(mPageListener);
		mCurrentChoosed = 0;
	}
	
	private void setTabTexts(List<TextView> tabTexts){
		if(tabTexts == null || tabTexts.size() < 2) return;
		mTabTexts = tabTexts;
		mTabNum = mTabTexts.size();
		for(int i = 0; i < mTabTexts.size(); i++){
			final int position = i;
			mTabTexts.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(position);
				}
			});
		}
		setTabChoosedColor(mCurrentChoosed);
	}
	
	private boolean setTabChoosedColor(int position){
		if(mTabTexts == null) return false;
		TextView textView = mTabTexts.get(position);
		if(textView == null) return false;
		textView.setTextColor(getResources().getColor(R.color.color_app_base_21));
		return true;
	}
	
	private boolean setTabNomalColor(int position){
		if(mTabTexts == null) return false;
		TextView textView = mTabTexts.get(position);
		if(textView == null) return false;
		textView.setTextColor(getResources().getColor(R.color.color_app_base_22));
		return true;
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
			if(position == mCurrentChoosed) return;
			if(setTabNomalColor(mCurrentChoosed) &&
					setTabChoosedColor(position)) mCurrentChoosed = position;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
	
		}

	}
}
