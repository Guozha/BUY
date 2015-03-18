package com.guozha.buy.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;

/**
 * 添加地址
 * @author PeggyTong
 *
 */
public class AddAddressActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		customActionBarStyle("添加地址");
		
		findViewById(R.id.add_canton_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddAddressActivity.this, ChooseCantonActivity.class);
				startActivity(intent);
			}
		});
	}
}
