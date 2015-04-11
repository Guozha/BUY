package com.guozha.buy.activity.mpage;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.PreSpecialGridAdapter;
import com.guozha.buy.entry.mpage.prespecial.PreSpecialItem;
import com.guozha.buy.entry.mpage.prespecial.PreSpecialPage;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 预售/特供
 * @author PeggyTong
 *
 */
public class PreSpecialActivity extends BaseActivity implements OnScrollListener{
	
	private static final String PAGE_NAME = "PreSpecialPage";
	private static final int HAND_DATA_COMPLETED = 0x0001; //请求数据完成
	private static final int PAGE_SIZE = 10;
	
	private GridView mGridView;
	private List<PreSpecialItem> mPreSpecialItems;
	private PreSpecialGridAdapter mPreSpecialGridAdapter;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				mPreSpecialGridAdapter.notifyDataSetChanged();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_special);
		customActionBarStyle("特供");
		initView();
		loadNewDataAndUpdate();
	}
	
	private int mPageNum = 0; //当前页数
	private int mMaxDataNum;  //最多条数
	private int mTotalPageSize; //页数
	
	private void loadNewDataAndUpdate(){
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("goods/special/list")
		.setParams("addressId", addressId)
		.setParams("pageNum", mPageNum)
		.setParams("pageSize", PAGE_SIZE);
		HttpManager.getInstance(this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				PreSpecialPage preSpecialPage = gson.fromJson(response, new TypeToken<PreSpecialPage>() { }.getType());
				addFormatData(preSpecialPage);
			}
		});
		
	}
	
	private void addFormatData(PreSpecialPage preSpecialPage){
		if(preSpecialPage == null) return;
		if(mPreSpecialItems == null){
			mPreSpecialItems = new ArrayList<PreSpecialItem>();
		}
		mMaxDataNum = preSpecialPage.getTotalCount();
		mTotalPageSize = preSpecialPage.getPageCount();
		List<PreSpecialItem> preSpecialItems = preSpecialPage.getGoodsList();
		if(preSpecialItems == null)return;
		mPreSpecialItems.addAll(preSpecialItems);
		mPageNum++;
		handler.sendEmptyMessage(HAND_DATA_COMPLETED);
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mGridView = (GridView) findViewById(R.id.pre_special_gridlist);
		mPreSpecialItems = new ArrayList<PreSpecialItem>();
		mPreSpecialGridAdapter = new PreSpecialGridAdapter(this, mPreSpecialItems);
		mGridView.setAdapter(mPreSpecialGridAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String prop = mPreSpecialItems.get(position).getGoodsProp();
				if("02".equals(prop) || "03".equals(prop)){
					Intent intent = new Intent(PreSpecialActivity.this, PreSpecialDetail.class);
					intent.putExtra("goodsId", mPreSpecialItems.get(position).getGoodsId());
					startActivity(intent);
				}else{
					ToastUtil.showToast(PreSpecialActivity.this, "非特供预售商品");
				}
			}
		});
		mGridView.setOnScrollListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//友盟界面统计
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
	
	private int mLastVisibleIndex;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex == mPreSpecialGridAdapter.getCount() - 1
				&& mPageNum < mTotalPageSize){
			loadNewDataAndUpdate();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		LogUtil.e("pageSize == " + mTotalPageSize);
		LogUtil.e("currentPage == " + mPageNum);
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;  
		//所有的条目已经和最大数相等，则移除底部的view
		if(totalItemCount >= mMaxDataNum){	//加了viewHead和viewFooter
			//TODO 没有更多数据
		}
	}
}
