package com.guozha.buy.activity.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.guozha.buy.R;
import com.guozha.buy.fragment.CollectionRecipeFragment;
import com.guozha.buy.fragment.CollectionVegetableFragment;
import com.guozha.buy.view.ViewPagerTab;

/**
 * 我的收藏
 * @author PeggyTong
 *
 */
public class MyCollectionActivity extends FragmentActivity{
	
	private ViewPager mViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	private ViewPagerTab mViewPagerTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		//customActionBarStyle("我的收藏");
		setUpViewPage();
		setUpTab();
	}
	
	private void setUpTab(){
		mViewPagerTab = (ViewPagerTab) findViewById(R.id.viewpager_tab);
		mViewPagerTab.setViewPager(mViewPager);
		ImageView childView = new ImageView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		childView.setImageResource(R.drawable.line);
		childView.setLayoutParams(params);
		mViewPagerTab.addView(childView);
	}
	
	private void setUpViewPage(){
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mViewPagerAdapter);
	}
	
	class ViewPagerAdapter extends FragmentPagerAdapter{
		
		private Class[] fragments;
		
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new Class[]{CollectionRecipeFragment.class, CollectionVegetableFragment.class};
		}

		@Override
		public Fragment getItem(int position) {
			try {
				return (Fragment) fragments[position].newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
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
