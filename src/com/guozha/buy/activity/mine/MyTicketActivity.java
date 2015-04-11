package com.guozha.buy.activity.mine;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.TicketListAdapter;
import com.guozha.buy.entry.mine.MarketTicket;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的菜票
 * @author PeggyTong
 *
 */
public class MyTicketActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "MyTicketPage";
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private ListView mMyTicket;  //我的菜票
	
	private List<MarketTicket> mMarketTickets;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				if(mMyTicket == null) return;
				mMyTicket.setAdapter(new TicketListAdapter(MyTicketActivity.this, mMarketTickets));
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_ticket);
		customActionBarStyle("我的菜票");
		
		initView();
		setTicketData();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mMyTicket = (ListView) findViewById(R.id.my_ticket);
	}
	
	/**
	 * 获取并设置我的菜票
	 */
	private void setTicketData(){
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("account/ticket/list")
		.setParams("userId", userId);
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				mMarketTickets = gson.fromJson(response, new TypeToken<List<MarketTicket>>() { }.getType());
				LogUtil.e("mMarketTicket.size  == " + mMarketTickets.size());
				handler.sendEmptyMessage(HAND_DATA_COMPLETED);
			}
		});
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
