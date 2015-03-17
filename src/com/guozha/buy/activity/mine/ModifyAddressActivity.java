package com.guozha.buy.activity.mine;

import android.os.Bundle;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;

/**
 * 修改地址
 * @author PeggyTong
 *
 */
public class ModifyAddressActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_address);
		customActionBarStyle("修改地址");
	}
}
