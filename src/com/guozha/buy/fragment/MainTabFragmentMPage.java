package com.guozha.buy.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.ChooseMenuActivity;
import com.guozha.buy.activity.PlanMenuActivity;
import com.guozha.buy.activity.PreSpecialActivity;
import com.guozha.buy.activity.SeasonActivity;
import com.guozha.buy.global.MainPageInitDataManager;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMPage extends MainTabBaseFragment implements OnClickListener{
	
	private static final String TAG = "MainTabFragmentMPage";
	private static final String PAGE_NAME = "MainPage";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_maintab_mpage, container, false);
		initView(view);
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
		view.findViewById(R.id.mpage_season_menu).setOnClickListener(this);
	}
	
	@Override
	public void loadDataCompleted(MainPageInitDataManager dataManager, int handlerType) {
		// TODO Auto-generated method stub
		
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
			startActivity(intent);
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
		default:
			break;
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
	public void onPause() {
		super.onPause();
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
			
	}

}
