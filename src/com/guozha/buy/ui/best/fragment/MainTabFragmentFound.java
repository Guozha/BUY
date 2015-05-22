package com.guozha.buy.ui.best.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.guozha.buy.R;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.ui.found.fragment.FoundMenuFragment;
import com.guozha.buy.ui.found.fragment.FoundSubjectFragment;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.view.ViewPagerTab;

/**
 * 发现
 * @author PeggyTong
 */
public class MainTabFragmentFound extends MainTabBaseFragment{
	
	private ViewPagerTab mViewPagerTab;
	private ViewPagerAdapter mViewPagerAdapter;
	private ViewPager mViewPager;

	@Override
	public void loadDataCompleted(MainPageInitDataManager dataManager,
			int handerType) {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		LogUtil.e("onCreateView");
		View view = inflater.inflate(R.layout.fragment_maintab_found, container, false);
		initActionBar("发现");
		initView(view); 
		return view;
	}
	
	private void initView(View view){
		setUpViewPage(view);
		setUpTab(view);
		
		view.findViewById(R.id.found_frag1_tab).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtil.e("getCurrentItem == " + mViewPager.getCurrentItem());
				if(mViewPager.getCurrentItem() == 0) return;
				mViewPager.setCurrentItem(0);
			}
		});
		
		view.findViewById(R.id.found_frag2_tab).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtil.e("getCurrentItem == " + mViewPager.getCurrentItem());
				if(mViewPager.getCurrentItem() == 1) return;
				mViewPager.setCurrentItem(1);
			}
		});
	}
	
	private void setUpViewPage(View view){
		mViewPagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager());
		mViewPager = (ViewPager) view.findViewById(R.id.found_viewpager);
		mViewPager.setAdapter(mViewPagerAdapter);
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
