package com.guozha.buy.activity.global;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.GridView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.ChooseQuickMenuListAdapter;
import com.guozha.buy.entry.global.QuickMenu;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 选择快捷菜单
 * @author PeggyTong
 *
 */
public class ChooseMenuActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "ChooseMenuPage";
	
	private GridView mChooseList;
	
	private List<QuickMenu> mQuickMenus;
	
	private List<String> mChoosedMenusId = new ArrayList<String>();
	
	private ChooseQuickMenuListAdapter mChooseMenuListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_menu);
		customActionBarStyle();
		initQuickMenuData();
		initView();
		setResult(0);
		//initData();
	}
	
	/**
	 * 获取快速入口菜单列表
	 */
	private void initQuickMenuData(){
		List<QuickMenu> choosedQuickMenus = ConfigManager.getInstance().getQuickMenus();
		if(choosedQuickMenus == null){
			mChoosedMenusId.add("1");
			mChoosedMenusId.add("5");
			mChoosedMenusId.add("7");
			mChoosedMenusId.add("8");
			mChoosedMenusId.add("10");
		}else if(mChoosedMenusId == null){
			mChoosedMenusId = new ArrayList<String>();
			for(int i = 0; i < choosedQuickMenus.size(); i++){
				mChoosedMenusId.add(String.valueOf(choosedQuickMenus.get(i).getMenuId()));
			}
		}else{
			mChoosedMenusId.clear();
			for(int i = 0; i < choosedQuickMenus.size(); i++){
				mChoosedMenusId.add(String.valueOf(choosedQuickMenus.get(i).getMenuId()));
			}
		}
		
	    mQuickMenus = new ArrayList<QuickMenu>();
		QuickMenu quickMenu;
		for(int i = 0; i < 15; i++){
			quickMenu = new QuickMenu(i, "水果" + i);
			mQuickMenus.add(quickMenu);
		}
	}


	
	/**
	 * 初始化UI
	 */
	private void initView(){
		mChooseList = (GridView) findViewById(R.id.choose_menu_list);
		mChooseMenuListAdapter = new ChooseQuickMenuListAdapter(this, mQuickMenus, mChoosedMenusId);
		mChooseList.setAdapter(mChooseMenuListAdapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//友盟界面统计
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * 设置快捷入口配置文件
	 */
	private void setQuickMenuConfig() {
		List<Integer> choosedMenusId = mChooseMenuListAdapter.getChoosedMenusPosition();
		if(choosedMenusId == null) return;
		LogUtil.e("chosedMenusId = " + choosedMenusId);
		
		List<QuickMenu> choosedQuickMenus = new ArrayList<QuickMenu>();
		for(int i = 0; i < choosedMenusId.size(); i++){
			choosedQuickMenus.add(mQuickMenus.get(choosedMenusId.get(i)));
		}
		ConfigManager.getInstance().setQuickMenus(choosedQuickMenus);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			setQuickMenuConfig();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setQuickMenuConfig();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.e("onDestroy");
	}
}
