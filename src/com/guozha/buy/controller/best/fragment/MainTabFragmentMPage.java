package com.guozha.buy.controller.best.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.MPageListAdapter;
import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.controller.found.MenuDetailActivity;
import com.guozha.buy.entry.mpage.BestMenuItem;
import com.guozha.buy.entry.mpage.BestMenuPage;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.MenuModel;
import com.guozha.buy.model.result.MenuModelResult;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMPage extends MainTabBaseFragment implements OnScrollListener{
	
	private static final String PAGE_NAME = "MainPage";
	
	private static final int HAND_BEST_LIST_COMPLETED = 0x0001;
	private ListView mListView;
	private MPageListAdapter mMPageListAdpater;
	
	private View mBottomLoadingView; 		//底部刷新视图
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	private BitmapCache mBitmapCache = BitmapCache.getInstance();
	private List<BestMenuItem> mBestMenuItems = new ArrayList<BestMenuItem>();
	private MenuModel mMenuModel = new MenuModel(new MyMenuModelResult());
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_BEST_LIST_COMPLETED:
				if(mMPageListAdpater != null){
					mMPageListAdpater.notifyDataSetChanged();
				}
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_mpage, container, false);
		initActionBar("爱掌勺");
		initView(view);
		initData();
		return view;
	}
	
	/**
	 * 初始化视图
	 * @param view
	 */
	private void initView(View view){
		if(view == null) return;
		mListView = (ListView) view.findViewById(R.id.mpage_list);
		//添加footerView
		mBottomLoadingView = getActivity().getLayoutInflater()
				.inflate(R.layout.list_paging_bottom, null);
		mBottomLoadingView.setBackgroundColor(getActivity().getResources().getColor(R.color.color_app_base_6));
		mLoadText = (TextView) mBottomLoadingView.findViewById(R.id.list_paging_bottom_text);
		mLoadProgressBar = (ProgressBar) 
				mBottomLoadingView.findViewById(R.id.list_paging_bottom_progressbar);
		mListView.addFooterView(mBottomLoadingView);
		
		mMPageListAdpater = new MPageListAdapter(getActivity(), mBestMenuItems, mBitmapCache);
		mListView.setAdapter(mMPageListAdpater);
		mListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), MenuDetailActivity.class);
				intent.putExtra("menuId", mBestMenuItems.get(position).getMenuId());
				startActivity(intent);
			}
		});
	}
	
	private void initData(){
		mMaxPageSize = 0;     //总的页数
		mMaxDateNum = 0;		//最大数据数
		mCurrentPage = 0;
		mLastVisibleIndex = 0;  //可见的最后一条数据
		mBestMenuItems.clear();
		requestBestMenuList();
	}
	
	private void requestBestMenuList(){
		mMenuModel.requestBestMenuList(getActivity(), mCurrentPage + 1);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
			//测试服提示
		}else{
			//View不可见
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
	
	//////////////////////////--分页加载相关--/////////////////////////////
	private int mLastVisibleIndex; //可见的最大索引
	private int mMaxDateNum;		//最大条数
	private int mMaxPageSize;		//最大页数
	private int mCurrentPage;		//当前页

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& mLastVisibleIndex == mMPageListAdpater.getCount() 
					&& mCurrentPage < mMaxPageSize){
			mLoadProgressBar.setVisibility(View.VISIBLE);
			mLoadText.setVisibility(View.GONE);
			loadNextPageData();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//计算最后可见条目的索引
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		
		//如果现有条目和最大数相等，则移除底部
		if(totalItemCount == mMaxDateNum + 1){
			mLoadProgressBar.setVisibility(View.GONE);
			mLoadText.setVisibility(View.VISIBLE);
		}
		
	}
	
	/**
	 * 加载下一页数据
	 */
	private void loadNextPageData(){
		requestBestMenuList();
	}
	//////////////////////////--分页加载END--/////////////////////////////
	
	@Override
	public void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
	}
	class MyMenuModelResult extends MenuModelResult{
		@Override
		public void requestBestMenuPageResult(BestMenuPage bestMenuPage) {
			if(bestMenuPage == null) return;
			List<BestMenuItem> bestMenuItem = bestMenuPage.getMenuPickList();
			if(bestMenuItem == null) return;
			mBestMenuItems.addAll(bestMenuItem);
			mCurrentPage++;
			mMaxPageSize = bestMenuPage.getPageCount();
			mMaxDateNum = bestMenuPage.getTotalCount();
			mHandler.sendEmptyMessage(HAND_BEST_LIST_COMPLETED);
		}
	}
}
