package com.guozha.buy.activity.global;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.mine.SettingActivity;
import com.guozha.buy.fragment.MainTabBaseFragment;
import com.guozha.buy.fragment.MainTabFragmentCart;
import com.guozha.buy.fragment.MainTabFragmentFound;
import com.guozha.buy.fragment.MainTabFragmentMPage;
import com.guozha.buy.fragment.MainTabFragmentMarket;
import com.guozha.buy.fragment.MainTabFragmentMine;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.share.ShareManager;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.view.TabBarItem;
import com.umeng.update.UmengUpdateAgent;

/**
 * 应用主界面
 * @author lixiaoqiang
 *
 */
public class MainActivity extends FragmentActivity{
	
	private int mCurrentItem = 0;
	
	private ClickTabItemListener mClickTabItemListener;
	
	private List<MainTabBaseFragment> mFragments = new ArrayList<MainTabBaseFragment>();
	
	private List<TabBarItem> mTabIndicators = 
			new ArrayList<TabBarItem>();
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
					//mTabIndicators.get(2).setTextNum(mInitDataManager.getCartItemsNum());
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initFragment();
		//添加一个fragment
		getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_container, mFragments.get(0)).commit();
		initTabIndicators();
		initData();
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
	 * 初始化Fragment
	 */
	private void initFragment(){
		mFragments.add(new MainTabFragmentMPage());
		mFragments.add(new MainTabFragmentFound());
		mFragments.add(new MainTabFragmentMarket());
		mFragments.add(new MainTabFragmentCart());
		mFragments.add(new MainTabFragmentMine());
	}
	

	
	/**
	 * 初始化Tab
	 */
	private void initTabIndicators(){
		mTabIndicators.add((TabBarItem)findViewById(R.id.id_indicator_one));
		mTabIndicators.add((TabBarItem)findViewById(R.id.id_indicator_two));
		mTabIndicators.add((TabBarItem)findViewById(R.id.id_indicator_three));
		mTabIndicators.add((TabBarItem)findViewById(R.id.id_indicator_four));
		mTabIndicators.add((TabBarItem)findViewById(R.id.id_indicator_five));
		mClickTabItemListener = new ClickTabItemListener();
		for(int i = 0; i < mTabIndicators.size(); i++){
			mTabIndicators.get(i).setOnClickListener(mClickTabItemListener);
		}
	}
	
	/**
	 * 底部Tab按下监听
	 * @author Administrator
	 *
	 */
	class ClickTabItemListener implements OnClickListener{
		@Override
		public void onClick(View view) {
			int viewId = view.getId();
			int currentItem;
			switch (view.getId()) {
			case R.id.id_indicator_one:
				currentItem = 0;
				break;
			case R.id.id_indicator_two:
				currentItem = 1;
				break;
			case R.id.id_indicator_three:
				currentItem = 2;
				break;
			case R.id.id_indicator_four:
				currentItem = 3;
				break;
			case R.id.id_indicator_five:
				currentItem = 4;
				break;
			default:
				currentItem = 0;
				break;
			}
			changeTabRefreshView(currentItem);
		}
	}
	
	/**
	 * 点击按钮后的逻辑及样式变化
	 * 
	 * @param view
	 */
	private void changeTabRefreshView(int currentItem) {
		mTabIndicators.get(mCurrentItem).setDisCheckedItem();
		mCurrentItem = currentItem;
		mTabIndicators.get(mCurrentItem).setCheckedItem();
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragment_container, mFragments.get(mCurrentItem))
			.addToBackStack(null).commit();
		invalidateOptionsMenu();
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
				changeTabRefreshView(0);
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
