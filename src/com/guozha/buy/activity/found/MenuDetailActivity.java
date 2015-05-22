package com.guozha.buy.activity.found;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.guozha.buy.R;
import com.guozha.buy.fragment.MenuDetailDescriptFragment;
import com.guozha.buy.fragment.MenuDetailFoodFragment;
import com.guozha.buy.fragment.MenuDetailStepFragment;
import com.guozha.buy.view.ViewPagerTab;

/**
 * 菜谱详情
 * @author PeggyTong
 *
 */
public class MenuDetailActivity extends FragmentActivity{
	
	private ViewPagerTab mViewPagerTab;
	private ViewPagerAdapter mViewPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_detail);
		//customActionBarStyle("菜谱详情");
		initView();
	}
	
	private void initView(){
		setUpViewPage();
		setUpTab();
		
	}
	
	private void setUpViewPage(){
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.menu_detail_viewpager);
		mViewPager.setAdapter(mViewPagerAdapter);
	}
	
	private void setUpTab(){
		mViewPagerTab = (ViewPagerTab) findViewById(R.id.viewpager_tab);
		mViewPagerTab.setViewPager(mViewPager);
		ImageView childView = new ImageView(this);
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
			fragments = new Class[]{
				MenuDetailDescriptFragment.class, 
				MenuDetailFoodFragment.class,
				MenuDetailStepFragment.class
			};
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
