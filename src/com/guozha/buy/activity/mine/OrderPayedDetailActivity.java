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
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单详情
 * @author PeggyTong
 *
 */
public class OrderPayedDetailActivity extends BaseActivity{

	private static final String PAGE_NAME = "OrderDetailPage";
	private static final int HAND_DATA_COMPLTED = 0x0001;
	
	private int mOrderId; 
	private String mOrderNum;
	private String mOrderTime;
	private String mOrderDescript;
	private String mOrderAddressName;
	private String mOrderAddressDetail;
	private String mOrderTotalPrice;
	
	private TextView mOrderNumText;
	private TextView mOrderTimeText;
	private TextView mOrderDescriptText;
	private TextView mOrderAddressNameText;
	private TextView mOrderAddressDetailText;
	private TextView mOrderTotalPriceText;
	
	private ExpandableListView mExpandableListView;
	private OrderDetailMenusListAdapter mMenusAdapter;
	
	private List<ExpandListData> mExpandListDatas;
	
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
		setResult(0);
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
		
		mOrderNumText = (TextView) findViewById(R.id.order_detail_num);
		mOrderTimeText = (TextView) findViewById(R.id.order_detail_time);
		mOrderAddressNameText = (TextView) findViewById(R.id.order_detail_address_name);
		mOrderAddressDetailText = (TextView) findViewById(R.id.order_detail_address_detail);
		mOrderTotalPriceText = (TextView) findViewById(R.id.order_detail_total_price);
	}
	
	private void updateView(){
		if(mMenusAdapter == null){
			mMenusAdapter = new OrderDetailMenusListAdapter(this, mExpandListDatas, false);
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
		
		mOrderNumText.setText(mOrderNum);
		mOrderTimeText.setText(mOrderTime);
		mOrderDescriptText.setText(mOrderDescript);
		mOrderAddressNameText.setText(mOrderAddressName);
		mOrderAddressDetailText.setText(mOrderAddressDetail);
		mOrderTotalPriceText.setText(mOrderTotalPrice);
	}
	
	private void initData(){
		RequestParam paramPath = new RequestParam("order/detail")
		.setParams("orderId", mOrderId);
		HttpManager.getInstance(this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					OrderDetail orderDetail = gson.fromJson(response, new TypeToken<OrderDetail>() { }.getType());
					if(orderDetail == null) return;
					mOrderNum = "订单号：" + orderDetail.getOrderNo();
					mOrderTime = "下单时间：" + DimenUtil.getStringFormatTime(orderDetail.getCreateTime());
					mOrderAddressName = orderDetail.getReceiveMen() + "   " + orderDetail.getReceiveMobile();
					mOrderAddressDetail = orderDetail.getReceiveAddr();
					mOrderTotalPrice = "订单总额 " + UnitConvertUtil.getSwitchedMoney(orderDetail.getTotalPrice());
					if(mExpandListDatas == null){
						mExpandListDatas = new ArrayList<ExpandListData>();
					}
					
					if(orderDetail.getGoodsInfoList() != null){
						for(int i = 0; i < orderDetail.getGoodsInfoList().size(); i++){
							OrderDetailGoods orderDetailGoods = orderDetail.getGoodsInfoList().get(i);
							ExpandListData expandListData = new ExpandListData();
							expandListData.setId(orderDetailGoods.getGoodsId());
							expandListData.setName(orderDetailGoods.getGoodsName());
							expandListData.setUnit(orderDetailGoods.getUnit());
							expandListData.setAmount(orderDetailGoods.getAmount());
							expandListData.setPrice(orderDetailGoods.getPrice());
							//TODO 设置价格等
							mExpandListDatas.add(expandListData);
						}
					}
					
					if(orderDetail.getMenuInfoList() != null){
						for(int i = 0; i < orderDetail.getMenuInfoList().size(); i++){
							OrderDetailMenus orderDetailMenus = orderDetail.getMenuInfoList().get(i);
							ExpandListData expandListData = new ExpandListData();
							expandListData.setId(orderDetailMenus.getMenuId());
							expandListData.setName(orderDetailMenus.getMenuName());
							expandListData.setAmount(orderDetailMenus.getAmount());
							expandListData.setUnit("8");
							expandListData.setMenuslist(orderDetailMenus.getGoodsInfoList());
							expandListData.setPrice(orderDetailMenus.getPrice());
							mExpandListDatas.add(expandListData);
						}
					}
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
