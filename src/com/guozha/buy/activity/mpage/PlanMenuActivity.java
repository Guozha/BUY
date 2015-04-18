package com.guozha.buy.activity.mpage;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.activity.global.SetWarnTimeActivity;
import com.guozha.buy.dialog.RemindLoginDialog;
import com.guozha.buy.entry.mpage.plan.PlanMenu;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 计划一周菜谱
 * @author PeggyTong
 *
 */
public class PlanMenuActivity extends BaseActivity implements OnCheckedChangeListener,OnClickListener{
	
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
	
	private List<ImageView> mButtons;
	
	private ViewFlipper mViewFlipper;
	private MenuFlipperClick mMenuFlipperClick;
	
	private int mCurrentIndex;
	
	private List<PlanMenu> mPlanMenus;
	
	private List<View> mAreas;
	
	private List<ImageView> points = new ArrayList<ImageView>();
	
	private BitmapCache mBitmapCache = CustomApplication.getBitmapCache();
	
	private TextView mWeakDayText;
	
	private List<TextView> mWeakDays;
	
	private List<List<CheckBox>> mCheckBoxs;
	
	private Button mCollectionChoosed;		//选择的加入收藏
	private Button mAddCartChoosed;			//选择的加入购物车
	
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
		customActionBarStyle("饮食管家");
		initView();
		initPlanMenuData();
	}
	
	/**
	 * 初始化View视图
	 */
	private void initView(){
		mViewFlipper = (ViewFlipper) findViewById(R.id.planmenu_content_view);
		
		mCollectionChoosed = (Button) findViewById(R.id.colloection_menu_button);
		mAddCartChoosed = (Button) findViewById(R.id.addcart_menu_button);
		mCollectionChoosed.setOnClickListener(this);
		mAddCartChoosed.setOnClickListener(this);
		
		mWeakDayText = (TextView) findViewById(R.id.choosed_week_day);
		//设置Tag
		mButton1 = (ImageView) findViewById(R.id.plantmenu_choose_button1);
		mButton2 = (ImageView) findViewById(R.id.plantmenu_choose_button2);
		mButton3 = (ImageView) findViewById(R.id.plantmenu_choose_button3);
		mButton4 = (ImageView) findViewById(R.id.plantmenu_choose_button4);
		mButton5 = (ImageView) findViewById(R.id.plantmenu_choose_button5);
		mButton6 = (ImageView) findViewById(R.id.plantmenu_choose_button6);
		mButton7 = (ImageView) findViewById(R.id.plantmenu_choose_button7);
		
		mButtons = new ArrayList<ImageView>();
		mButtons.add(mButton1);
		mButtons.add(mButton2);
		mButtons.add(mButton3);
		mButtons.add(mButton4);
		mButtons.add(mButton5);
		mButtons.add(mButton6);
		mButtons.add(mButton7);
		
		for(int i = 0; i < mButtons.size(); i++){
			mButtons.get(i).setTag(i);
			mButtons.get(i).setOnClickListener(mMenuFlipperClick);
		}
		
		mWeakDays = new ArrayList<TextView>();
		mWeakDays.add((TextView)findViewById(R.id.week_day1));
		mWeakDays.add((TextView)findViewById(R.id.week_day2));
		mWeakDays.add((TextView)findViewById(R.id.week_day3));
		mWeakDays.add((TextView)findViewById(R.id.week_day4));
		mWeakDays.add((TextView)findViewById(R.id.week_day5));
		mWeakDays.add((TextView)findViewById(R.id.week_day6));
		mWeakDays.add((TextView)findViewById(R.id.week_day7));
	}
	
	/**
	 * 初始化计划菜谱数据
	 */
	private void initPlanMenuData(){
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("menuplan/list")
		.setParams("userId", userId);
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
			    mPlanMenus = gson.fromJson(response, new TypeToken<List<PlanMenu>>() { }.getType());
			    if(mPlanMenus != null && mPlanMenus.size() == 7){
			    	handler.sendEmptyMessage(HAND_PLAN_MENU_DATA_COMPLETED);
			    }
			}
		});
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.colloection_menu_button:
			requestCollectRecipeList();
			break;
		case R.id.addcart_menu_button:
			requestAddCartRecipeList();
			break;
		}
	}
	
	/**
	 * 请求添加多个菜谱到购物车
	 */
	private void requestAddCartRecipeList(){
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(PlanMenuActivity.this);
		if(token == null) return;
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		if(mCheckBoxs == null) {
			ToastUtil.showToast(PlanMenuActivity.this, "没有可添加的数据");
			return;
		}
		List<CheckBox> checkBoxs = mCheckBoxs.get(mCurrentIndex);
		for(int i = 0; i < checkBoxs.size(); i++){
			CheckBox checkbox = checkBoxs.get(i);
			if(!checkbox.isChecked()) continue;
			requestAddCart(userId, token, addressId,
					checkbox);
		}
		MainPageInitDataManager.mCartItemsUpdated = true;
	}

	private void requestAddCart(int userId, String token,
			int addressId, CheckBox checkbox) {
		RequestParam paramPath = new RequestParam("cart/insert")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("addressId", addressId)
		.setParams("id", String.valueOf(checkbox.getTag()))
		.setParams("productType", "01")
		.setParams("amount", "1");
		HttpManager.getInstance(PlanMenuActivity.this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							ToastUtil.showToast(PlanMenuActivity.this, "添加成功");
						}else{
							ToastUtil.showToast(PlanMenuActivity.this, response.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	

	/**
	 * 请求收藏多个菜谱
	 */
	private void requestCollectRecipeList() {
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(PlanMenuActivity.this);
		if(token == null) return;
		StringBuffer menuIds = new StringBuffer();
		if(mCheckBoxs == null) {
			ToastUtil.showToast(PlanMenuActivity.this, "没有可添加的数据");
			return;
		}
		List<CheckBox> checkBoxs = mCheckBoxs.get(mCurrentIndex);
		for(int i = 0; i < checkBoxs.size(); i++){
			CheckBox checkbox = checkBoxs.get(i);
			if(!checkbox.isChecked()) continue;
			menuIds.append(String.valueOf(checkbox.getTag()));
			menuIds.append(",");
		}
		String menus = "";
		if(menuIds.length() > 1){
			 menus = menuIds.substring(0, menuIds.length() - 1);
		}else{
			ToastUtil.showToast(PlanMenuActivity.this, "请先选择菜谱");
			return;
		}
		LogUtil.e("menusId end==== " + menus);
		RequestParam paramPath = new RequestParam("account/myfavo/insertMenuFavo")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("menuIds", menus);
		HttpManager.getInstance(PlanMenuActivity.this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							ToastUtil.showToast(PlanMenuActivity.this, "收藏成功");
						}else{
							ToastUtil.showToast(PlanMenuActivity.this, response.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
	
	
	private void initPlanMenuItem(){
		
		getPointBar();
		mCurrentIndex = 0;//开始默认选中第一个
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
		mCheckBoxs = new ArrayList<List<CheckBox>>();
		List<CheckBox> checkboxs;
		for(int i = 0; i < mPlanMenus.size(); i++){
			areaView = mAreas.get(i);
			checkboxs = new ArrayList<CheckBox>();
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
			
			check1.setTag(1);
			check2.setTag(2);
			check3.setTag(3);
			check4.setTag(4);
			check5.setTag(5);
			check6.setTag(6);

			check1.setOnCheckedChangeListener(this);
			check2.setOnCheckedChangeListener(this);
			check3.setOnCheckedChangeListener(this);
			check4.setOnCheckedChangeListener(this);
			check5.setOnCheckedChangeListener(this);
			check6.setOnCheckedChangeListener(this);
			
			checkboxs.add(check1);
			checkboxs.add(check2);
			checkboxs.add(check3);
			checkboxs.add(check4);
			checkboxs.add(check5);
			checkboxs.add(check6);
			mCheckBoxs.add(checkboxs);
			
			PlanMenu planMenu = mPlanMenus.get(i);
			
			mWeakDays.get(i).setText(UnitConvertUtil.getWeakString(planMenu.getWeek()));
			
			if(planMenu == null) return;
			String imgUrl = planMenu.getFirstMenuImg();
			isInitStatus = true;
			mBitmapCache.loadBitmaps(image1, imgUrl);
			check1.setText(planMenu.getFirstMenuName());
			check1.setTag(planMenu.getFirstMenuId());
			boolean isCheck1 = (1 == planMenu.getFirstMenuCheck() ? true : false);
			check1.setChecked(isCheck1);
			
			imgUrl = planMenu.getSecondMenuImg();
			mBitmapCache.loadBitmaps(image2, imgUrl);
			check2.setText(planMenu.getSecondMenuName());
			check2.setTag(planMenu.getSecondMenuId());
			boolean isCheck2 = (1== planMenu.getSecondMenuCheck() ? true : false);
			check2.setChecked(isCheck2);
			
			imgUrl = planMenu.getThirdMenuImg();
			mBitmapCache.loadBitmaps(image3, imgUrl);
			check3.setText(planMenu.getThirdMenuName());
			check3.setTag(planMenu.getThirdMenuId());
			boolean isCheck3 = (1 == planMenu.getThirdMenuCheck() ? true : false);
			check3.setChecked(isCheck3);
			
			imgUrl = planMenu.getFourMenuImg();
			mBitmapCache.loadBitmaps(image4, imgUrl);
			check4.setText(planMenu.getFourMenuName());
			check4.setTag(planMenu.getFourMenuId());
			boolean isCheck4 = (1 == planMenu.getFourMenuCheck() ? true : false);
			check4.setChecked(isCheck4);
			
			imgUrl = planMenu.getFiveMenuImg();
			mBitmapCache.loadBitmaps(image5, imgUrl);
			check5.setText(planMenu.getFiveMenuName());
			check5.setTag(planMenu.getFiveMenuId());
			boolean isCheck5 = (1 == planMenu.getFiveMenuCheck() ? true : false);
			check5.setChecked(isCheck5);
			
			imgUrl = planMenu.getSixMenuImg();
			mBitmapCache.loadBitmaps(image6, imgUrl);
			check6.setText(planMenu.getSixMenuName());
			check6.setTag(planMenu.getSixMenuId());
			boolean isCheck6 = (1 == planMenu.getSixMenuCheck() ? true : false);
			check6.setChecked(isCheck6);
			
			
			if(isCheck1 || isCheck2 || isCheck3 || isCheck4 || isCheck5 || isCheck6){
				mButtons.get(i).setImageResource(R.drawable.main_plan_days_added);
			}else{
				mButtons.get(i).setImageResource(R.drawable.main_plan_days_normal);
			}
			
			isInitStatus = false;
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
			onClickEvent(groupId, view.getId());
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
			
			Intent intent = new Intent(PlanMenuActivity.this, CookBookDetailActivity.class);
			intent.putExtra("menuId", menuId);
			startActivity(intent);
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
				PlanMenu planMenu = mPlanMenus.get(i);
				if(planMenu != null){
					mWeakDayText.setText(UnitConvertUtil.getWeakString(planMenu.getWeek()));
				}
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
		mBitmapCache.fluchCache();
		//友盟页面统计代码
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}

	private String successMsg;
	private boolean isInitStatus = false; //初始化状态中
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isInitStatus) return;
		List<CheckBox> checkBoxs = mCheckBoxs.get(mCurrentIndex);
		
		PlanMenu planMenu = mPlanMenus.get(mCurrentIndex);
		
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(PlanMenuActivity.this);
		if(token == null) return;
		
		RequestParam paramPath = new RequestParam("menuplan/insert")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("planDate", planMenu.getPlanDate());
		
		if(isChecked){
			successMsg = "已添加到计划";
		}else{
			successMsg = "取消计划";
		}
		
		CheckBox checkbox = checkBoxs.get(0);
		if(checkbox != null){
			paramPath.setParams("firstMenuId", checkbox.isChecked() ? 
					String.valueOf(planMenu.getFirstMenuId()) : "");
		}
		checkbox = checkBoxs.get(1);
		if(checkbox != null){
			paramPath.setParams("secondMenuId", checkbox.isChecked() ? 
					String.valueOf(planMenu.getSecondMenuId()) : "");
		}
		checkbox = checkBoxs.get(2);
		if(checkbox != null){
			paramPath.setParams("thirdMenuId", checkbox.isChecked() ? 
					String.valueOf(planMenu.getThirdMenuId()) : "");
		}
		checkbox = checkBoxs.get(3);
		if(checkbox != null){
			paramPath.setParams("fourMenuId", checkbox.isChecked() ? 
					String.valueOf(planMenu.getFourMenuId()) : "");
		}
		checkbox = checkBoxs.get(4);
		if(checkbox != null){
			paramPath.setParams("fiveMenuId", checkbox.isChecked() ? 
					String.valueOf(planMenu.getFiveMenuId()) : "");
		}
		checkbox = checkBoxs.get(5);
		if(checkbox != null){
			paramPath.setParams("sixMenuId", checkbox.isChecked() ? 
					String.valueOf(planMenu.getSixMenuId()) : "");
		}
		
		HttpManager.getInstance(this).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode");
					if("1".equals(returnCode)){
						ToastUtil.showToast(PlanMenuActivity.this, successMsg);
					}else{
						ToastUtil.showToast(PlanMenuActivity.this, "请求失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		updateChooseImageButton();
	}
	
	
	private void updateChooseImageButton(){
		List<CheckBox> checkBoxs = mCheckBoxs.get(mCurrentIndex);
		boolean isChoosedDay = false;
		for(int i = 0; i < checkBoxs.size(); i++){
			if(checkBoxs.get(i).isChecked()) isChoosedDay = true;
		}
		
		if(isChoosedDay){
			mButtons.get(mCurrentIndex).setImageResource(R.drawable.main_plan_days_added);
		}else{
			mButtons.get(mCurrentIndex).setImageResource(R.drawable.main_plan_days_normal);
		}
	}
}
