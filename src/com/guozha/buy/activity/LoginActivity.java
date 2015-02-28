package com.guozha.buy.activity;

import com.guozha.buy.R;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 登录界面
 * @author PeggyTong
 *
 */
public class LoginActivity extends BaseActivity{

	private EditText mEditPhoneNum;
	private EditText mEditPwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mEditPhoneNum = (EditText) findViewById(R.id.login_phonenum);
		mEditPwd = (EditText) findViewById(R.id.login_pwd);
		
		mEditPhoneNum.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String phoneNum = mEditPhoneNum.getText().toString(); 
				//TODO 判断是否格式正确了
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) { }
			
			@Override
			public void afterTextChanged(Editable arg0) { }
		});
		
		mEditPwd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String pwd = mEditPwd.getText().toString();
				//TODO 判断是否格式正确了
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) { }
			
			@Override
			public void afterTextChanged(Editable arg0) { }
		});
	}
}
