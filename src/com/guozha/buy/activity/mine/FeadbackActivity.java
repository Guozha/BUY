package com.guozha.buy.activity.mine;

import android.os.Bundle;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;

/**
 * 意见反馈
 * @author PeggyTong
 *
 */
public class FeadbackActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feadback);
		customActionBarStyle("意见反馈");
	}
}
