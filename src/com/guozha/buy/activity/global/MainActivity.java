package com.guozha.buy.activity.global;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.market.ClickMarketMenuListener;
import com.guozha.buy.activity.mine.SettingActivity;
import com.guozha.buy.entry.cart.CartTotalData;
import com.guozha.buy.fragment.MainTabBaseFragment;
import com.guozha.buy.fragment.MainTabFragmentCart;
import com.guozha.buy.fragment.MainTabFragmentMPage;
import com.guozha.buy.fragment.MainTabFragmentMarket;
import com.guozha.buy.fragment.MainTabFragmentMine;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.share.ShareManager;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.view.ChangeColorIconWithText;
import com.guozha.buy.view.CustomViewPager;
import com.guozha.buy.view.CustomViewPager.OnInterceptTouchListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 应用主界面
 * @author lixiaoqiang
 *
 */
public class MainActivity extends FragmentActivity{
	
	private int mCurrentItem = 0;
	
	private CustomViewPager mCustomViewPager;
	private MyFragmentPagerAdapter mFragmentPagerAdapter;
	
	private ClickTabItemListener mClickTabItemListener;
	
	private List<MainTabBaseFragment> mFragments = new ArrayList<MainTabBaseFragment>();
	
	private List<ChangeColorIconWithText> mTabIndicators = 
			new ArrayList<ChangeColorIconWithText>();
	private MainPageInitDataManager mInitDataManager;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			MainTabBaseFragment fragment = null;
			switch (msg.what) {
			case MainPageInitDataManager.HAND_INITDATA_MSG_FIRST_CATEGORY:
			case MainPageInitDataManager.HAND_INITDATA_MSG_TODAY_INFO:
			case MainPageInitDataManager.HAND_INITDATA_MSG_PLAN_MENU_STATUS:
				fragment = mFragments.get(0);
				break;
			case MainPageInitDataManager.HAND_INITDATA_MSG_ITEMTYPE:
			case MainPageInitDataManager.HAND_INITDATA_MSG_MARKETHOME:
			case MainPageInitDataManager.HAND_INTTDATA_MSG_ADDRESS_LIST:
				fragment = mFragments.get(1);
				break;
			case MainPageInitDataManager.HAND_INITDATA_MSG_CART_ITEM:
				if(!mTabIndicators.isEmpty()){
					mTabIndicators.get(2).setTextNum(mInitDataManager.getCartItemsNum());
				}
				fragment = mFragments.get(2);
				break;
			case MainPageInitDataManager.HAND_INITDATA_MSG_ACCOUNTINFO:
				fragment = mFragments.get(3);
				break;
			}
			
