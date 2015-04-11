package com.guozha.buy.activity.cart;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.ChooseTicketListAdapter;
import com.guozha.buy.entry.mine.MarketTicket;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;

/**
 * 选择有效菜票
 * @author PeggyTong
 *
 */
public class ChooseTicketActivity extends BaseActivity{
	
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private ListView mChooseTicketList;
	private List<MarketTicket> mMarketTickets;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateView();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_ticket);
		customActionBarStyle("选择菜票");
		setResult(0);
		initView();
		initData();
	}
	
	private void initView(){
		mChooseTicketList = (ListView) findViewById(R.id.choose_ticket_list);
		mChooseTicketList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = getIntent();
				if(intent != null){
					intent.putExtra("ticktId", mMarketTickets.get(position).getMyTicketId());
					intent.putExtra("ticketPrice", mMarketTickets.get(position).getForPrice());
					setResult(0, intent);
				}
				ChooseTicketActivity.this.finish();
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("account/ticket/listValid")
		.setParams("userId", userId);
		HttpManager.getInstance(ChooseTicketActivity.this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mMarketTickets = gson.fromJson(response, new TypeToken<List<MarketTicket>>() { }.getType());
					handler.sendEmptyMessage(HAND_DATA_COMPLETED);
				}
		});
	}
	
	
	private void updateView(){
		mChooseTicketList.setAdapter(new ChooseTicketListAdapter(this, mMarketTickets));
	}
}
