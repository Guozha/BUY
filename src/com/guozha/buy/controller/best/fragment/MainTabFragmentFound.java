package com.guozha.buy.controller.best.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.found.fragment.FoundMenuFragment;
import com.guozha.buy.controller.found.fragment.FoundSubjectFragment;
import com.guozha.buy.view.ViewPagerTab;

/**
 * 发现
 * @author PeggyTong
 */
public class MainTabFragmentFound extends MainTabBaseFragment{
	
	private ViewPagerTab mViewPagerTab;
	private ViewPagerAdapter mViewPagerAdapter;
	private ViewPager mViewPager;
	private List<TextView> mTabTexts;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_found, container, false);
		initActionBar("发现");
		initView(view); 
		return view;
	}
	
	private void initView(View view){
		setUpViewPage(view);
		mTabTexts = new ArrayList<TextView>();
		mTabTexts.add((TextView) view.findViewById(R.id.found_frag1_tab));
		mTabTexts.add((TextView) view.findViewById(R.id.found_frag2_tab));
		setUpTab(view);
	}
	
	private void setUpViewPage(View view){
		mViewPagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager());
		mViewPager = (ViewPager) view.findViewById(R.id.found_viewpager);
		mViewPager.setAdapter(mViewPagerAdapter);
	}
	
	private void setUpTab(View view){
		mViewPagerTab = (ViewPagerTab) view.findViewById(R.id.viewpager_tab);
		mViewPagerTab.setViewPageAndTabTexts(mViewPager, mTabTexts);
	}
	
	@Override
	public boolean onKeyDownBack() {
		if(mViewPagerTab.getCurrentIndex() != 0){
			mViewPager.setCurrentItem(0);
			return true;
		}
		return false;
	}
	
	class ViewPagerAdapter extends FragmentPagerAdapter{
		
		private Class[] fragments;
		
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new Class[]{FoundSubjectFragment.class, FoundMenuFragment.class};
		}

		@Override
		public Fragment getItem(int position) {
			try {
				return (Fragment) fragments[position].newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}	

		@Override
		public int getCount() {
			return fragments.length;
		}
	}
}
