package com.guozha.buy.activity.mine;

import android.os.Bundle;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.MySellerListAdapter;

/**
 * 我的卖家
 * @author PeggyTong
 *
 */
public class MySellerActivity extends BaseActivity{
	
	private ListView mMySellerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_seller);
		customActionBarStyle("常用卖家");
		
		mMySellerList = (ListView) findViewById(R.id.my_seller_list);
		mMySellerList.setAdapter(new MySellerListAdapter(this));
	}
}
