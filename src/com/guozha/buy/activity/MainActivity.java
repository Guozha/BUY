package com.guozha.buy.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.guozha.buy.R;
import com.guozha.buy.fragment.MainTabFragmentCart;
import com.guozha.buy.fragment.MainTabFragmentGoods;
import com.guozha.buy.fragment.MainTabFragmentMPage;
import com.guozha.buy.fragment.MainTabFragmentMine;
import com.guozha.buy.view.ChangeColorIconWithText;

/**
 * Ӧ��������
 * @author lixiaoqiang
 *
 */
public class MainActivity extends FragmentActivity{
	
	private ViewPager mViewPager;
	private MyFragmentPagerAdapter mFragmentPagerAdapter;
	
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
	private ClickTabItemListener mClickTabItemListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initActionBar(getActionBar());
		initFragment();
		initTabIndicators();
		
		mViewPager = (ViewPager) findViewById(R.id.main_viewpage);
		mViewPager.setOnPageChangeListener(new PagerChangeListener());
		mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mFragmentPagerAdapter);
	}
	
	/**
	 * ��ʼ��ActionBar
	 * @param actionbar
	 */
	private void initActionBar(ActionBar actionbar) {
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(false);
	}
	
	/**
	 * ��ʼ��Fragment
	 */
	private void initFragment(){
		mFragments.add(new MainTabFragmentMPage());
		mFragments.add(new MainTabFragmentGoods());
		mFragments.add(new MainTabFragmentCart());
		mFragments.add(new MainTabFragmentMine());
	}
	
	/**
	 * ��ʼ��Tab
	 */
	private void initTabIndicators(){
		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);
		mClickTabItemListener = new ClickTabItemListener();
		one.setOnClickListener(mClickTabItemListener);
		two.setOnClickListener(mClickTabItemListener);
		three.setOnClickListener(mClickTabItemListener);
		four.setOnClickListener(mClickTabItemListener);

		one.setIconAlpha(1.0f);
	}
	
	/**
	 * �ײ�Tab���¼���
	 * @author Administrator
	 *
	 */
	class ClickTabItemListener implements OnClickListener{
		@Override
		public void onClick(View view) {
			clickTab(view);
		}
	}
	
	/**
	 * ����������TabIndicator����ɫ
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicators.size(); i++) {
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}
	
	/**
	 * ���Tab��ť
	 * 
	 * @param view
	 */
	private void clickTab(View view) {
		resetOtherTabs();
		switch (view.getId()) {
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		}
	}

	/**
	 * ������Fragment������
	 * @author lixiaoqiang
	 *
	 */
	class MyFragmentPagerAdapter extends FragmentPagerAdapter{

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}
		
		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}
	}
	
	/**
	 * ���滬������
	 * @author lixiaoqiang
	 *
	 */
	class PagerChangeListener implements OnPageChangeListener{	

		@Override
		public void onPageScrollStateChanged(int arg0) { }

		@Override
		public void onPageSelected(int arg0) { }

		@Override
		public void onPageScrolled(int position, 
				float positionOffset, int positionOffsetPixels) {
			if (positionOffset > 0) {
				ChangeColorIconWithText left = mTabIndicators.get(position);
				ChangeColorIconWithText right = mTabIndicators.get(position + 1);
				left.setIconAlpha(1 - positionOffset);
				right.setIconAlpha(positionOffset);
			}
		}
	}
	
}
