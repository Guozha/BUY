package com.guozha.buy.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.LoginActivity;

/**
 * 提醒登录的对话框
 * @author PeggyTong
 *
 */
public class RemindLoginDialog extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_remind_login);
		
		findViewById(R.id.cancel_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RemindLoginDialog.this.finish();
			}
		});
		
		findViewById(R.id.login_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RemindLoginDialog.this, LoginActivity.class);
				startActivity(intent);
				RemindLoginDialog.this.finish();
			}
		});
	}
}
