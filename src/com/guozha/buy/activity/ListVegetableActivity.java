package com.guozha.buy.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.guozha.buy.R;
import com.guozha.buy.adapter.VegetableListAdapter;
import com.guozha.buy.entry.VegetableInfo;

/**
 * 蔬菜详细列表
 * @author PeggyTong
 *
 */
public class ListVegetableActivity extends BaseActivity implements OnScrollListener{
	
	private static final int MAX_DATA_NUM = 100;
	
	private int mMaxDateNum; //最大数据数
	
	private ListView mListView;
	private View mBottomLoadingView;  //底部刷新视图
	
	private Button mLoadButton;
	private ProgressBar mLoadProgressBar;
	
	private List<VegetableInfo> mListData;
	private VegetableListAdapter mVegetableAdapter;
	
	private int mLastVisibleIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_vegetable);
		
		mMaxDateNum = MAX_DATA_NUM;
		
		mListView = (ListView) findViewById(R.id.list_vegetable);
		mBottomLoadingView = getLayoutInflater().inflate(R.layout.list_paging_bottom, null);
		
		mLoadButton = (Button) mBottomLoadingView.findViewById(R.id.bt_load);
		mLoadProgressBar = (ProgressBar) mBottomLoadingView.findViewById(R.id.pg);
		
		initData();
		
		mVegetableAdapter = new VegetableListAdapter(this, mListData);
		mListView.addFooterView(mBottomLoadingView);
		mListView.setAdapter(mVegetableAdapter);
		
		mListView.setOnScrollListener(this);
		/*
		mLoadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				loadNewDataAndUpdate();
			}
			
		});
		*/
		
	}
	
	/**
	 * 加载新数据并且更新界面
	 */
	private void loadNewDataAndUpdate() {
		mLoadProgressBar.setVisibility(View.VISIBLE);
		mLoadButton.setVisibility(View.GONE);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				loadMoreData();  //加载更多的数据
				mLoadButton.setVisibility(View.VISIBLE);
				mLoadProgressBar.setVisibility(View.GONE);
				mVegetableAdapter.notifyDataSetChanged();
			}
		}, 2000);
	}
	
	private void loadMoreData(){
		int count = mVegetableAdapter.getCount();
		VegetableInfo info;
		if(count + 5 < mMaxDateNum){
			for(int i = 0; i < 5; i++){
				info = new VegetableInfo();
				info.setName("name_" + i);
				info.setDescription("description_" + i);
				mListData.add(info);
			}
		}else{
			for (int i = count; i < mMaxDateNum; i++) {
				info = new VegetableInfo();
				info.setName("name_" + i);
				info.setDescription("description_" + i);
				mListData.add(info);
            }
		}
	}
	
	private void initData(){
		mListData = new ArrayList<VegetableInfo>();
		
		VegetableInfo info;
		for(int i = 0; i < 20; i++){
			info = new VegetableInfo();
			info.setName("name_" + i);
			info.setDescription("description_" + i);
			mListData.add(info);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, 
			int visibleItemCount, int totalItemCount) {
		//计算最后可见条目的索引
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		
		//所有的条目已经和最大数相等，则移除底部的View
		if(totalItemCount == mMaxDateNum + 1){
			mListView.removeFooterView(mBottomLoadingView);
			Toast.makeText(this, "数据全部加载完毕，没有更多数据!", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		//当滑动到底部后自动加载
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex == mVegetableAdapter.getCount()){
			loadNewDataAndUpdate();
		}
	}
}
