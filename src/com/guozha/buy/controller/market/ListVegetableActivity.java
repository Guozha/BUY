package com.guozha.buy.controller.market;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.VegetableListAdapter;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.entry.market.ItemSaleInfo;
import com.guozha.buy.entry.market.ItemSaleInfoPage;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.GoodsModel;
import com.guozha.buy.model.result.GoodsModelResult;
import com.umeng.analytics.MobclickAgent;

/**
 * 蔬菜详细列表
 * @author PeggyTong
 *
 */
public class ListVegetableActivity extends BaseActivity implements OnScrollListener{
	
	private static final String PAGE_NAME = "ListVegetable";
	
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private int mMaxDateNum; //最大数据数
	private BitmapCache mBitmapCache = BitmapCache.getInstance();
	
	private ListView mListView;
	private View mBottomLoadingView;  //底部刷新视图
	
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	
	private List<ItemSaleInfo[]> mAdapterData;
	private VegetableListAdapter mVegetableAdapter;
	
	private int mLastVisibleIndex;		//最后一个可见列表的下标
	
	private String mFrontTypeId;		//菜谱类别id
	private String mFrontTypeName;		//菜谱类别名称
	
	private GoodsModel mGoodsModel;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				mVegetableAdapter.notifyDataSetChanged();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_vegetable);
		
		//获取商品类别
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mFrontTypeId = bundle.getString("frontTypeId");
				mFrontTypeName = bundle.getString("frontTypeName");
			}
		}
		customActionBarStyle(mFrontTypeName);
		
		mListView = (ListView) findViewById(R.id.list_vegetable);
		mBottomLoadingView = getLayoutInflater().inflate(R.layout.list_paging_bottom, null);
		
		mLoadText = (TextView) mBottomLoadingView.findViewById(R.id.list_paging_bottom_text);
		mLoadProgressBar = (ProgressBar) mBottomLoadingView.findViewById(R.id.list_paging_bottom_progressbar);
		
		mAdapterData = new ArrayList<ItemSaleInfo[]>();
		mVegetableAdapter = new VegetableListAdapter(this, mAdapterData, mListView, mBitmapCache);
		mListView.addFooterView(mBottomLoadingView);
		mListView.setAdapter(mVegetableAdapter);
		mListView.setOnScrollListener(this);
		mGoodsModel = new GoodsModel(new MyGoodsModelResult());
		requestNewData();
	}
	
	/**
	 * 加载新数据并且更新界面
	 */
	private void loadNewDataAndUpdate() {
		mLoadProgressBar.setVisibility(View.VISIBLE);
		mLoadText.setVisibility(View.GONE);
		requestNewData();
	}

	
	/**
	 * 适配数据
	 */
	private void addFormatData(List<ItemSaleInfo> vegetables){
		ItemSaleInfo[] infos;
		int count = vegetables.size() / 3;

		for(int i = 0; i < count; i++){
			infos = new ItemSaleInfo[3];
			infos[0] = vegetables.get(i * 3);
			infos[1] = vegetables.get(i * 3 + 1);
			infos[2] = vegetables.get(i * 3 + 2);
			mAdapterData.add(infos);
		}
		
		int remain = vegetables.size() % 3;
		
		if(remain == 0){
			handler.sendEmptyMessage(HAND_DATA_COMPLETED);
			return;
		}
		infos = new ItemSaleInfo[3];
		ItemSaleInfo info1;
		ItemSaleInfo info2;
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
		handler.sendEmptyMessage(HAND_DATA_COMPLETED);
	}
	
	/**
	 * 向服务器请求数据
	 */
	private void requestNewData(){
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		mGoodsModel.requestTypeGoodsList(this, Integer.parseInt(mFrontTypeId), addressId, mCurrentPage + 1);
	}
	
	private int mCurrentPage = 0;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, 
			int visibleItemCount, int totalItemCount) {
		//计算最后可见条目的索引
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		
		//所有的条目已经和最大数相等，则移除底部的View
		int maxList = (mMaxDateNum + 2) / 3;
		if(totalItemCount == maxList + 1){
			mLoadText.setVisibility(View.VISIBLE);
			mLoadProgressBar.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		//当滑动到底部后自动加载
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex == mVegetableAdapter.getCount()){
			mLoadText.setVisibility(View.GONE);
			mLoadProgressBar.setVisibility(View.VISIBLE);
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
		mBitmapCache.fluchCache();
		//友盟页面统计代码
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
	
	class MyGoodsModelResult extends GoodsModelResult{
		@Override
		public void requestTypeGoodsListResult(ItemSaleInfoPage itemSaleInfoPage) {
			if(itemSaleInfoPage == null) return;
			mMaxDateNum = itemSaleInfoPage.getTotalCount();
			itemSaleInfoPage.getPageCount();
			List<ItemSaleInfo> vegetables = itemSaleInfoPage.getGoodsList();
			if(vegetables == null) return;
			mCurrentPage++;
			addFormatData(vegetables);
		}
	}
}
