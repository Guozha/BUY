package com.guozha.buy.activity.market;

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
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.VegetableListAdapter;
import com.guozha.buy.entry.market.ItemSaleInfo;
import com.guozha.buy.entry.market.ItemSaleInfoPage;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 蔬菜详细列表
 * @author PeggyTong
 *
 */
public class ListVegetableActivity extends BaseActivity implements OnScrollListener{
	
	private static final String PAGE_NAME = "ListVegetable";
	
	private static final int PAGE_ITEM_COUNT = 24;
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private int mMaxDateNum; //最大数据数
	
	private ListView mListView;
	private View mBottomLoadingView;  //底部刷新视图
	
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	
	private List<ItemSaleInfo[]> mAdapterData;
	private VegetableListAdapter mVegetableAdapter;
	
	private int mLastVisibleIndex;		//最后一个可见列表的下标
	
	private String mFrontTypeId;		//菜谱类别id
	private String mFrontTypeName;		//菜谱类别名称
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				LogUtil.e("Vegetable_List_update");
				LogUtil.e("mAdapterData = " + mAdapterData.size());
				mVegetableAdapter.notifyDataSetChanged();
				break;

			default:
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
		mVegetableAdapter = new VegetableListAdapter(this, mAdapterData, mListView);
		mListView.addFooterView(mBottomLoadingView);
		mListView.setAdapter(mVegetableAdapter);
		mListView.setOnScrollListener(this);
		
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
		RequestParam paramPath = new RequestParam("goods/general/typeList")
		//TODO 这里的地址和类型写死了
		.setParams("frontTypeId", "1")
		.setParams("addressId", "0")
		.setParams("pageNum", mCurrentPage + 1)
		.setParams("pageSize", PAGE_ITEM_COUNT);
		
		HttpManager.getInstance(this).volleyRequestByPost(HttpManager.URL + paramPath, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				ItemSaleInfoPage itemSaleInfoPage = gson.fromJson(response, new TypeToken<ItemSaleInfoPage>() { }.getType());
				mMaxDateNum = itemSaleInfoPage.getTotalCount();
				if(itemSaleInfoPage == null) return;
				itemSaleInfoPage.getPageCount();
				List<ItemSaleInfo> vegetables = itemSaleInfoPage.getGoodsList();
				if(vegetables == null) return;
				mCurrentPage++;
				addFormatData(vegetables);
			}
		});
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
