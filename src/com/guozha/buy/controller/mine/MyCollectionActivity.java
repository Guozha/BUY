package com.guozha.buy.controller.mine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.guozha.buy.R;
import com.guozha.buy.controller.mine.fragment.CollectionRecipeFragment;
import com.guozha.buy.controller.mine.fragment.CollectionVegetableFragment;
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
	private List<TextView> mTabTexts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		setActionBar();
		initView();
	}

	/**
	 * 设置ActionBar
	 */
	private void setActionBar() {
		ActionBar actionbar = getActionBar();
		if(actionbar == null) return;
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(false);
		actionbar.setTitle("我的收藏");
	}
	
	
	
	private void setUpTab(){
		mViewPagerTab = (ViewPagerTab) findViewById(R.id.viewpager_tab);
		mViewPagerTab.setViewPageAndTabTexts(mViewPager, mTabTexts);
	}
	
	private void setUpViewPage(){
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mViewPagerAdapter);
	}
	
	private void initView(){
		setUpViewPage();
		mTabTexts = new ArrayList<TextView>();
		mTabTexts.add((TextView) findViewById(R.id.my_collection_frag1_tab));
		mTabTexts.add((TextView) findViewById(R.id.my_collection_frag2_tab));
		setUpTab();
	}
	
	class ViewPagerAdapter extends FragmentPagerAdapter{
		
		@SuppressWarnings("rawtypes")
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
