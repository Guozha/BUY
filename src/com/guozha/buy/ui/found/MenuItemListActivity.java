package com.guozha.buy.ui.found;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.MenuItemGridAdapter;
import com.guozha.buy.ui.BaseActivity;
import com.guozha.buy.util.DimenUtil;

/**
 * 菜谱列表
 * @author PeggyTong
 *
 */
public class MenuItemListActivity extends BaseActivity implements OnScrollListener{
	
	private GridView mMenuItemGrid;
	private MenuItemGridAdapter mItemGridAdapter;
	
	private int mGridSpace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menulist);
		mGridSpace = DimenUtil.dp2px(this, 10);
		customActionBarStyle("xxx");
		initView();
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
	
	//////////////////////////--分页加载相关--/////////////////////////////
	private int mLastVisibaleIndex; //可见的最大索引
	private int mMaxDateNum;		//最大条数
	private int mMaxPageSize;		//最大页数
	private int mCurrentPage;		//当前页

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& mLastVisibaleIndex == mItemGridAdapter.getCount() 
					&& mCurrentPage < mMaxPageSize){
			loadNextPageData();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//计算最后可见条目的索引
		mLastVisibaleIndex = firstVisibleItem + visibleItemCount;
		
		//如果现有条目和最大数相等，则移除底部
		if(totalItemCount == mMaxDateNum + 1){
		}
		
	}
	
	/**
	 * 加载下一页数据
	 */
	private void loadNextPageData(){
		
	}
	//////////////////////////--分页加载END--/////////////////////////////
}
