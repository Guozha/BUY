package com.guozha.buy.controller;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.guozha.buy.R;
import com.guozha.buy.controller.best.fragment.MainTabBaseFragment;
import com.guozha.buy.controller.best.fragment.MainTabFragmentCart;
import com.guozha.buy.controller.best.fragment.MainTabFragmentFound;
import com.guozha.buy.controller.best.fragment.MainTabFragmentMPage;
import com.guozha.buy.controller.best.fragment.MainTabFragmentMarket;
import com.guozha.buy.controller.best.fragment.MainTabFragmentMine;
import com.guozha.buy.controller.mine.SettingActivity;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.server.ShareManager;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initFragment();
		//添加一个fragment
		getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_container, mFragments.get(0)).commit();
		initTabIndicators();
		initYoumeng();
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
		case 2:
			inflater.inflate(R.menu.mpage_actionbar_menu, menu);
			break;
		case 3:
			inflater.inflate(R.menu.cart_actionbar_menu, menu);
			break;
		case 4:
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
}
