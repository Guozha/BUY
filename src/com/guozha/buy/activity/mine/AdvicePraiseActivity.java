package com.guozha.buy.activity.mine;

import android.os.Bundle;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;

/**
 * 推荐有奖
 * @author PeggyTong
 *
 */
public class AdvicePraiseActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice_praise);
		customActionBarStyle("推荐有奖");
	}
}
