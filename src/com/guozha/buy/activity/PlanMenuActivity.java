package com.guozha.buy.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 计划一周菜谱
 * @author PeggyTong
 *
 */
public class PlanMenuActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "PlanMenuPage";
	
	//7个选择按钮
	private Button mButton1;
	private Button mButton2;
	private Button mButton3;
	private Button mButton4;
	private Button mButton5;
	private Button mButton6;
	private Button mButton7;
	
	private ViewFlipper mViewFlipper;
	private MenuFlipperClick mMenuFlipperClick;
	
	private int mCurrentIndex;
	
	private List<ImageView> points = new ArrayList<ImageView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planmenu);
		mMenuFlipperClick = new MenuFlipperClick();
		initView();
		getPointBar();
		mCurrentIndex = 0;//开始默认选中第一个
	}
	
	/**
	 * 初始化View视图
	 */
	private void initView(){
		//设置Tag
		mButton1 = (Button) findViewById(R.id.plantmenu_choose_button1);
		mButton1.setTag(0);
		mButton2 = (Button) findViewById(R.id.plantmenu_choose_button2);
		mButton2.setTag(1);
		mButton3 = (Button) findViewById(R.id.plantmenu_choose_button3);
		mButton3.setTag(2);
		mButton4 = (Button) findViewById(R.id.plantmenu_choose_button4);
		mButton4.setTag(3);
		mButton5 = (Button) findViewById(R.id.plantmenu_choose_button5);
		mButton5.setTag(4);
		mButton6 = (Button) findViewById(R.id.plantmenu_choose_button6);
		mButton6.setTag(5);
		mButton7 = (Button) findViewById(R.id.plantmenu_choose_button7);
		mButton7.setTag(6);
		//设置监听事件
		mButton1.setOnClickListener(mMenuFlipperClick);
		mButton2.setOnClickListener(mMenuFlipperClick);
		mButton3.setOnClickListener(mMenuFlipperClick);
		mButton4.setOnClickListener(mMenuFlipperClick);
		mButton5.setOnClickListener(mMenuFlipperClick);
		mButton6.setOnClickListener(mMenuFlipperClick);
		mButton7.setOnClickListener(mMenuFlipperClick);
		
		mViewFlipper = (ViewFlipper) findViewById(R.id.planmenu_content_view);
	}
	
	/**
	 * 添加指示点显示条
	 * @param context
	 * @return
	 */
	private View getPointBar(){
		LinearLayout pointBar = 
				(LinearLayout) findViewById(R.id.plantmenu_point_bar);
		addPoints(pointBar); //添加小圆点
		return pointBar;
	}
	
	/**
	 * 添加小圆点
	 * @param context
	 * @param pointBar
	 */
	private void addPoints(LinearLayout pointBar){
		LinearLayout.LayoutParams pointViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		pointViewParams.setMargins(DimenUtil.dp2px(this, 8), 0,
				DimenUtil.dp2px(this, 8), DimenUtil.dp2px(this, 8));
		
		ImageView pointView;
		for(int i = 0; i < 7; i++){
			pointView = new ImageView(this);
			pointView.setScaleType(ScaleType.CENTER_INSIDE);
			pointView.setLayoutParams(pointViewParams);
			points.add(pointView);
			pointBar.addView(pointView);
		}
		setPointColorByCurrentItem();
	}
	
	/**
	 * 根据当前选中项来设置圆点
	 */
	private void setPointColorByCurrentItem(){
		Bitmap grayPointBitmap = getGrayPointBitmap(this);
		Bitmap lightPointBitmap = getLightPointBitmap(this);
		ImageView imageView;
		for(int i = 0; i < points.size(); i++){
			imageView = points.get(i);
			if(mCurrentIndex == i){
				imageView.setImageBitmap(lightPointBitmap);
			}else{
				imageView.setImageBitmap(grayPointBitmap);
			}
		}
	}
	
	/**
	 * 获取选择圆点图片
	 * @param context
	 * @return
	 */
	private Bitmap getLightPointBitmap(Context context){
		return BitmapFactory.decodeResource(
				getResources(), R.drawable.main_page_scorll_point_selected);
	}
	
	/**
	 * 获取灰色圆点图片
	 * @param context
	 * @return
	 */
	private Bitmap getGrayPointBitmap(Context context){
		return BitmapFactory.decodeResource(
				getResources(), R.drawable.main_page_scroll_point_unselected);
	}
	
	class MenuFlipperClick implements OnClickListener{

		@Override
		public void onClick(View view) {
			int index = (Integer) view.getTag();
			if(index < mCurrentIndex){
				slopToRight(index);
			}else if(index > mCurrentIndex){
				slopToLeft(index);
			}
			mCurrentIndex = index;
			setPointColorByCurrentItem();
		}
		
	}
	
	/**
	 * 向右滑动
	 */
	private void slopToRight(int index){
		mViewFlipper.setInAnimation(this, R.anim.slide_in_left);
		mViewFlipper.setOutAnimation(this, R.anim.slide_out_right);
		//mViewFlipper.showPrevious();
		mViewFlipper.setDisplayedChild(index);
	}
	
	/**
	 * 向左滑动
	 */
	private void slopToLeft(int index){
		mViewFlipper.setInAnimation(this, R.anim.slide_in_right);
		mViewFlipper.setOutAnimation(this, R.anim.slide_out_left);
		mViewFlipper.setDisplayedChild(index);
		//mViewFlipper.showNext();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//友盟页面统计代码
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//友盟页面统计代码
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
	
}
