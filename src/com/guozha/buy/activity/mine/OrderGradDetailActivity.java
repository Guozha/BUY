package com.guozha.buy.activity.mine;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单详情（评价界面，也就是已完成订单详情界面）
 * @author PeggyTong
 *
 */
public class OrderGradDetailActivity extends BaseActivity{
	

	private static final String PAGE_NAME = "OrderDetailPage";
	private static final int HAND_DATA_COMPLTED = 0x0001;
	
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
	private EditText mFeadBackText;
	private Button mFeadBackButton;
	private int mOrderId; 
	
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
		setContentView(R.layout.activity_grad_order_detail);
		customActionBarStyle("订单详情");
		setResult(1);
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
		mFeadBackText = (EditText)findViewById(R.id.order_feadback_text);
		mFeadBackButton = (Button) findViewById(R.id.order_feadback_button);
		mFeadBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestOrderFeadback();
			}
		});
		
		mOrderNumText = (TextView) findViewById(R.id.order_detail_num);
		mOrderTimeText = (TextView) findViewById(R.id.order_detail_time);
		mOrderAddressNameText = (TextView) findViewById(R.id.order_detail_address_name);
		mOrderAddressDetailText = (TextView) findViewById(R.id.order_detail_address_detail);
		mOrderTotalPriceText = (TextView) findViewById(R.id.order_detail_total_price);
	}
	
	/**
	 * 请求订单评价
	 */
	private void requestOrderFeadback() {
		String feadback = mFeadBackText.getText().toString();
		if(feadback.isEmpty()) return;
		String token = ConfigManager.getInstance().getUserToken();
		RequestParam paramPath = new RequestParam("order/orderMark")
		.setParams("token", token)
		.setParams("orderId", mOrderId)
		.setParams("commentDesc", feadback)
		.setParams("serviceStar", "");
		HttpManager.getInstance(OrderGradDetailActivity.this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							ToastUtil.showToast(OrderGradDetailActivity.this, "评价成功");
							setResult(1);
							OrderGradDetailActivity.this.finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
	
	private void updateView(){
		if(mMenusAdapter == null){
			mMenusAdapter = new OrderDetailMenusListAdapter(this, mExpandListDatas, true, mOrderId);
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
					
					mOrderNum = "订单号：" + orderDetail.getOrderNo();
					mOrderTime = "下单时间：" + DimenUtil.getStringFormatTime(orderDetail.getCreateTime());
					mOrderAddressName = orderDetail.getReceiveMen() + "   " + orderDetail.getReceiveMobile();
					mOrderAddressDetail = orderDetail.getReceiveAddr();
					mOrderTotalPrice = "订单总额 " + UnitConvertUtil.getSwitchedMoney(orderDetail.getTotalPrice());
					
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
							expandListData.setUnit(orderDetailGoods.getUnit());
							expandListData.setAmount(orderDetailGoods.getAmount());
							expandListData.setPrice(orderDetailGoods.getPrice());
							expandListData.setType(1);
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
							expandListData.setAmount(orderDetailMenus.getAmount());
							expandListData.setUnit("8");
							expandListData.setMenuslist(orderDetailMenus.getGoodsInfoList());
							expandListData.setPrice(orderDetailMenus.getPrice());
							expandListData.setType(0);
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
