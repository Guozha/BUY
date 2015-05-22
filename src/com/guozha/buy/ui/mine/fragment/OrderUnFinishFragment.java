				package com.guozha.buy.ui.mine.fragment;

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

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.adapter.OrderListAdapter;
import com.guozha.buy.entry.mine.order.OrderSummary;
import com.guozha.buy.entry.mine.order.OrderSummaryPage;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.ui.BaseFragment;
import com.guozha.buy.ui.mine.OrderPayedDetailActivity;
import com.guozha.buy.ui.mine.OrderUnPayDetailActivity;
import com.guozha.buy.util.ConstantUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 未完成订单列表
 * @author PeggyTong
 *
 */
public class OrderUnFinishFragment extends BaseFragment implements OnScrollListener{
	
	private static final String PAGE_NAME = "UnFinishedOrderPage";
	
	private static final int HAND_DATA_COMPLETED = 0x0001;  //数据完成
	private static final int REQUEST_CODE = 0x0001;			
	
	private ListView mOrderUnFinishList;
	
	private OrderSummaryPage mOrderSummaryPage; //每一页的数据
	private List<OrderSummary> mOrderList; //该页数据
	
	private View mBottomLoadingView; 		//底部刷新视图
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	private OrderListAdapter mOrderListAdapter;
	private View mEmptyView;
	
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
		mEmptyView = view.findViewById(R.id.empty_view);
		mOrderUnFinishList = (ListView) view.findViewById(R.id.order_unfinished_list);
		mOrderUnFinishList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				OrderSummary orderSummary = mOrderList.get(position);
				
				String orderDescript = ConstantUtil.getOrderStatusString(
						orderSummary.getStatus(),
						orderSummary.getArrivalPayFlag(), 
						orderSummary.getCommentFlag());
				//如果是货到付款或者支付成功
				Intent intent;
				if("1".equals(orderSummary.getArrivalPayFlag())){
					intent = new Intent(
							OrderUnFinishFragment.this.getActivity(), OrderPayedDetailActivity.class);
					intent.putExtra("orderDescript", orderDescript);
					intent.putExtra("orderId", orderSummary.getOrderId());
					startActivity(intent);
				}else if("1".equals(orderSummary.getFinishPayFlag())){
					intent = new Intent(
							OrderUnFinishFragment.this.getActivity(), OrderPayedDetailActivity.class);
					intent.putExtra("orderDescript", orderDescript);
					intent.putExtra("orderId", orderSummary.getOrderId());
					startActivity(intent);
				}else{
					intent = new Intent(
							OrderUnFinishFragment.this.getActivity(), OrderUnPayDetailActivity.class);
					intent.putExtra("orderDescript", orderDescript);
					intent.putExtra("orderId", orderSummary.getOrderId());
					startActivityForResult(intent, REQUEST_CODE);
				}
			}
		});
		
		mBottomLoadingView = getActivity().getLayoutInflater()
				.inflate(R.layout.list_paging_bottom, null);
		mBottomLoadingView.setBackgroundColor(getActivity().getResources().getColor(R.color.color_app_base_6));
		mLoadText = (TextView) mBottomLoadingView.findViewById(R.id.list_paging_bottom_text);
		mLoadProgressBar = (ProgressBar) 
				mBottomLoadingView.findViewById(R.id.list_paging_bottom_progressbar);
		mOrderUnFinishList.addFooterView(mBottomLoadingView);
		mOrderUnFinishList.setOnScrollListener(this);
	}
	
	/**
	 * 更新listView数据
	 */
	private void updateListView(){
		if(mOrderList.isEmpty()){
			mOrderUnFinishList.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.VISIBLE);
		}else{
			mOrderUnFinishList.setVisibility(View.VISIBLE);
			mEmptyView.setVisibility(View.GONE);
		}
		if(mOrderListAdapter == null){
			mOrderListAdapter = new OrderListAdapter(getActivity(), mOrderList);
			mOrderUnFinishList.setAdapter(mOrderListAdapter);
		}else{
			mOrderListAdapter.notifyDataSetChanged();
		}
	}
	
	
	private static final String PAGE_SIZE = "10";
	private int mMaxDateNum;   			//最大数据
	private int mMaxPageSize;
	private int mCurrentPage = 0;		//当前页
	
	/**
	 * 从网络获取数据
	 */
	private void loadData(){
		int userId = ConfigManager.getInstance().getUserId();
		if(userId == -1) return;
		RequestParam paramPath = new RequestParam("order/list")
		.setParams("userId", userId)
		.setParams("searchType", "1")
		.setParams("pageNum", mCurrentPage + 1)
		.setParams("pageSize", PAGE_SIZE);
		HttpManager.getInstance(getActivity()).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mOrderSummaryPage = gson.fromJson(response, new TypeToken<OrderSummaryPage>() { }.getType());
					mCurrentPage++;
					//TODO 设置总页数
					mMaxDateNum = mOrderSummaryPage.getTotalCount();
					mMaxPageSize = mOrderSummaryPage.getPageCount();
					List<OrderSummary> orderSummary = mOrderSummaryPage.getOrderList();
					if(orderSummary == null)return;
					if(mOrderList == null){
						mOrderList = new ArrayList<OrderSummary>();
					}
					mOrderList.addAll(orderSummary);
					handler.sendEmptyMessage(HAND_DATA_COMPLETED);
				}
			});
	}

	private int mLastVisibleIndex;		//最后一条可见项
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//当滑动到底部后自动加载
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex == mOrderListAdapter.getCount()
				&& mCurrentPage < mMaxPageSize){
			mLoadProgressBar.setVisibility(View.VISIBLE);
			mLoadText.setVisibility(View.GONE);
			loadData();
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//计算最后可见条目的索引
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		//所有的条目已经和最大数相等，则移除底部的View
		if(totalItemCount == mMaxDateNum + 1){
			mLoadProgressBar.setVisibility(View.GONE);
			mLoadText.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见	
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE){
			if(mOrderList != null){
				mOrderList.clear();
			}
			mCurrentPage = 0;
			loadData();
		}
	}
}
