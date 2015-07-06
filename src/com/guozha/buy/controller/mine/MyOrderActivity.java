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
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.mine.fragment.OrderFinishedFragment;
import com.guozha.buy.controller.mine.fragment.OrderUnFinishFragment;
import com.guozha.buy.view.ViewPagerTab;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的订单
 * @author PeggyTong
 *
 */
public class MyOrderActivity extends FragmentActivity{
	
	private static final String PAGE_NAME = "我的订单";
	
	private ViewPager mViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	private ViewPagerTab mViewPagerTab;
	private List<TextView> mTabTexts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
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
		actionbar.setTitle(PAGE_NAME);
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
			fragments = new Class[]{OrderUnFinishFragment.class, OrderFinishedFragment.class};
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
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);       
		MobclickAgent.onPageStart("MainScreen"); 
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("MainScreen"); 
	}

}
