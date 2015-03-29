package com.guozha.buy.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.CartItemListAdapter;
import com.guozha.buy.entry.cart.CartBaseItem;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartCookMaterial;
import com.guozha.buy.entry.cart.CartMarketItem;
import com.guozha.buy.entry.cart.CartBaseItem.CartItemType;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单详情
 * @author PeggyTong
 *
 */
public class OrderDetailActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "OrderDetailPage";
	
	private ExpandableListView mOrderDetailList;
	
	private List<CartBaseItem> mOrderItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		customActionBarStyle("订单详情");
		initData();
		initView();
	}
	
	private void initView(){
		mOrderDetailList = (ExpandableListView) findViewById(R.id.expandable_order_detail_list);
		mOrderDetailList.setAdapter(new CartItemListAdapter(this, mOrderItems));
		//首次全部展开
		for (int i = 0; i < mOrderItems.size(); i++) {
		    mOrderDetailList.expandGroup(i);
		}
	}
	
	private void initData(){
		mOrderItems = new ArrayList<CartBaseItem>();
		
		//TODO 参考购物车显示
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
}
