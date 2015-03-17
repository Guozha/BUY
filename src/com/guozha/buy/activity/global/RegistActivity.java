package com.guozha.buy.activity.global;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.HttpUtil;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.RegularUtil;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 注册界面
 * @author PeggyTong
 *
 */
public class RegistActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "RegistPage";
	
	private EditText mEditPhoneNum;
	private EditText mEditValidNum;
	private EditText mEditPwd;
	
	private ImageView mPhoneNumIcon;
	private ImageView mPwdIcon;
	
	private Button mObtainValidNumButton;
	private Button mRegistButton;
	
	private CheckBox mProtocalAffirmCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		customActionBarStyle("注册");
		initView();
		textChangeWatch();

	}

	/**
	 * 初始化View
	 */
	private void initView() {
		mEditPhoneNum = (EditText) findViewById(R.id.regist_phonenum);
		mEditValidNum = (EditText) findViewById(R.id.regist_validenum);
		mEditPwd = (EditText) findViewById(R.id.regist_pwd);
		
		mPhoneNumIcon = (ImageView) findViewById(R.id.regist_phonenum_clear);
		mPwdIcon = (ImageView) findViewById(R.id.regist_pwd_clear);
		
		mObtainValidNumButton = (Button) findViewById(R.id.regist_obtain_validenum);
		mRegistButton = (Button) findViewById(R.id.regist_button);
		
		mProtocalAffirmCheckBox = (CheckBox) findViewById(R.id.regist_protocal_affirm);
		
		mPhoneNumIcon.setOnClickListener(this);
		mPwdIcon.setOnClickListener(this);
		mObtainValidNumButton.setOnClickListener(this);
		mRegistButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		String phoneNum;
		String pwd;
		switch (view.getId()) {
		case R.id.regist_phonenum_clear:
			phoneNum = mEditPhoneNum.getText().toString(); 
			if(!isValidatePhoneNum(phoneNum)){
				mEditPhoneNum.setText("");
			}
			break;
		case R.id.regist_pwd_clear:
			pwd = mEditPwd.getText().toString();
			if(!isValidatePwd(pwd)){
				mEditPwd.setText("");
			}
			break;
		case R.id.regist_obtain_validenum:
			phoneNum = mEditPhoneNum.getText().toString();
			if(!isValidatePhoneNum(phoneNum)){
				ToastUtil.showToast(this, "你填写的手机号不正确");
				return;
			}
			//TODO 发送短信验证码
			obtainPoneValidate(phoneNum);
			break;
		case R.id.regist_button:
			phoneNum = mEditPhoneNum.getText().toString(); 
			pwd = mEditPwd.getText().toString();
			String validNum = mEditValidNum.getText().toString();
			if(!isValidatePhoneNum(phoneNum)){
				//提示手机号填写有误
				ToastUtil.showToast(this, "手机号码格式不正确");
				return;
			}
			if(!isValidatePwd(pwd)){
				//提示密码设置有误
				ToastUtil.showToast(this, "请检查密码设置");
				return;
			}
			if(!isValideNumRight(validNum)){
				//提示验证码错误
				ToastUtil.showToast(this, "验证码输入错误");
				return;
			}
			if(!mProtocalAffirmCheckBox.isChecked()){
				//提示没有同意用户协议
				ToastUtil.showToast(this, "请先阅读并同意用户协议");
				return;
			}
			//请求注册
			requestRegist(phoneNum, pwd, validNum);
			break;
		default:
			break;
		}
	}

	/**
	 * 请求注册
	 * @param phoneNum
	 * @param pwd
	 * @param validNum
	 */
	private void requestRegist(String phoneNum, String pwd, String validNum) {
		Map<String, String> params;
		String paramPath;
		params = new HashMap<String, String>();
		params.put("mobileNo", phoneNum);
		params.put("passwd", pwd);
		params.put("checkCode", validNum);
		paramPath = "account/register" + HttpUtil.generatedAddress(params);
		//TODO 可以登录了
		HttpManager.getInstance(this).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode").trim();
					if("1".equals(returnCode)){
						ToastUtil.showToast(RegistActivity.this, "注册成功");
						//存储密码
						ConfigManager.getInstance().setUserPwd(mEditPwd.getText().toString());
					}else{
						String msg = response.getString("msg").trim();
						ToastUtil.showToast(RegistActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取验证码
	 * @param phoneNum
	 */
	private void obtainPoneValidate(String phoneNum) {
		Map<String, String> params;
		String paramPath;
		params = new HashMap<String, String>();
		params.put("mobileNo", phoneNum);
		paramPath = "account/checkCodeForRegister" + HttpUtil.generatedAddress(params);
		HttpManager.getInstance(this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode");
					if("1".equals(returnCode.trim())){
						ToastUtil.showToast(RegistActivity.this, "验证码已发送");
					}else{
						String msg = response.getString("msg");
						ToastUtil.showToast(RegistActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 判断验证码是否正确
	 * @param validNum
	 * @return
	 */
	private boolean isValideNumRight(String validNum){
		if(validNum.isEmpty() || validNum.length() < 5 || validNum.length() > 15) return false;
		return true;
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
