package com.guozha.buy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;

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
		this(context, null);
	}

	public TabBarItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TabBarItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initTabBarItem(context, attrs);
	}

	/**
	 * 初始化
	 * @param context
	 * @param attrs
	 */
	private void initTabBarItem(Context context, AttributeSet attrs) {
		initTabItem(context, attrs);  //获取尺寸
		addChilds(context);			  //添加子view
		changeCheckedStatus();		  //根据状态显示当前view
	}

	/**
	 * 添加子控件
	 * @param context
	 */
	private void addChilds(Context context) {
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		
		mBtnImage = new ImageView(context);
		mBtnImage.setScaleType(ScaleType.CENTER_INSIDE);
		mBtnImage.setLayoutParams(
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, DimenUtil.dp2px(context, ICON_HEIGHT)));
		
		mBtnText = new TextView(context);
		mBtnText.setText(mTextContent);
		mBtnText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
		mBtnText.setLayoutParams(
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addView(mBtnImage);
		addView(mBtnText);
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
	
	/**
	 * 初始配置的数据
	 * @param context
	 * @param attrs
	 */
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
				mTextNomalColor = typeArr.getColor(attr, getResources().getColor(R.color.color_app_base_2));
				break;
			case R.styleable.TabBarItem_check_color:
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
