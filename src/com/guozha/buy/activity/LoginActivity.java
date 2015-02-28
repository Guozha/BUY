package com.guozha.buy.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.guozha.buy.R;
import com.guozha.buy.util.RegularUtil;

/**
 * 登录界面
 * @author PeggyTong
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener{

	private EditText mEditPhoneNum;  
	private EditText mEditPwd;   
	private ImageView mPhoneNumIcon;
	private ImageView mPwdIcon;
	private Button mLoginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initView();
		textChangeWatch();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		mEditPhoneNum = (EditText) findViewById(R.id.login_phonenum);
		mEditPwd = (EditText) findViewById(R.id.login_pwd);
		
		mPhoneNumIcon = (ImageView) findViewById(R.id.login_phonenum_clear);
		mPwdIcon = (ImageView) findViewById(R.id.login_pwd_clear);
		
		mLoginButton = (Button) findViewById(R.id.login_button);
		
		mPhoneNumIcon.setOnClickListener(this);
		mPwdIcon.setOnClickListener(this);
		mLoginButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		String phoneNum;
		String pwd;
		switch (view.getId()) {
		case R.id.login_phonenum_clear:
			phoneNum = mEditPhoneNum.getText().toString(); 
			if(!isValidatePhoneNum(phoneNum)){
				mEditPhoneNum.setText("");
			}
			break;
		case R.id.login_pwd_clear:
			pwd = mEditPwd.getText().toString();
			if(!isValidatePwd(pwd)){
				mEditPwd.setText("");
			}
			break;
		case R.id.login_button:
			phoneNum = mEditPhoneNum.getText().toString(); 
			pwd = mEditPwd.getText().toString();
			if(isValidatePhoneNum(phoneNum) && isValidatePwd(pwd)){
				//TODO 可以登录了
			}else{
				//TODO 提示填写有误
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 检测文字变化
	 */
	private void textChangeWatch() {
		if(mEditPhoneNum == null || mEditPwd == null) return;
		
		mEditPhoneNum.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String phoneNum = mEditPhoneNum.getText().toString(); 
				if(isValidatePhoneNum(phoneNum)){
					//TODO 设置为对号图标
				}else{
					//TODO 设置为叉号图标
				}
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
				if(isValidatePwd(pwd)){
					//TODO
				}else{
					//TODO
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) { }
			
			@Override
			public void afterTextChanged(Editable arg0) { }
		});
	}
	
	/**
	 * 判断输入的手机号码是否合格
	 * @param phoneNum
	 */
	private boolean isValidatePhoneNum(String phoneNum) {
		if(phoneNum.isEmpty() || 
				!RegularUtil.regularPhoneNum(phoneNum.trim())){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 判断输入的密码格式是否合格
	 * @param pwd
	 * @return
	 */
	private boolean isValidatePwd(String pwd) {
		if(pwd.isEmpty() || pwd.trim().length() < 6){
			return false;
		}else{
			return true;
		}
	}
}
