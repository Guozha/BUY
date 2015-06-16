package com.guozha.buy.controller.found;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.MenuItemGridAdapter;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.entry.found.FoundMenu;
import com.guozha.buy.entry.found.FoundMenuPage;
import com.guozha.buy.model.MenuModel;
import com.guozha.buy.model.result.MenuModelResult;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.LogUtil;

/**
 * 菜谱列表
 * @author PeggyTong
 *
 */
public class MenuItemListActivity extends BaseActivity implements OnScrollListener{
	
	private static final int HAND_FOUND_MENU_LIST_COMPLETED = 0x0001;
	
	private GridView mMenuItemGrid;
	private MenuItemGridAdapter mItemGridAdapter;
	private int mMenuTypeId;
	private String mMenuTypeName;
	private List<FoundMenu> mFoundMenus = new ArrayList<FoundMenu>();
	private int mGridSpace;
	private MenuModel mMenuModel = new MenuModel(new MyMenuModelResult());
	
	private Handler mHander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_FOUND_MENU_LIST_COMPLETED:
				mItemGridAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menulist);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			mMenuTypeId = bundle.getInt("menuTypeId");
			mMenuTypeName = bundle.getString("menuTypeName");
		}
		mGridSpace = DimenUtil.dp2px(this, 10);
		customActionBarStyle(mMenuTypeName);
		initView();
		initData();
	}
	
	private void initView(){
		mMenuItemGrid = (GridView) findViewById(R.id.menuitem_grid);
		mMenuItemGrid.setHorizontalSpacing(mGridSpace);
		mMenuItemGrid.setVerticalSpacing(mGridSpace);
		mMenuItemGrid.setPadding(mGridSpace, 0, mGridSpace, 0);
		
		mItemGridAdapter = new MenuItemGridAdapter(this, mGridSpace);
		mMenuItemGrid.setAdapter(mItemGridAdapter);
		
		mMenuItemGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
	
	private void initData(){
		mLastVisibleIndex = 0;  //可见的最后一条数据
		mMaxDateNum = 0;		//最大数据数
		mMaxPageSize = 0;
		mCurrentPage = 0;
		mFoundMenus.clear();
		requestFoundMenuList();
	}
	
	private void requestFoundMenuList(){
		mMenuModel.requestFoundMenuList(this, mCurrentPage + 1, mMenuTypeId);
	}
	
	//////////////////////////--分页加载相关--/////////////////////////////
	private int mLastVisibleIndex; //可见的最大索引
	private int mMaxDateNum;		//最大条数
	private int mMaxPageSize;		//最大页数
	private int mCurrentPage;		//当前页

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& mLastVisibleIndex == mItemGridAdapter.getCount() 
					&& mCurrentPage < mMaxPageSize){
			loadNextPageData();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//计算最后可见条目的索引
		mLastVisibleIndex = firstVisibleItem + visibleItemCount;
		
		//如果现有条目和最大数相等，则移除底部
		if(totalItemCount == mMaxDateNum + 1){
		}
		
	}
	
	/**
	 * 加载下一页数据
	 */
	private void loadNextPageData(){
		requestFoundMenuList();
	}
	//////////////////////////--分页加载END--/////////////////////////////
	
	class MyMenuModelResult extends MenuModelResult{
		@Override
		public void requestFoundMenuListResult(FoundMenuPage foundMenuPage) {
			if(foundMenuPage == null) return;
			List<FoundMenu> foundMenus = foundMenuPage.getMenuList();
			if(foundMenus == null) return;
			mFoundMenus.addAll(foundMenus);
			
			mCurrentPage++;
			mMaxPageSize = foundMenuPage.getPageCount();
			mMaxDateNum = foundMenuPage.getTotalCount();
			LogUtil.e("maxDateNum == " + mMaxDateNum);
			LogUtil.e("maxPageSize == " + mMaxPageSize);
			mHander.sendEmptyMessage(HAND_FOUND_MENU_LIST_COMPLETED);
		}
	}
}
