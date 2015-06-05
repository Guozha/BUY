package com.guozha.buy.controller.found.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.guozha.buy.controller.found.FoundSubjectDetailActivity;

/**
 * 发现-专题
 * @author PeggyTong
 *
 */
public class FoundSubjectFragment extends BaseFragment implements OnScrollListener{
	
	private static final String PAGE_NAME = "FoundSubjectPage";
	private ListView mSubjectListView;
	private FoundSubjectListAdapter mSubjectListAdapter;
	
	private View mBottomLoadingView; 		//底部刷新视图
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_found_subject, container, false);
		initView(view); 
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
		
		mSubjectListAdapter = new FoundSubjectListAdapter(getActivity());
		mSubjectListView.setAdapter(mSubjectListAdapter);
		
		mSubjectListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), FoundSubjectDetailActivity.class);
				startActivity(intent);
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
					&& mLastVisibaleIndex == mSubjectListAdapter.getCount() 
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
		mLastVisibaleIndex = firstVisibleItem + visibleItemCount - 1;
		
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
		
	}
	//////////////////////////--分页加载END--/////////////////////////////

}
