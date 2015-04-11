package com.guozha.buy.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.OrderDetailMenusListAdapter;
import com.guozha.buy.entry.mine.order.ExpandListData;
import com.guozha.buy.entry.mine.order.OrderDetail;
import com.guozha.buy.entry.mine.order.OrderDetailGoods;
import com.guozha.buy.entry.mine.order.OrderDetailMenus;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单详情
 * @author PeggyTong
 *
 */
public class OrderPayedDetailActivity extends BaseActivity{

	private static final String PAGE_NAME = "OrderDetailPage";
	private static final int HAND_DATA_COMPLTED = 0x0001;
	
	private ExpandableListView mExpandableListView;
	private OrderDetailMenusListAdapter mMenusAdapter;
	
	private List<ExpandListData> mExpandListDatas;
	private TextView mOrderDescriptText;
	
	private int mOrderId; 
	private String mOrderDescript;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLTED:
				updateView();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payed_order_detail);
		customActionBarStyle("订单详情");
		
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mOrderId = bundle.getInt("orderId");
				mOrderDescript = bundle.getString("orderDescript");
			}
		}
		initView();
		initData();
	}
	
	private void initView(){
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandable_order_detail_list);
		mOrderDescriptText = (TextView) findViewById(R.id.order_descript_text);
	}
	
	private void updateView(){
		if(mMenusAdapter == null){
			mMenusAdapter = new OrderDetailMenusListAdapter(this, mExpandListDatas);
			mExpandableListView.setAdapter(mMenusAdapter);
			//首次全部展开
			for (int i = 0; i < mExpandListDatas.size(); i++) {
			    mExpandableListView.expandGroup(i);
			}
		}else{
			mMenusAdapter.notifyDataSetChanged();
		}
		if(mOrderDescriptText != null){
			mOrderDescriptText.setText(mOrderDescript);
		}
	}
	
	private void initData(){
		LogUtil.e("OrderId = " + mOrderId);
		RequestParam paramPath = new RequestParam("order/detail")
		.setParams("orderId", mOrderId);
		
		HttpManager.getInstance(this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					OrderDetail orderDetail = gson.fromJson(response, new TypeToken<OrderDetail>() { }.getType());
					if(orderDetail == null) return;
					if(mExpandListDatas == null){
						mExpandListDatas = new ArrayList<ExpandListData>();
					}
					
					if(orderDetail.getGoodsInfoList() != null){
						LogUtil.e("goods_size = " + orderDetail.getGoodsInfoList().size());
						for(int i = 0; i < orderDetail.getGoodsInfoList().size(); i++){
							OrderDetailGoods orderDetailGoods = orderDetail.getGoodsInfoList().get(i);
							ExpandListData expandListData = new ExpandListData();
							expandListData.setId(orderDetailGoods.getGoodsId());
							expandListData.setName(orderDetailGoods.getGoodsName());
							//TODO 设置价格等
							mExpandListDatas.add(expandListData);
						}
					}
					
					if(orderDetail.getMenuInfoList() != null){
						LogUtil.e("menu_size = " + orderDetail.getMenuInfoList().size());
						for(int i = 0; i < orderDetail.getMenuInfoList().size(); i++){
							OrderDetailMenus orderDetailMenus = orderDetail.getMenuInfoList().get(i);
							ExpandListData expandListData = new ExpandListData();
							expandListData.setId(orderDetailMenus.getMenuId());
							expandListData.setName(orderDetailMenus.getMenuName());
							mExpandListDatas.add(expandListData);
						}
					}
					LogUtil.e("mExpandListDatas == " + mExpandListDatas.size());
					handler.sendEmptyMessage(HAND_DATA_COMPLTED);
				}
		});
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
