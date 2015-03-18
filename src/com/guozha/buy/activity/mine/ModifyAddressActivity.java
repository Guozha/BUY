package com.guozha.buy.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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
		
		findViewById(R.id.modify_choose_canton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ModifyAddressActivity.this, ChooseCantonActivity.class);
				startActivity(intent);
			}
		});
	}
}
