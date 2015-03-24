package com.guozha.buy.activity.global;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.ChooseMenuListAdapter;
import com.guozha.buy.entry.QuickMenus;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 选择快捷菜单
 * @author PeggyTong
 *
 */
public class ChooseMenuActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "ChooseMenuPage";
	
	private ListView mChooseList;
	
	private List<QuickMenus[]> mQuickMenus;
	
	private List<String> mChoosedMenusId;
	
	private ChooseMenuListAdapter mChooseMenuListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_menu);
		customActionBarStyle();
		initQuickMenuData();
		initView();
		//initData();
	}
	
	/**
	 * 获取快速入口菜单列表
	 */
	private void initQuickMenuData(){
		mChoosedMenusId = new ArrayList<String>();
		mChoosedMenusId.add("1");
		mChoosedMenusId.add("5");
		mChoosedMenusId.add("7");
		mChoosedMenusId.add("8");
		
	    List<QuickMenus> quickMenus = new ArrayList<QuickMenus>();
		QuickMenus quickMenu;
		boolean isChoosed;
		for(int i = 0; i < 15; i++){
			if(mChoosedMenusId.contains(String.valueOf(i))){
				isChoosed = true;
			}else{
				isChoosed = false;
			}
			quickMenu = new QuickMenus(String.valueOf(i), "水果", String.valueOf(i), isChoosed);
			quickMenus.add(quickMenu);
		}
		quickMenus.get(3).setChoosed(true);
		

		
		addFormatData(quickMenus);
	}
	
	/**
	 * 适配数据
	 */
	private void addFormatData(List<QuickMenus> quickMenus){
		QuickMenus[] qm;
		if(mQuickMenus == null){
			mQuickMenus = new ArrayList<QuickMenus[]>();
		}
		int count = quickMenus.size() / 3;

		for(int i = 0; i < count; i++){
			qm = new QuickMenus[3];
			qm[0] = quickMenus.get(i * 3);
			qm[1] = quickMenus.get(i * 3 + 1);
			qm[2] = quickMenus.get(i * 3 + 2);
			mQuickMenus.add(qm);
		}
		
		int remain = quickMenus.size() % 3;
		
		if(remain == 0) return;
		qm = new QuickMenus[3];
		QuickMenus qm1;
		QuickMenus qm2;
		if(remain == 1){
			qm1 = quickMenus.get(count * 3);
			qm2 = null;
		}else{
			qm1 = quickMenus.get(count * 3);
			qm2 = quickMenus.get(count * 3 + 1);
		}
		qm[0] = qm1;
		qm[1] = qm2;
		qm[2] = null;
		mQuickMenus.add(qm);
	}
	
	/**
	 * 初始化UI
	 */
	private void initView(){
		mChooseList = (ListView) findViewById(R.id.choose_menu_list);
		mChooseMenuListAdapter = new ChooseMenuListAdapter(this, mQuickMenus);
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
		LogUtil.e("onStop");
		List<String> choosedMenusId = mChooseMenuListAdapter.getChoosedMenusID();
		for(int i = 0; i < choosedMenusId.size(); i++){
			LogUtil.e("choosedID = " + choosedMenusId.get(i));
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.e("onDestroy");
	}
}
