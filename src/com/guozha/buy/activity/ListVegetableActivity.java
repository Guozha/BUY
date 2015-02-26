package com.guozha.buy.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guozha.buy.R;
import com.guozha.buy.adapter.VegetableListAdapter;
import com.guozha.buy.entry.VegetableInfo;
import com.umeng.analytics.MobclickAgent;

/**
 * 蔬菜详细列表
 * @author PeggyTong
 *
 */
public class ListVegetableActivity extends BaseActivity implements OnScrollListener{
	
	private static final String PAGE_NAME = "ListVegetable";
	
	private static final int MAX_DATA_NUM = 100;
	private static final int LOADING_ITEM_COUNT = 18;
	
	private int mMaxDateNum; //最大数据数
	
	private ListView mListView;
	private View mBottomLoadingView;  //底部刷新视图
	
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	
	private List<VegetableInfo[]> mAdapterData;
	private VegetableListAdapter mVegetableAdapter;
	
	private int mLastVisibleIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_vegetable);
		
		mMaxDateNum = MAX_DATA_NUM;
		
		mListView = (ListView) findViewById(R.id.list_vegetable);
		mBottomLoadingView = getLayoutInflater().inflate(R.layout.list_paging_bottom, null);
		
		mLoadText = (TextView) mBottomLoadingView.findViewById(R.id.list_paging_bottom_text);
		mLoadProgressBar = (ProgressBar) mBottomLoadingView.findViewById(R.id.list_paging_bottom_progressbar);
		
		initData();
		mVegetableAdapter = new VegetableListAdapter(this, mAdapterData);
		mListView.addFooterView(mBottomLoadingView);
		mListView.setAdapter(mVegetableAdapter);
		
		mListView.setOnScrollListener(this);
		
	}
	
	/**
	 * 加载新数据并且更新界面
	 */
	private void loadNewDataAndUpdate() {
		mLoadProgressBar.setVisibility(View.VISIBLE);
		mLoadText.setVisibility(View.GONE);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				loadMoreData();  //加载更多的数据
				mLoadText.setVisibility(View.VISIBLE);
				mLoadProgressBar.setVisibility(View.GONE);
				mVegetableAdapter.notifyDataSetChanged();
			}
		}, 2000);
	}
	
	private void loadMoreData(){
		List<VegetableInfo> listData = new ArrayList<VegetableInfo>();
		int count = mVegetableAdapter.getCount() * 3;
		VegetableInfo info;
		if(count + LOADING_ITEM_COUNT < mMaxDateNum){
			for(int i = 0; i < LOADING_ITEM_COUNT; i++){
				info = new VegetableInfo();
				info.setImageId(R.drawable.vegetable_image);
				info.setVegetableName("产品名称" + (count + i));
				listData.add(info);
			}
		}else{
			for (int i = count; i < mMaxDateNum; i++) {
				info = new VegetableInfo();
				info.setImageId(R.drawable.vegetable_image);
				info.setVegetableName("产品名称" + i);
				listData.add(info);
            }
		}
		addFormatData(listData);
	}
	
	/**
	 * 适配数据
	 */
	private void addFormatData(List<VegetableInfo> vegetables){
		VegetableInfo[] infos;
		if(mAdapterData == null){
			mAdapterData = new ArrayList<VegetableInfo[]>();
		}
		int count = vegetables.size() / 3;

		for(int i = 0; i < count; i++){
			infos = new VegetableInfo[3];
			infos[0] = vegetables.get(i * 3);
			infos[1] = vegetables.get(i * 3 + 1);
			infos[2] = vegetables.get(i * 3 + 2);
			mAdapterData.add(infos);
		}
		
		int remain = vegetables.size() % 3;
		
		if(remain == 0) return;
		infos = new VegetableInfo[3];
		VegetableInfo info1;
		VegetableInfo info2;
		if(remain == 1){
			info1 = vegetables.get(count * 3);
			info2 = null;
		}else{
			info1 = vegetables.get(count * 3);
			info2 = vegetables.get(count * 3 + 1);
		}
		infos[0] = info1;
		infos[1] = info2;
		infos[2] = null;
		mAdapterData.add(infos);
		
	}
	
	private void initData(){
		List<VegetableInfo> listData = new ArrayList<VegetableInfo>();
		
		VegetableInfo info;
		for(int i = 0; i < LOADING_ITEM_COUNT; i++){
			info = new VegetableInfo();
			info.setImageId(R.drawable.vegetable_image);
			info.setVegetableName("产品名称" + i);
			listData.add(info);
		}
		addFormatData(listData);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, 
			int visibleItemCount, int totalItemCount) {
		//计算最后可见条目的索引
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		
		//所有的条目已经和最大数相等，则移除底部的View
		int maxList = (mMaxDateNum + 2) / 3;
		if(totalItemCount == maxList + 1){
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
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//友盟页面统计代码
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//友盟页面统计代码
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
}
