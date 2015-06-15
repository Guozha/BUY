package com.guozha.buy.controller.found.fragment;

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
import com.guozha.buy.adapter.FoundSubjectListAdapter;
import com.guozha.buy.controller.BaseFragment;
import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.controller.found.FoundSubjectDetailActivity;
import com.guozha.buy.entry.found.FoundSubject;
import com.guozha.buy.entry.found.FoundSubjectPage;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.FoundModel;
import com.guozha.buy.model.result.FoundModelResult;
import com.guozha.buy.util.LogUtil;

/**
 * 发现-专题
 * @author PeggyTong
 *
 */
public class FoundSubjectFragment extends BaseFragment implements OnScrollListener{
	
	private static final String PAGE_NAME = "FoundSubjectPage";
	
	private static final int HAND_SUBJECT_COMPLETED = 0x0001;
	private ListView mSubjectListView;
	private FoundSubjectListAdapter mSubjectListAdapter;
	
	/**
	 * 分页相关
	 */
	private int mLastVisibleIndex;  //可见的最后一条数据
	private int mMaxDateNum;		//最大数据数
	private int mMaxPageSize;		//最大页数
	private int mCurrentPage;		//当前页
	
	private View mBottomLoadingView; 		//底部刷新视图
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	private BitmapCache mBitmapCache = CustomApplication.getBitmapCache();
	private List<FoundSubject> mSubjectItems = new ArrayList<FoundSubject>();
	private FoundModel mFoundModel = new FoundModel(new MyFoundModelResult());
	
	private Handler mHander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_SUBJECT_COMPLETED:
				mSubjectListAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_found_subject, container, false);
		initView(view); 
		initData();
		return view;
	}
	
	private void initView(View view){
		mSubjectListView = (ListView) view.findViewById(R.id.found_subject_list);
		mSubjectListView.setItemsCanFocus(true);
		
		//添加footerView
		mBottomLoadingView = getActivity().getLayoutInflater()
				.inflate(R.layout.list_paging_bottom, null);
		mBottomLoadingView.setBackgroundColor(getActivity().getResources().getColor(R.color.color_app_base_6));
		mLoadText = (TextView) mBottomLoadingView.findViewById(R.id.list_paging_bottom_text);
		mLoadProgressBar = (ProgressBar) 
				mBottomLoadingView.findViewById(R.id.list_paging_bottom_progressbar);
		mSubjectListView.addFooterView(mBottomLoadingView);
		
		mSubjectListAdapter = new FoundSubjectListAdapter(getActivity(), mSubjectItems, mBitmapCache);
		mSubjectListView.setAdapter(mSubjectListAdapter);
		mSubjectListView.setOnScrollListener(this);
		mSubjectListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FoundSubject foundSubject = mSubjectItems.get(position);
				Intent intent = new Intent(getActivity(), FoundSubjectDetailActivity.class);
				intent.putExtra("subjectId", foundSubject.getSubjectId());
				intent.putExtra("subjectName", foundSubject.getSubjectName());
				intent.putExtra("subjectDescript", foundSubject.getSubjectDesc());
				startActivity(intent);
			}
		});
	}
	
	private void initData(){
		mLastVisibleIndex = 0;  //可见的最后一条数据
		mMaxDateNum = 0;		//最大数据数
		mMaxPageSize = 0;
		mCurrentPage = 0;
		mSubjectItems.clear();
		requestFoundSubjectList();
	}
	
	private void requestFoundSubjectList(){
		mFoundModel.requestFoundSubjectList(getActivity(), mCurrentPage + 1);
	}
	
	//////////////////////////--分页加载相关--/////////////////////////////

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& mLastVisibleIndex == mSubjectListAdapter.getCount() 
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
		requestFoundSubjectList();
	}
	//////////////////////////--分页加载END--/////////////////////////////
	
	class MyFoundModelResult extends FoundModelResult{
		@Override
		public void requestFoundSubjectListResult(
				FoundSubjectPage foundSubjectPage) {
			if(foundSubjectPage == null) return;
			List<FoundSubject> foundSubjects = foundSubjectPage.getSubjectList();
			if(foundSubjects == null) return;
			mSubjectItems.addAll(foundSubjects);
			mCurrentPage++;
			mMaxPageSize = foundSubjectPage.getPageCount();
			mMaxDateNum = foundSubjectPage.getTotalCount();
			LogUtil.e("maxDateNum == " + mMaxDateNum);
			LogUtil.e("maxPageSize == " + mMaxPageSize);
			mHander.sendEmptyMessage(HAND_SUBJECT_COMPLETED);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
	}

}
