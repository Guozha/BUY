package com.guozha.buy.activity.cart;

import android.os.Bundle;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;

/**
 * 特供预售支付（特供预售不需要加入购物车)
 * @author PeggyTong
 *
 */
public class PreSpecialPayActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prespecial_pay);
		customActionBarStyle("订单确认");
	}
}
