package com.guozha.buy.activity.mine;

import android.os.Bundle;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;

/**
 * 我的卖家
 * @author PeggyTong
 *
 */
public class MySellerActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_seller);
		customActionBarStyle("常用卖家");
	}
}
