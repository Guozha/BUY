package com.guozha.buy.controller.mine;

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
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.guozha.buy.R;
import com.guozha.buy.controller.mine.fragment.OrderFinishedFragment;
import com.guozha.buy.controller.mine.fragment.OrderUnFinishFragment;
import com.guozha.buy.view.ViewPagerTab;

/**
 * 我的订单
 * @author PeggyTong
 *
 */
public class MyOrderActivity extends FragmentActivity{
	
	private ViewPager mViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	private ViewPagerTab mViewPagerTab;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		setActionBar();
		setUpViewPage();
		setUpTab();
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
		actionbar.setTitle("我的订单");
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
	
	private void setUpViewPage(){
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mViewPagerAdapter);
	}
	
	private void initView(){
		findViewById(R.id.my_collection_frag1_tab).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mViewPager.getCurrentItem() == 0) return;
				mViewPager.setCurrentItem(0);
			}
		});
		
		findViewById(R.id.my_collection_frag2_tab).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mViewPager.getCurrentItem() == 1) return;
				mViewPager.setCurrentItem(1);
			}
		});
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

}
