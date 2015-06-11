package com.guozha.buy.controller.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.OrderDetailMenusListAdapter;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.cart.PayActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.entry.mine.order.ExpandListData;
import com.guozha.buy.entry.mine.order.OrderDetail;
import com.guozha.buy.entry.mine.order.OrderDetailGoods;
import com.guozha.buy.entry.mine.order.OrderDetailMenus;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.OrderModel;
import com.guozha.buy.model.result.OrderModelResult;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 未付款订单详情
 * @author PeggyTong
 *
 */
public class OrderUnPayDetailActivity extends BaseActivity{

	private static final String PAGE_NAME = "OrderDetailPage";
	private static final int REQUEST_CODE = 0x0001;
	private static final int HAND_DATA_COMPLTED = 0x0001;
	
	private ExpandableListView mExpandableListView;
	private OrderDetailMenusListAdapter mMenusAdapter;
	
	private List<ExpandListData> mExpandListDatas;
	
	private int mOrderId; 
	private String mOrderType;
	private String mOrderNum;
	private String mOrderTime;
	private String mOrderDescript;
	private String mOrderAddressName;
	private String mOrderAddressDetail;
	private String mOrderTotalPrice;
	private int mServiceFee = 0;
	
	private String mOrderStatus;
	
	private TextView mOrderNumText;
	private TextView mOrderTimeText;
	private TextView mOrderDescriptText;
	private TextView mOrderAddressNameText;
	private TextView mOrderAddressDetailText;
	private TextView mOrderTotalPriceText;
	
	private OrderModel mOrderModel;
	
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
		setContentView(R.layout.activity_unpay_order_detail);
		customActionBarStyle("订单详情");
		
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mOrderId = bundle.getInt("orderId");
				mOrderDescript = "订单状态：" + bundle.getString("orderDescript");
			}
		}
		setResult(0);
		initView();
		mOrderModel = new OrderModel(new MyOrderModelResult());
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
		
		findViewById(R.id.order_detail_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CustomDialog cancelOrderDialog = new CustomDialog(
						OrderUnPayDetailActivity.this, R.layout.dialog_cancel_order);
				cancelOrderDialog.setDismissButtonId(R.id.cancel_button);
				cancelOrderDialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String token = ConfigManager.getInstance().getUserToken();
						if(token == null) return; //TODO 先登录
						mOrderModel.requestCancelOrder(OrderUnPayDetailActivity.this, 
								token, mOrderId, mOrderStatus);
					}
				});
			}
		});
		
		findViewById(R.id.order_detail_pay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("1".equals(mOrderType)){ //普通订单
					Intent intent = new Intent(OrderUnPayDetailActivity.this, PayActivity.class);
					intent.putExtra("orderId", mOrderId);
					intent.putExtra("serverPrice", mServiceFee);
					intent.putExtra("orderComeIn", true);
					startActivityForResult(intent, REQUEST_CODE);
				}else{		//特供预售
					
					//TODO 这里要做特殊处理
					if(mExpandListDatas != null && !mExpandListDatas.isEmpty()){
						ExpandListData expandListData = mExpandListDatas.get(0);
						Intent intent = new Intent(OrderUnPayDetailActivity.this, PayActivity.class);
						intent.putExtra("goodsId", expandListData.getId());
						intent.putExtra("unitPrice", expandListData.getUnitPrice());
						intent.putExtra("goodsName", expandListData.getName());
						intent.putExtra("goodsType", mOrderType);
						intent.putExtra("orderComeIn", true);
						startActivityForResult(intent, REQUEST_CODE);
					}
				}
			}
		});
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
		mOrderNumText.setText(mOrderNum);
		mOrderTimeText.setText(mOrderTime);
		mOrderDescriptText.setText(mOrderDescript);
		mOrderAddressNameText.setText(mOrderAddressName);
		mOrderAddressDetailText.setText(mOrderAddressDetail);
		mOrderTotalPriceText.setText(mOrderTotalPrice);
	}
	
	private void initData(){
		mOrderModel.requestOrderDetail(this, mOrderId);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE){
			if(data != null){
				Bundle bundle = data.getExtras();
				if(bundle != null){
					boolean paySuccess = bundle.getBoolean("paySuccess");
					if(paySuccess){
						OrderUnPayDetailActivity.this.finish();
					}
				}
			}
		}
	}
	
	class MyOrderModelResult extends OrderModelResult{

		@Override
		public void requestOrderDetailResult(OrderDetail orderDetail) {
			if(orderDetail == null) return;
			mOrderNum = "订单号：" + orderDetail.getOrderNo();
			mOrderType = orderDetail.getOrderType();
			mOrderTime = "下单时间：" + DimenUtil.getStringFormatTime(orderDetail.getCreateTime());
			mOrderAddressName = orderDetail.getReceiveMen() + "   " + orderDetail.getReceiveMobile();
			mOrderAddressDetail = orderDetail.getReceiveAddr();
			mServiceFee = orderDetail.getServiceFee();
			mOrderTotalPrice = "订单总额 " + UnitConvertUtil.getSwitchedMoney(orderDetail.getTotalPrice());
			mOrderStatus = orderDetail.getStatus();
			if(mExpandListDatas == null){
				mExpandListDatas = new ArrayList<ExpandListData>();
			}
			
			if(orderDetail.getGoodsInfoList() != null){
				for(int i = 0; i < orderDetail.getGoodsInfoList().size(); i++){
					OrderDetailGoods orderDetailGoods = orderDetail.getGoodsInfoList().get(i);
					ExpandListData expandListData = new ExpandListData();
					expandListData.setId(orderDetailGoods.getGoodsId());
					expandListData.setName(orderDetailGoods.getGoodsName());
					expandListData.setUnitPrice(orderDetailGoods.getUnitPrice());
					expandListData.setUnit(orderDetailGoods.getUnit());
					expandListData.setAmount(orderDetailGoods.getAmount());
					expandListData.setPrice(orderDetailGoods.getPrice());
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
					expandListData.setUnitPrice(orderDetailMenus.getUnitPrice());
					expandListData.setMenuslist(orderDetailMenus.getGoodsInfoList());
					expandListData.setPrice(orderDetailMenus.getPrice());
					mExpandListDatas.add(expandListData);
				}
			}
			handler.sendEmptyMessage(HAND_DATA_COMPLTED);
		}

		@Override
		public void requestCancelOrderResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				OrderUnPayDetailActivity.this.finish();
			}else{
				ToastUtil.showToast(OrderUnPayDetailActivity.this, msg);
			}
		}
	}
}
