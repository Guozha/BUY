package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.ChooseMenuActivity;
import com.guozha.buy.activity.mpage.PlanMenuActivity;
import com.guozha.buy.activity.mpage.PreSpecialActivity;
import com.guozha.buy.activity.mpage.SeasonActivity;
import com.guozha.buy.entry.global.QuickMenu;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.CustomApplication;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMPage extends MainTabBaseFragment implements OnClickListener{
	
	private static final String TAG = "MainTabFragmentMPage";
	private static final String PAGE_NAME = "MainPage";
	private static final int REQUEST_CODE = 0;
	
	private List<TextView> mQuickMenus;
	
	private ImageView mSeasonImage;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_maintab_mpage, container, false);
		initView(view);
		initQuickMenusData();
		return view;
	}
	
	/**
	 * 初始化视图
	 * @param view
	 */
	private void initView(View view){
		if(view == null) return;
		view.findViewById(R.id.fragment_mpage_season).setOnClickListener(this);
		view.findViewById(R.id.mpage_choose_menu_custom).setOnClickListener(this);
		view.findViewById(R.id.mpage_pre_special_menu).setOnClickListener(this);
		view.findViewById(R.id.mpage_market_menu).setOnClickListener(this);
		mQuickMenus = new ArrayList<TextView>();
		mQuickMenus.add((TextView)view.findViewById(R.id.mpage_quick_menu_1));
		mQuickMenus.add((TextView)view.findViewById(R.id.mpage_quick_menu_2));
		mQuickMenus.add((TextView)view.findViewById(R.id.mpage_quick_menu_3));
		mQuickMenus.add((TextView)view.findViewById(R.id.mpage_quick_menu_4));
		mQuickMenus.add((TextView)view.findViewById(R.id.mpage_quick_menu_5));
		
		for(int i = 0; i < mQuickMenus.size(); i++){
			mQuickMenus.get(i).setOnClickListener(this);
		}
		
		mSeasonImage = (ImageView) view.findViewById(R.id.mpage_season_menu);
		mSeasonImage.setOnClickListener(this);
		
		setSeasonImageByMonth();
	}
	
	/**
	 * 初始化快捷菜单数据
	 */
	private void initQuickMenusData(){
		if(mQuickMenus == null) return;
		List<QuickMenu> quickMenus = ConfigManager.getInstance().getQuickMenus();
		
		if(quickMenus == null) {
			for(int i = 0; i < 5; i++){
				mQuickMenus.get(i).setBackgroundResource(R.drawable.main_tag_edit);
				mQuickMenus.get(i).setTag(-1);
				mQuickMenus.get(i).setText("");
			}
			return;
		}
		ToastUtil.showToast(getActivity(), "quickMenusSize = " + quickMenus.size());
		for(int i = 0; i < quickMenus.size(); i++){
			mQuickMenus.get(i).setText(quickMenus.get(i).getName());
			mQuickMenus.get(i).setTag(quickMenus.get(i).getMenuId());
			mQuickMenus.get(i).setBackgroundResource(R.drawable.main_tag_backgroung);
		}
		
		for(int i = 0; i < 5 - quickMenus.size(); i++){
			mQuickMenus.get(i).setBackgroundResource(R.drawable.main_tag_edit);
			mQuickMenus.get(i).setTag(-1);
			mQuickMenus.get(i).setText("");
		}
	}
	
	/**
	 * 根据季节设置图片
	 */
	private void setSeasonImageByMonth(){
		if(mSeasonImage == null) return;
		Time time = new Time();
		time.setToNow();
		int month = time.month + 1;
		ToastUtil.showToast(getActivity(), "month = " + month);
		if(month >= 2 && month < 5){  //2,3,4月份
			mSeasonImage.setImageResource(R.drawable.main_season_img_spring);
		}else if(month >= 5 && month < 8){ //5,6,7月份
			//TODO 夏天的图片
		}else if(month >= 8 && month < 11){ //8,9,10月份
			mSeasonImage.setImageResource(R.drawable.main_season_img_autumn);
		}else{//11,12,1月份
			//TODO 冬天的图片
		}
	}
	
	@Override
	public void loadDataCompleted(MainPageInitDataManager dataManager, int handlerType) {
		mDataManager = dataManager;
		switch (handlerType) {
		case MainPageInitDataManager.HAND_INITDATA_MSG_FIRST_CATEGORY:  //一级菜单
			List<QuickMenu> quickMenu = mDataManager.getQuickMenus(null);
			if(quickMenu == null) return;
			List<QuickMenu> defaultQuickMenu = new ArrayList<QuickMenu>();
			for(int i =0; i < quickMenu.size(); i++){
				if(i >= 5) return;
				defaultQuickMenu.add(quickMenu.get(i));
			}
			ConfigManager.getInstance().setQuickMenus(defaultQuickMenu);
			break;
		}
	}
	
	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		//菜谱计划
		case R.id.fragment_mpage_season:
			intent = new Intent(getActivity(), PlanMenuActivity.class);
			startActivity(intent);
			break;
		case R.id.mpage_choose_menu_custom:
			intent = new Intent(MainTabFragmentMPage.this.getActivity(), ChooseMenuActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
			break;
		case R.id.mpage_pre_special_menu:
			intent = new Intent(getActivity(), PreSpecialActivity.class);
			startActivity(intent);
			break;
		case R.id.mpage_market_menu:
			if(mClickMarketMenuListener != null){
				mClickMarketMenuListener.clickMarketMenu();
			}
			break;
		case R.id.mpage_season_menu:
			intent = new Intent(getActivity(), SeasonActivity.class);
			startActivity(intent);
			break;
		case R.id.mpage_quick_menu_1:
		case R.id.mpage_quick_menu_2:
		case R.id.mpage_quick_menu_3:
		case R.id.mpage_quick_menu_4:
		case R.id.mpage_quick_menu_5:
			clickQuickMneuEvent(view);
			break;
		default:
			break;
		}
	}

	private void clickQuickMneuEvent(View view) {
		int tag = (Integer) view.getTag();
		Intent intent;
		if(-1 == tag){
			intent = new Intent(MainTabFragmentMPage.this.getActivity(), ChooseMenuActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
		}else{
			if(view == null) return;
			//TODO 跳转到对应的类目
		}
	}
	
	public interface ClickMarketMenuListener{
		public void clickMarketMenu();
	}
	
	private ClickMarketMenuListener mClickMarketMenuListener;
	
	public void setOnClickMarketMenuListener(ClickMarketMenuListener clickMarketMenuListner){
		this.mClickMarketMenuListener = clickMarketMenuListner;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			//初始化ActionBar
			initActionBar(getActivity().getActionBar());
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
	
	/**
	 * 初始化ActionBar
	 * @param actionbar
	 */
	private void initActionBar(ActionBar actionbar) {
		if(actionbar == null) return;
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.actionbar_base_view);
		TextView title = (TextView) actionbar.getCustomView().findViewById(R.id.title);
		title.setText("我要买菜");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == REQUEST_CODE){
			initQuickMenusData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
			
	}

}
