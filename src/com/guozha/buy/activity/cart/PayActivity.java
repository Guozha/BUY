package com.guozha.buy.activity.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.dialog.CustomDialog;

public class PayActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		customActionBarStyle("支付");
		
		initView();
	}
	
	private void initView(){
		findViewById(R.id.pay_server_fee_rule).setOnClickListener(this);
		findViewById(R.id.pay_ticket_choose).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.pay_server_fee_rule: //服务费规则
			CustomDialog customDialog = new CustomDialog(
					PayActivity.this, R.layout.dialog_server_fee_rule);
			customDialog.setDismissButtonId(0);
			break;
		case R.id.pay_ticket_choose:
			Intent intent = new Intent(PayActivity.this, ChooseTicketActivity.class);
			startActivity(intent);
			break;
		}
	}
}