			if(fragment != null){
				fragment.loadDataCompleted(mInitDataManager, msg.what);
			}
		};
	};
	
	/** 弱引用
	 private WeakReferenceHandler<MainActivity> weakHandler = new MyWeakReferenceHandler(this);
	
		static class MyWeakReferenceHandler extends WeakReferenceHandler<MainActivity>{

		public MyWeakReferenceHandler(MainActivity ref) {
			super(ref);
		}

		@Override
		protected void handleMessage(MainActivity ref, Message msg) {
			
		}
		
		}
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initFragment();
		initTabIndicators();
		initData();
		initViewPager();
		initYoumeng();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		mInitDataManager.getAccountInfo(handler);  	//获取账户信息（F4)
		mInitDataManager.getAddressInfos(handler);  //获取地址列表
		mInitDataManager.getCartItems(handler);		//获取购物车信息
		mInitDataManager.getMenuPlaneStatus(handler); //获取今日菜谱计划状态
	}
	
	/**
	 * 初始化四个Fragment的数据
	 */
	private void initData(){
		mInitDataManager = 
				MainPageInitDataManager.getInstance(CustomApplication.getContext());
		mInitDataManager.getAccountInfo(handler);  	//获取账户信息（F4)
		mInitDataManager.getGoodsItemType(handler);  //获取商品分类（F2)
		mInitDataManager.getMarketHomePage(handler, 1, 6); //获取逛菜场首页数据
		mInitDataManager.getQuickMenus(handler);	//获取一级菜单
		mInitDataManager.getAddressInfos(handler);  //获取地址列表
		mInitDataManager.getCartItems(handler);		//获取购物车数据
		mInitDataManager.getTodayInfo(handler);		//获取今日信息
		mInitDataManager.getMenuPlaneStatus(handler);	//获取今日菜谱计划状态
	}
	
	/**
	 * 初始化友盟相关
	 */
	private void initYoumeng(){
		//友盟自动更新
		UmengUpdateAgent.update(this);
		//友盟静默更新
		//UmengUpdateAgent.silentUpdate(this);
	}
	
	/**
	 * 获取Handler
	 * @return
	 */
	public Handler getHandler(){
		return handler;
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		mCustomViewPager = (CustomViewPager) findViewById(R.id.main_viewpage);
		mCustomViewPager.setOnPageChangeListener(new PagerChangeListener());
		mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		mCustomViewPager.setAdapter(mFragmentPagerAdapter);
		
		/*
		 *本来是为了解决滑动冲突的 
		 */
		mCustomViewPager.setOnInterceptTouchListener(new OnInterceptTouchListener() {
			
			@Override
			public boolean interceptTouched(float eventX, float eventY) {
				/*
				MainTabFragmentMPage mainTabFragmentMPage = 
						(MainTabFragmentMPage) mFragments.get(0);
				if(mainTabFragmentMPage == null) return true;
				int beginYPoint = mainTabFragmentMPage.getBeginYPoint();
				int endYPoint = mainTabFragmentMPage.getEndYPoint();
				if(eventY < beginYPoint || (eventY > endYPoint)) return true;
				*/
				return false;
			}
		});
		
	}
	
	/**
	 * 初始化Fragment
	 */
	private void initFragment(){
		ClickMarketMenuListener clickMarketMenuListener = new ClickMarketMenuListener() {
			
			@Override
			public void clickMarketMenu() {
				resetOtherTabs();
				mCurrentItem = 1;
				mTabIndicators.get(mCurrentItem).setIconAlpha(1.0f);
				mCustomViewPager.setCurrentItem(mCurrentItem, false);
			}
		};
		MainTabFragmentMPage mPage = new MainTabFragmentMPage();
		mPage.setOnClickMarketMenuListener(clickMarketMenuListener);
		MainTabFragmentCart mCart = new MainTabFragmentCart();
		mCart.setOnClickMarketMenuListener(clickMarketMenuListener);
		mFragments.add(mPage);
		mFragments.add(new MainTabFragmentMarket());
		mFragments.add(mCart);
		mFragments.add(new MainTabFragmentMine());
	}
	

	
	/**
	 * 初始化Tab
	 */
	private void initTabIndicators(){
		ChangeColorIconWithText one = 
				(ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = 
				(ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		
		mTabIndicators.add(two);
		ChangeColorIconWithText three = 
				(ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		three.setCloseDrawNum(false);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = 
				(ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);
		
		mClickTabItemListener = new ClickTabItemListener();
		one.setOnClickListener(mClickTabItemListener);
		two.setOnClickListener(mClickTabItemListener);
		three.setOnClickListener(mClickTabItemListener);
		four.setOnClickListener(mClickTabItemListener);

		one.setIconAlpha(1.0f);
	}
	
	/**
	 * 底部Tab按下监听
	 * @author Administrator
	 *
	 */
	class ClickTabItemListener implements OnClickListener{
		@Override
		public void onClick(View view) {
			clickTab(view);
		}
	}
	
	/**
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicators.size(); i++) {
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}
	
	/**
	 * 点击Tab按钮
	 * 
	 * @param view
	 */
	private void clickTab(View view) {
		
		resetOtherTabs();
		
		switch (view.getId()) {
		case R.id.id_indicator_one:
			mCurrentItem = 0;
			break;
		case R.id.id_indicator_two:
			mCurrentItem = 1;
			break;
		case R.id.id_indicator_three:
			mCurrentItem = 2;
			break;
		case R.id.id_indicator_four:
			mCurrentItem = 3;
			break;
		}
		mTabIndicators.get(mCurrentItem).setIconAlpha(1.0f);
		mCustomViewPager.setCurrentItem(mCurrentItem, false);
	}

	/**
	 * 主界面Fragment适配器
	 * @author lixiaoqiang
	 *
	 */
	class MyFragmentPagerAdapter extends FragmentPagerAdapter{

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}
		
		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}
	}
	
	/**
	 * 界面滑动监听
	 * @author lixiaoqiang
	 *
	 */
	class PagerChangeListener implements OnPageChangeListener{	

		@Override
		public void onPageScrollStateChanged(int status) { 
			switch (status) {
			case 0:
				//mCoverPlateView.setVisibility(View.GONE);
				break;
			case 1:
				//mCoverPlateView.setVisibility(View.VISIBLE);
				break;
			case 2:
				//mCoverPlateView.setVisibility(View.GONE);
				break;
			}
		}

		@Override
		public void onPageSelected(int position) {			
			ChangeColorIconWithText choosed = mTabIndicators.get(position);
			for(int i = 0; i < mTabIndicators.size(); i++){
				mTabIndicators.get(i).setChoosed(false);
			}
			choosed.setChoosed(true);
			mCurrentItem = position;
			invalidateOptionsMenu();
		}

		@Override
		public void onPageScrolled(int position, 
				float positionOffset, int positionOffsetPixels) {
			if(positionOffset <= 0) return;
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			if(right == null) return;			
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计
		//TODO 注意给其他界面添加友盟统计事件
		//官方参考 http://dev.umeng.com/analytics/android-doc/integration
		MobclickAgent.onResume(this);
		
		
		
		//TODO 友盟自定义事件，在onResume()后面调用，不要放在onCreate()中
		//1、计数事件（统计次数）
		//MobclickAgent.onEvent(this, "test_event_1");
		//2、统计行为属性发生次数
		/*
		 *  HashMap<String,String> map = new HashMap<String,String>();
			map.put("type","book");
			map.put("quantity","3"); 
			MobclickAgent.onEvent(mContext, "purchase", map);  
		 */
		//3、计算事件
		/*
		int duration = 12000; //开发者需要自己计算音乐播放时长
	　　 Map<String, String> map_value = new HashMap<String, String>();
	　　 map_value.put("type", "popular");
	　　 map_value.put("artist", "JJLin");	
		MobclickAgent.onEventValue(this, "music", map_value, duration);
		*/
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//TODO
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 修改ActionBar的菜单
	 * 注：必须调用invalidateOptionsMenu()才能刷新
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		menu.clear();
		switch (mCurrentItem) {
		case 0:
		case 1:
			inflater.inflate(R.menu.mpage_actionbar_menu, menu);
			break;
		case 2:
			inflater.inflate(R.menu.cart_actionbar_menu, menu);
			break;
		case 3:
			inflater.inflate(R.menu.mine_actionbar_menu, menu);
			break;
		default:
			break;
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_search: //查找
			intent = new Intent(MainActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.action_share:  //分享
			ShareManager shareManager = new ShareManager(MainActivity.this);
			shareManager.showSharePlatform(this);
			break;
		case R.id.action_setting: //设置
			if(ConfigManager.getInstance().getUserToken(MainActivity.this) == null){
				return super.onMenuItemSelected(featureId, item);
			}
			intent = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ConfigManager.getInstance().setUserToken(null);
	}
	
	/**
	 * 防止误操作退出
	 */
	long mWaitTime = 2000;    
	long mTouchTime = 0;   
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//按下了物理返回键
		if(KeyEvent.KEYCODE_BACK == keyCode){
			if(mCurrentItem != 0){
				resetOtherTabs();
				mCurrentItem = 0;
				mTabIndicators.get(mCurrentItem).setIconAlpha(1.0f);
				mCustomViewPager.setCurrentItem(mCurrentItem, false);
				return true;
			}
			long currentTimes = System.currentTimeMillis();
			if((currentTimes - mTouchTime) >= mWaitTime){
				mTouchTime = currentTimes;
				ToastUtil.showToast(this, "再按一次退出");
			}else{
				mTouchTime = 0;
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case MainTabFragmentMarket.REQUEST_CODE_ADDRESS:
			mInitDataManager.getAddressInfos(handler);  //获取地址列表
			break;
		case MainTabFragmentMarket.REQUEST_CODE_CART:   //购物车
			updateCartItemData();
			break;
		}
	}
	
	/**
	 * 重新请求购物车数据
	 */
	public void updateCartItemData(){
		mInitDataManager.getCartItems(handler);
	}
	
}
