package com.guozha.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.guozha.buy.R;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.view.ViewPagerTab;

/**
 * 发现
 * @author PeggyTong
 *
 */
public class MainTabFragmentFound extends MainTabBaseFragment{
	
	private ViewPagerTab mViewPagerTab;
	private ViewPager mViewPager;

	@Override
	public void loadDataCompleted(MainPageInitDataManager dataManager,
			int handerType) {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_found, container, false);
		initView(view); 
		return view;
	}
	
	private void initView(View view){
		setUpViewPage(view);
		setUpTab(view);
	}
	
	private void setUpViewPage(View view){
		//mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) view.findViewById(R.id.found_viewpager);
		//mViewPager.setAdapter(mViewPagerAdapter);
	}
	
	private void setUpTab(View view){
		mViewPagerTab = (ViewPagerTab) view.findViewById(R.id.viewpager_tab);
		mViewPagerTab.setViewPager(mViewPager);
		ImageView childView = new ImageView(getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		childView.setImageResource(R.drawable.main_favorite_background);
		childView.setScaleType(ScaleType.FIT_XY);
		childView.setLayoutParams(params);
		mViewPagerTab.addView(childView);
	}
}
