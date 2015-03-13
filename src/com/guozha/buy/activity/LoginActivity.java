package com.guozha.buy.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.HttpManager;
import com.guozha.buy.util.HttpUtil;
import com.guozha.buy.util.RegularUtil;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 登录界面
 * @author PeggyTong
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "LoginPage";

	private EditText mEditPhoneNum;  
	private EditText mEditPwd;   
	private ImageView mPhoneNumIcon;
	private ImageView mPwdIcon;
	private Button mLoginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		customActionBarStyle("登录");
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
		
		findViewById(R.id.login_toregist_tv).setOnClickListener(this);
		findViewById(R.id.login_tofindpwd_tv).setOnClickListener(this);
		
		mPhoneNumIcon.setOnClickListener(this);
		mPwdIcon.setOnClickListener(this);
		mLoginButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		Intent intent;
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
			if(!isValidatePhoneNum(phoneNum) || !isValidatePwd(pwd)){
				//TODO 提示填写有误
				ToastUtil.showToast(this, "手机号码或密码不正确");
				return;
			}
			//请求登录
			requestLogin(phoneNum, pwd);
			
			break;
		case R.id.login_toregist_tv:
			intent = new Intent(LoginActivity.this, RegistActivity.class);
			startActivity(intent);
			break;
		case R.id.login_tofindpwd_tv:
			intent = new Intent(LoginActivity.this, FindPwdActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 请求登录
	 * @param phoneNum
	 * @param pwd
	 */
	private void requestLogin(String phoneNum, String pwd) {
		Map<String, String> params;
		String paramPath;
		params = new HashMap<String, String>();
		params.put("mobileNo", phoneNum);
		params.put("passwd", pwd);
		paramPath = "account/login" + HttpUtil.generatedAddress(params);
		HttpManager.getInstance(this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode");
					if("1".equals(returnCode.trim())){
						String userId = response.getString("userId");
						String userToken = response.getString("token");
						String mobileNum = response.getString("mobileNo");
						ConfigManager.getInstance().setUserId(userId);
						ConfigManager.getInstance().setUserToken(userToken);
						ConfigManager.getInstance().setMobileNum(mobileNum);
						ToastUtil.showToast(LoginActivity.this, "登录成功");
					}else{
						String msg = response.getString("msg");
						ToastUtil.showToast(LoginActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
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
				if(phoneNum.length() == 0){
					mPhoneNumIcon.setVisibility(View.INVISIBLE);
				}else{
					mPhoneNumIcon.setVisibility(View.VISIBLE);
				}
				if(isValidatePhoneNum(phoneNum)){
					//TODO 设置为对号图标
					mPhoneNumIcon.setImageResource(R.drawable.login_right_state);
				}else{
					//TODO 设置为叉号图标
					mPhoneNumIcon.setImageResource(R.drawable.login_wrong_state);
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
				if(pwd.length() == 0){
					mPwdIcon.setVisibility(View.INVISIBLE);
				}else{
					mPwdIcon.setVisibility(View.VISIBLE);
				}
				if(isValidatePwd(pwd)){
					//TODO
					mPwdIcon.setImageResource(R.drawable.login_right_state);
				}else{
					//TODO
					mPwdIcon.setImageResource(R.drawable.login_wrong_state);
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
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//友盟界面统计
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
}
