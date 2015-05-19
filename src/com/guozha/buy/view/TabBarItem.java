package com.guozha.buy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.LogUtil;

/**
 * TabBar的按钮控件
 * @author PeggyTong
 *
 */
public class TabBarItem extends LinearLayout{
	
	private static final int ICON_HEIGHT = 32;
	
	private boolean isChoosed;
	private String mTextContent;
	private int mTextSize;
	private int mTextNomalColor;
	private int mTextCheckColor;
	private Bitmap mNomalIcon;
	private Bitmap mCheckIcon;
	
	private ImageView mBtnImage;
	private TextView mBtnText;
	
	public TabBarItem(Context context) {
		super(context);
	}

	public TabBarItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initTabItem(context, attrs);
		
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		
		mBtnImage = new ImageView(context);
		mBtnImage.setScaleType(ScaleType.CENTER_INSIDE);
		mBtnImage.setLayoutParams(
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, DimenUtil.dp2px(context, ICON_HEIGHT)));
		
		mBtnText = new TextView(context);
		mBtnText.setText(mTextContent);
		LogUtil.e("TextSize == " + mTextSize);
		//TODO 
		mBtnText.setTextSize(14);
		mBtnText.setLayoutParams(
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addView(mBtnImage);
		addView(mBtnText);
		
		changeCheckedStatus();

	}

	public TabBarItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	/**
	 * 改变当前显示状态
	 */
	private void changeCheckedStatus() {
		if(isChoosed){
			mBtnImage.setImageBitmap(mCheckIcon);
			mBtnText.setTextColor(mTextCheckColor);
		}else{
			mBtnImage.setImageBitmap(mNomalIcon);
			mBtnText.setTextColor(mTextNomalColor);
		}
	}
	
	/**
	 * 设置为选中状态
	 */
	public void setCheckedItem(){
		isChoosed = true;
		changeCheckedStatus();
	}
	
	/**
	 * 设置为未选中状态
	 */
	public void setDisCheckedItem(){
		isChoosed = false;
		changeCheckedStatus();
	}
	
	private void initTabItem(Context context, AttributeSet attrs){
		TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.TabBarItem);
		int count = typeArr.getIndexCount();
		for(int i = 0; i < count; i++){
			int attr = typeArr.getIndex(i);
			switch(attr){
			case R.styleable.TabBarItem_checked_item:
				isChoosed = typeArr.getBoolean(attr, false);
				break;
			case R.styleable.TabBarItem_text:
				mTextContent = typeArr.getString(attr);
				break;
			case R.styleable.TabBarItem_text_size:
				mTextSize = (int) typeArr.getDimension(attr, TypedValue.applyDimension
						(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
				break;
			case R.styleable.TabBarItem_nomal_color:
				//TODO 这里默认颜色改一下
				mTextNomalColor = typeArr.getColor(attr, getResources().getColor(R.color.color_app_base_1));
				break;
			case R.styleable.TabBarItem_check_color:
				//TODO 这里默认颜色改一下
				mTextCheckColor = typeArr.getColor(attr, getResources().getColor(R.color.color_app_base_1));
				break;
			case R.styleable.TabBarItem_nomal_icon:
				mNomalIcon = ((BitmapDrawable)typeArr.getDrawable(attr)).getBitmap();
				break;
			case R.styleable.TabBarItem_check_icon:
				mCheckIcon = ((BitmapDrawable)typeArr.getDrawable(attr)).getBitmap();
				break;
			}
		}
		typeArr.recycle();
	}
}
