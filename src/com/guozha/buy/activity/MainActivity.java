package com.guozha.buy.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.guozha.buy.R;
import com.guozha.buy.fragment.MainTabFragmentCart;
import com.guozha.buy.fragment.MainTabFragmentGoods;
import com.guozha.buy.fragment.MainTabFragmentMPage;
import com.guozha.buy.fragment.MainTabFragmentMine;
import com.guozha.buy.view.ChangeColorIconWithText;
import com.guozha.buy.view.CustomViewPager;
import com.guozha.buy.view.CustomViewPager.OnInterceptTouchListener;

/**
 * 应用主界面
 * @author lixiaoqiang
 *
 */
public class MainActivity extends FragmentActivity{
	
	private CustomViewPager mCustomViewPager;
	private MyFragmentPagerAdapter mFragmentPagerAdapter;
	
	private ClickTabItemListener mClickTabItemListener;
	
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	
	private List<ChangeColorIconWithText> mTabIndicators = 
			new ArrayList<ChangeColorIconWithText>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initActionBar(getActionBar());
		initFragment();
		initTabIndicators();
		initViewPager();
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		mCustomViewPager = (CustomViewPager) findViewById(R.id.main_viewpage);
		mCustomViewPager.setOnPageChangeListener(new PagerChangeListener());
		mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		mCustomViewPager.setAdapter(mFragmentPagerAdapter);
		
		mCustomViewPager.setOnInterceptTouchListener(new OnInterceptTouchListener() {
			
			@Override
			public boolean interceptTouched(float eventX, float eventY) {
				MainTabFragmentMPage mainTabFragmentMPage = 
						(MainTabFragmentMPage) mFragments.get(0);
				if(mainTabFragmentMPage == null) return true;
				int beginYPoint = mainTabFragmentMPage.getBeginYPoint();
				int endYPoint = mainTabFragmentMPage.getEndYPoint();
				if(eventY < beginYPoint || (eventY > endYPoint)) return true;
				return false;
			}
		});
	}
	
	/**
	 * 初始化ActionBar
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
	 * 初始化Fragment
	 */
	private void initFragment(){
		mFragments.add(new MainTabFragmentMPage());
		mFragments.add(new MainTabFragmentGoods());
		mFragments.add(new MainTabFragmentCart());
		mFragments.add(new MainTabFragmentMine());
	}
	
	/**
	 * 初始化Tab
	 */
	private void initTabIndicators(){
		ChangeColorIconWithText one = 
				(ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = 
				(ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = 
				(ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = 
				(ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);
		
		mClickTabItemListener = new ClickTabItemListener();
		one.setOnClickListener(mClickTabItemListener);
		two.setOnClickListener(mClickTabItemListener);
		three.setOnClickListener(mClickTabItemListener);
		four.setOnClickListener(mClickTabItemListener);

		one.setIconAlpha(1.0f);
	}
	
	/**
	 * 底部Tab按下监听
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
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicators.size(); i++) {
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}
	
	/**
	 * 点击Tab按钮
	 * 
	 * @param view
	 */
	private void clickTab(View view) {
		
		resetOtherTabs();
		
		switch (view.getId()) {
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mCustomViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mCustomViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mCustomViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mCustomViewPager.setCurrentItem(3, false);
			break;
		}
	}

	/**
	 * 主界面Fragment适配器
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
	 * 界面滑动监听
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
			if(positionOffset <= 0) return;
			
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}
}
