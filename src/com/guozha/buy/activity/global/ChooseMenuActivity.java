package com.guozha.buy.activity.global;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.GridView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.adapter.ChooseQuickMenuListAdapter;
import com.guozha.buy.entry.global.QuickMenu;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 选择快捷菜单
 * @author PeggyTong
 *
 */
public class ChooseMenuActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "ChooseMenuPage";
	
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private GridView mChooseList;
	
	private List<QuickMenu> mQuickMenus = new ArrayList<QuickMenu>();
	
	private List<String> mChoosedMenusId = new ArrayList<String>();
	
	private ChooseQuickMenuListAdapter mChooseMenuListAdapter;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				initQuickMenuData();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_menu);
		customActionBarStyle();
		initView();
		initData();
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
		mChooseMenuListAdapter = new ChooseQuickMenuListAdapter(this, mQuickMenus, mChoosedMenusId);
		mChooseList.setAdapter(mChooseMenuListAdapter);
	}
	
	/**
	 * 初始化UI
	 */
	private void initView(){
		mChooseList = (GridView) findViewById(R.id.choose_menu_list);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		String paramsPath = "goods/frontType/typeList?frontTypeId=";
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + paramsPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				List<GoodsItemType> goodsItemTypes = 
						gson.fromJson(response, new TypeToken<List<GoodsItemType>>() { }.getType());
				QuickMenu quickMenu;
				for(int i = 0; i < goodsItemTypes.size(); i++){
					int id = goodsItemTypes.get(i).getFrontTypeId();
					String shortName = goodsItemTypes.get(i).getShortName();
					quickMenu = new QuickMenu(id, shortName);
					mQuickMenus.add(quickMenu);
				}
				handler.sendEmptyMessage(HAND_DATA_COMPLETED);
			}
		});
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
	}
}
