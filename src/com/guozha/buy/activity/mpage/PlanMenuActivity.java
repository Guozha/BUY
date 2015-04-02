package com.guozha.buy.activity.mpage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.activity.market.SetWarnTimeActivity;
import com.guozha.buy.entry.cart.CartTotalData;
import com.guozha.buy.entry.mpage.plan.PlanMenu;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 计划一周菜谱
 * @author PeggyTong
 *
 */
public class PlanMenuActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "PlanMenuPage";
	private static final int HAND_PLAN_MENU_DATA_COMPLETED = 0x0001;
	
	//7个选择按钮
	private ImageView mButton1;
	private ImageView mButton2;
	private ImageView mButton3;
	private ImageView mButton4;
	private ImageView mButton5;
	private ImageView mButton6;
	private ImageView mButton7;
	
	private ViewFlipper mViewFlipper;
	private MenuFlipperClick mMenuFlipperClick;
	
	private int mCurrentIndex;
	
	private List<PlanMenu> mPlanMenus;
	
	private List<View> mAreas;
	
	private List<ImageView> points = new ArrayList<ImageView>();
	
	private BitmapCache mBitmapCache;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_PLAN_MENU_DATA_COMPLETED:
				initPlanMenuItem();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planmenu);
		mMenuFlipperClick = new MenuFlipperClick();
		customActionBarStyle("菜谱计划");
		initView();
		initPlanMenuData();
		getPointBar();
		mCurrentIndex = 0;//开始默认选中第一个
	}
	
	/**
	 * 初始化View视图
	 */
	private void initView(){
		//设置Tag
		mButton1 = (ImageView) findViewById(R.id.plantmenu_choose_button1);
		mButton1.setTag(0);
		mButton2 = (ImageView) findViewById(R.id.plantmenu_choose_button2);
		mButton2.setTag(1);
		mButton3 = (ImageView) findViewById(R.id.plantmenu_choose_button3);
		mButton3.setTag(2);
		mButton4 = (ImageView) findViewById(R.id.plantmenu_choose_button4);
		mButton4.setTag(3);
		mButton5 = (ImageView) findViewById(R.id.plantmenu_choose_button5);
		mButton5.setTag(4);
		mButton6 = (ImageView) findViewById(R.id.plantmenu_choose_button6);
		mButton6.setTag(5);
		mButton7 = (ImageView) findViewById(R.id.plantmenu_choose_button7);
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
	 * 初始化计划菜谱数据
	 */
	private void initPlanMenuData(){
		String token = ConfigManager.getInstance().getUserToken();
		RequestParam paramPath = new RequestParam("menuplan/list")
		.setParams("token", token);
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
			    mPlanMenus = gson.fromJson(response, new TypeToken<List<PlanMenu>>() { }.getType());
			    if(mPlanMenus != null){
			    	handler.sendEmptyMessage(HAND_PLAN_MENU_DATA_COMPLETED);
			    }
			}
		});
	}
	
	private void initPlanMenuItem(){
		mBitmapCache = new BitmapCache(this, mViewFlipper);
		
		View area1 = findViewById(R.id.planmenu_area1);
		View area2 = findViewById(R.id.planmenu_area2);
		View area3 = findViewById(R.id.planmenu_area3);
		View area4 = findViewById(R.id.planmenu_area4);
		View area5 = findViewById(R.id.planmenu_area5);
		View area6 = findViewById(R.id.planmenu_area6);
		View area7 = findViewById(R.id.planmenu_area7);
		
		setChildItemTag(area1, 0);
		setChildItemTag(area2, 1);
		setChildItemTag(area3, 2);
		setChildItemTag(area4, 3);
		setChildItemTag(area5, 4);
		setChildItemTag(area6, 5);
		setChildItemTag(area7, 6);
		
		mAreas = new ArrayList<View>();
		mAreas.add(area1);
		mAreas.add(area2);
		mAreas.add(area3);
		mAreas.add(area4);
		mAreas.add(area5);
		mAreas.add(area6);
		mAreas.add(area7);
		
		View areaView;
		for(int i = 0; i < mPlanMenus.size(); i++){
			areaView = mAreas.get(i);
			ImageView image1 = (ImageView) areaView.findViewById(R.id.planmenu_detail_line1_item1);
			ImageView image2 = (ImageView) areaView.findViewById(R.id.planmenu_detail_line1_item2);
			ImageView image3 = (ImageView) areaView.findViewById(R.id.planmenu_detail_line1_item3);
			ImageView image4 = (ImageView) areaView.findViewById(R.id.planmenu_detail_line2_item1);
			ImageView image5 = (ImageView) areaView.findViewById(R.id.planmenu_detail_line2_item2);
			ImageView image6 = (ImageView) areaView.findViewById(R.id.planmenu_detail_line2_item3);
			
			CheckBox check1 = (CheckBox) areaView.findViewById(R.id.planmenu_detial_line1_checkbox1);
			CheckBox check2 = (CheckBox) areaView.findViewById(R.id.planmenu_detial_line1_checkbox2);
			CheckBox check3 = (CheckBox) areaView.findViewById(R.id.planmenu_detial_line1_checkbox3);
			CheckBox check4 = (CheckBox) areaView.findViewById(R.id.planmenu_detial_line2_checkbox1);
			CheckBox check5 = (CheckBox) areaView.findViewById(R.id.planmenu_detial_line2_checkbox2);
			CheckBox check6 = (CheckBox) areaView.findViewById(R.id.planmenu_detial_line2_checkbox3);
			
			PlanMenu planMenu = mPlanMenus.get(i);
			
			if(planMenu == null) return;
			String imgUrl = HttpManager.URL + planMenu.getFirstMenuImg();
			image1.setTag(imgUrl);
			mBitmapCache.loadBitmaps(image1, imgUrl);
			
			imgUrl = HttpManager.URL + planMenu.getSecondMenuImg();
			image2.setTag(imgUrl);
			mBitmapCache.loadBitmaps(image2, imgUrl);
			
			imgUrl = HttpManager.URL + planMenu.getThirdMenuImg();
			image3.setTag(imgUrl);
			mBitmapCache.loadBitmaps(image3, imgUrl);
			
			imgUrl = HttpManager.URL + planMenu.getFourtMenuImg();
			image4.setTag(imgUrl);
			mBitmapCache.loadBitmaps(image4, imgUrl);
			
			imgUrl = HttpManager.URL + planMenu.getFiveMenuImg();
			image5.setTag(imgUrl);
			mBitmapCache.loadBitmaps(image5, imgUrl);
			
			imgUrl = HttpManager.URL + planMenu.getSixMenuImg();
			image6.setTag(imgUrl);
			mBitmapCache.loadBitmaps(image6, imgUrl);
		}
	}
	
	private void setChildItemTag(View view, int group){
		MenuItemClickListener listener = new MenuItemClickListener(group);
		view.findViewById(R.id.planmenu_detail_line1_item1).setOnClickListener(listener);
		view.findViewById(R.id.planmenu_detail_line1_item2).setOnClickListener(listener);
		view.findViewById(R.id.planmenu_detail_line1_item3).setOnClickListener(listener);
		view.findViewById(R.id.planmenu_detail_line2_item1).setOnClickListener(listener);
		view.findViewById(R.id.planmenu_detail_line2_item2).setOnClickListener(listener);
		view.findViewById(R.id.planmenu_detail_line2_item3).setOnClickListener(listener);
	}
	
	class MenuItemClickListener implements OnClickListener{
		
		private int groupId;
		public MenuItemClickListener(int groupId){
			this.groupId = groupId;
		}

		@Override
		public void onClick(View view) {
			switch (groupId) {
			case 0:
				on
				break;
			case 1:
				
				break;
			case 2:
				
				break;
			case 3:
				
				break;
			case 4:
				
				break;
			case 5:
				
				break;
			case 6:
				
				break;
			default:
				break;
			}
			
			int childId = -1;
			switch (view.getId()) {
			case R.id.planmenu_detail_line1_item1:
				childId = 0;
				break;
			case R.id.planmenu_detail_line1_item2:
				childId = 1;
				break;
			case R.id.planmenu_detail_line1_item3:
				childId = 2;
				break;
			case R.id.planmenu_detail_line2_item1:
				childId = 3;
				break;
			case R.id.planmenu_detail_line2_item2:
				childId = 4;
				break;
			case R.id.planmenu_detail_line2_item3:
				childId = 5;
				break;
			default:
				break;
			}

			ToastUtil.showToast(PlanMenuActivity.this, "groupId = " + groupId + ",  childId = " + childId);
		
			Intent intent = new Intent(PlanMenuActivity.this, CookBookDetailActivity.class);
			startActivity(intent);
		}
		
		public void onClickEvent(int groupId, int childId){
			PlanMenu planMenu = mPlanMenus.get(groupId);
			int menuId = -1;
			switch (childId) {
			case R.id.planmenu_detail_line1_item1:
				menuId = planMenu.getFirstMenuId();
				break;
			case R.id.planmenu_detail_line1_item2:
				menuId = planMenu.getSecondMenuId();
				break;
			case R.id.planmenu_detail_line1_item3:
				menuId = planMenu.getThirdMenuId();
				break;
			case R.id.planmenu_detail_line2_item1:
				menuId = planMenu.getFourMenuId();
				break;
			case R.id.planmenu_detail_line2_item2:
				menuId = planMenu.getFiveMenuId();
				break;
			case R.id.planmenu_detail_line2_item3:
				menuId = planMenu.getSixMenuId();
				break;
			}
		}
		
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
		pointViewParams.setMargins(DimenUtil.dp2px(this, 12), 0,
				DimenUtil.dp2px(this, 12), DimenUtil.dp2px(this, 12));
		
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.planmenu_actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bell:
			Intent intent = new Intent(this, SetWarnTimeActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
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
