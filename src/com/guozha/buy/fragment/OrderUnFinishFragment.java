package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.mine.OrderDetailActivity;
import com.guozha.buy.adapter.OrderListAdapter;
import com.guozha.buy.entry.mine.order.OrderSummary;
import com.guozha.buy.entry.mine.order.OrderSummaryPage;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.HttpUtil;
import com.guozha.buy.util.ToastUtil;

/**
 * 未完成订单列表
 * @author PeggyTong
 *
 */
public class OrderUnFinishFragment extends Fragment implements OnScrollListener{
	
	private static final int HAND_DATA_COMPLETED = 0x0001;  //数据完成
	
	private ListView mOrderUnFinishList;
	
	private OrderSummaryPage mOrderSummaryPage; //每一页的数据
	private List<OrderSummary> mOrderList; //该页数据
	
	private View mBottomLoadingView; 		//底部刷新视图
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	private OrderListAdapter mOrderListAdapter;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				if(mOrderUnFinishList == null) return;
				updateListView();
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_order_unfinished, container, false);
		initView(view);
		loadData();
		return view;
	}
	
	/**
	 * 初始化View
	 * @param view
	 */
	private void initView(View view){
		mOrderUnFinishList = (ListView) view.findViewById(R.id.order_unfinished_list);
		
		mOrderUnFinishList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(
						OrderUnFinishFragment.this.getActivity(), OrderDetailActivity.class);
				startActivity(intent);
			}
			
		});
		
		mBottomLoadingView = getActivity().getLayoutInflater()
				.inflate(R.layout.list_paging_bottom, null);
		mLoadText = (TextView) mBottomLoadingView.findViewById(R.id.list_paging_bottom_text);
		mLoadProgressBar = (ProgressBar) 
				mBottomLoadingView.findViewById(R.id.list_paging_bottom_progressbar);
		mOrderUnFinishList.addFooterView(mBottomLoadingView);
		mOrderUnFinishList.setOnScrollListener(this);
		
		mOrderListAdapter = new OrderListAdapter(getActivity(), mOrderList);
		mOrderUnFinishList.setAdapter(mOrderListAdapter);
	}
	
	/**
	 * 更新listView数据
	 */
	private void updateListView(){
		if(mOrderSummaryPage == null) return;
		List<OrderSummary> orderSmmary = mOrderSummaryPage.getOrderList();
		if(orderSmmary == null) return;
		if(mOrderList == null){
			mOrderList = new ArrayList<OrderSummary>();
		}
		mOrderList.addAll(orderSmmary);
		mOrderSummaryPage = null;
		mOrderListAdapter.notifyDataSetChanged();
	}
	
	
	private static final String PAGE_SIZE = "20";
	private int mMaxDateNum;   			//最大数据
	private int mCurrentPage = 0;		//当前页
	
	/**
	 * 从网络获取数据
	 */
	private void loadData(){
		
		int userId = ConfigManager.getInstance().getUserId();
		if(userId == -1) return;
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", String.valueOf(userId));
		params.put("searchType", "1");
		//TODO 这里是分页加载的
		params.put("pageNum", String.valueOf(mCurrentPage + 1));
		params.put("pageSize", PAGE_SIZE);
		String paramPath = "order/list" + HttpUtil.generatedAddress(params);
		HttpManager.getInstance(getActivity()).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mOrderSummaryPage = gson.fromJson(response, new TypeToken<OrderSummaryPage>() { }.getType());
					ToastUtil.showToast(OrderUnFinishFragment.this.getActivity(), "返回结果了");
					mCurrentPage++;
					//TODO 设置总页数
					mMaxDateNum = mOrderSummaryPage.getTotalCount();
					handler.sendEmptyMessage(HAND_DATA_COMPLETED);
				}
			});
	}

	private int mLastVisibleIndex;		//最后一条可见项
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//当滑动到底部后自动加载
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex == mOrderListAdapter.getCount()){
			loadData();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//计算最后可见条目的索引
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		
		//所有的条目已经和最大数相等，则移除底部的View
		if(totalItemCount == mMaxDateNum){
			mOrderUnFinishList.removeFooterView(mBottomLoadingView);
			ToastUtil.showToast(getActivity(), "数据全部加载完毕，没有更多数据!");
		}
	}
}
