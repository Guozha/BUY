package com.guozha.buy.activity.mine;

import android.os.Bundle;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.TicketListAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的菜票
 * @author PeggyTong
 *
 */
public class MyTicketActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "MyTicketPage";
	
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
