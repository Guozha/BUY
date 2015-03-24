package com.guozha.buy.activity.mine;

import android.os.Bundle;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.TicketListAdapter;

/**
 * 我的菜票
 * @author PeggyTong
 *
 */
public class MyTicketActivity extends BaseActivity{
	
	private ListView mMyTicket;  //我的菜票

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_ticket);
		customActionBarStyle("我的菜票");
		
		initView();
	}
	
	private void initView(){
		mMyTicket = (ListView) findViewById(R.id.my_ticket);
		mMyTicket.setAdapter(new TicketListAdapter(MyTicketActivity.this));
	}
}
