package com.guozha.buy.controller.found;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.MainActivity;
import com.guozha.buy.controller.found.fragment.MenuDetailDescriptFragment;
import com.guozha.buy.controller.found.fragment.MenuDetailFoodFragment;
import com.guozha.buy.controller.found.fragment.MenuDetailStepFragment;
import com.guozha.buy.server.ShareManager;
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
	private List<TextView> mTabTexts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_detail);
		customActionBarStyle("菜谱详情");
		initView();
	}
	
	/**
	 * 设置ActionBar
	 * @return
	 */
	protected ActionBar customActionBarStyle(String title){
		ActionBar actionbar = getActionBar();
		if(actionbar == null) return null;
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(false);
		actionbar.setTitle(title);
		return actionbar;
	}
	
	private void initView(){
		setUpViewPage();
		mTabTexts = new ArrayList<TextView>();
		mTabTexts.add((TextView) findViewById(R.id.menu_detail_frag1_tab));
		mTabTexts.add((TextView) findViewById(R.id.menu_detail_frag2_tab));
		mTabTexts.add((TextView) findViewById(R.id.menu_detail_frag3_tab));
		setUpTab();
	}
	
	private void setUpViewPage(){
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.menu_detail_viewpager);
		mViewPager.setAdapter(mViewPagerAdapter);
	}
	
	private void setUpTab(){
		mViewPagerTab = (ViewPagerTab) findViewById(R.id.viewpager_tab);
		mViewPagerTab.setViewPageAndTabTexts(mViewPager, mTabTexts);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_detail_actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		case R.id.action_share:
			ShareManager shareManager = new ShareManager(MenuDetailActivity.this);
			shareManager.showSharePlatform(this);
			break;
		case R.id.action_cart:
			Intent intent = new Intent(MenuDetailActivity.this, MainActivity.class);
			intent.putExtra("fragmentIndex", MainActivity.FRAGMENT_CART_INDEX);
			intent.putExtra("canback", true);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
