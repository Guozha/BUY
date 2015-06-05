package com.guozha.buy.controller.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.guozha.buy.R;
import com.guozha.buy.controller.LoginActivity;

/**
 * 提醒登录的对话框
 * @author PeggyTong
 *
 */
public class RemindLoginDialog extends Activity{
	
	private String mTurnActivityName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_remind_login);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			mTurnActivityName = extras.getString(LoginActivity.SUCCESS_TURN_INTENT);
		}
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
				if(mTurnActivityName != null){
					intent.putExtra(LoginActivity.SUCCESS_TURN_INTENT, mTurnActivityName);
				}
				startActivity(intent);
				RemindLoginDialog.this.finish();
			}
		});
	}
}
