package com.guozha.buy.controller.found;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.LoginActivity;
import com.guozha.buy.controller.MainActivity;
import com.guozha.buy.controller.found.fragment.BaseMenuDetailFragment;
import com.guozha.buy.controller.found.fragment.MenuDetailDescriptFragment;
import com.guozha.buy.controller.found.fragment.MenuDetailFoodFragment;
import com.guozha.buy.controller.found.fragment.MenuDetailStepFragment;
import com.guozha.buy.controller.mine.AddAddressActivity;
import com.guozha.buy.entry.found.menu.MenuDetail;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.MenuModel;
import com.guozha.buy.model.ShopCartModel;
import com.guozha.buy.model.result.MenuModelResult;
import com.guozha.buy.model.result.ShopCartModelResult;
import com.guozha.buy.server.ShareManager;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.view.ViewPagerTab;

/**
 * 菜谱详情
 * @author PeggyTong
 *
 */
public class MenuDetailActivity extends FragmentActivity implements OnClickListener{
	
	private ViewPagerTab mViewPagerTab;
	private ViewPagerAdapter mViewPagerAdapter;
	private ViewPager mViewPager;
	private List<TextView> mTabTexts;
	private MenuDetail mMenuDetail;
	private MenuModel mMenuModel = new MenuModel(new MyMenuModelResult());
	private ShopCartModel mShopCartModel = new ShopCartModel(new MyShopCartModelResult());
	private int mMenuId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_detail);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			mMenuId = bundle.getInt("menuId");
		}
		customActionBarStyle("菜谱详情");
		initView();
		initData();
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
		findViewById(R.id.menu_collection_button).setOnClickListener(this);
		findViewById(R.id.menu_addcart_button).setOnClickListener(this);
	}
	
	private void initData(){
		mMenuModel.requestMenuDetail(this, mMenuId);
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
		
		BaseMenuDetailFragment[] fragments;
		
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new BaseMenuDetailFragment[]{
				new MenuDetailDescriptFragment(), 
				new MenuDetailFoodFragment(),
				new MenuDetailStepFragment()
			};
		}

		@Override
		public Fragment getItem(int position) {
			return fragments[position];
			
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
	
	class MyMenuModelResult extends MenuModelResult{
		@Override
		public void requestMenuDetailResult(MenuDetail menuDetail) {
			final BaseMenuDetailFragment[] fragments = mViewPagerAdapter.fragments;
			for(int i = 0; i < fragments.length; i++){
				fragments[i].sendMenuDetailData(menuDetail);
			}
			mMenuDetail = menuDetail;
		}
		
		@Override
		public void requestCollectionMenuResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				ToastUtil.showCollectionAnim(MenuDetailActivity.this);
			}else{
				ToastUtil.showToast(MenuDetailActivity.this, msg);
			}
		}
	}
	
	class MyShopCartModelResult extends ShopCartModelResult{
		@Override
		public void requestCartAddMenuResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				ToastUtil.showTopAddCartAnim(MenuDetailActivity.this);
			}else{
				ToastUtil.showToast(MenuDetailActivity.this, msg);
			}
		}
	}

	@Override
	public void onClick(View view) {
		String token = ConfigManager.getInstance().getUserToken(this);
		if(token == null) return;
		int addressId = ConfigManager.getInstance().getChoosedAddressId(this);
		if(addressId == -1) return;
		int userId = ConfigManager.getInstance().getUserId();
		
		switch (view.getId()) {
		case R.id.menu_collection_button:
			mMenuModel.requestCollectionMenu(
					MenuDetailActivity.this, token, userId, mMenuId);
			break;
		case R.id.menu_addcart_button:
			Set<String> checkedIds = mViewPagerAdapter.fragments[1].getCheckedIds();
			LogUtil.e("checkedIds = " + checkedIds);
			mShopCartModel.requestCartAddMenu(
					MenuDetailActivity.this, userId, mMenuId, token, addressId, checkedIds);
			break;
		default:
			break;
		}
	}
}
