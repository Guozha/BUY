package com.guozha.buy.activity.mine;

import android.os.Bundle;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;

/**
 * 我的菜票
 * @author PeggyTong
 *
 */
public class MyTicketActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_ticket);
		customActionBarStyle("我的菜票");
	}
}
