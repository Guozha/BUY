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
import com.umeng.analytics.MobclickAgent;

/**
 * 找回密码
 * @author PeggyTong
 *
 */
public class FindPwdActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "FindPwdPage";
	
	private EditText mEditPhoneNum;
	private EditText mEditValidNum;
	private EditText mEditPwd;
	private EditText mEditRepeatPwd;
	
	private ImageView mPhoneNumIcon;
	private ImageView mPwdIcon;
	private ImageView mRepeatPwdIcon;
	
	private Button mObtainValidNumButton;
	private Button mConfirmButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpwd);
		customActionBarStyle("找回密码");
		initView();
		
		textChangeWatch();
	}
	
	/**
	 * 初始化View
	 */
	private void initView() {
		mEditPhoneNum = (EditText) findViewById(R.id.findpwd_phonenum);
		mEditValidNum = (EditText) findViewById(R.id.findpwd_validenum);
		mEditPwd = (EditText) findViewById(R.id.findpwd_pwd);
		mEditRepeatPwd = (EditText) findViewById(R.id.findpwd_repeat_pwd);
		
		mPhoneNumIcon = (ImageView) findViewById(R.id.findpwd_phonenum_clear);
		mPwdIcon = (ImageView) findViewById(R.id.findpwd_pwd_clear);
		mRepeatPwdIcon = (ImageView) findViewById(R.id.findpwd_repeat_pwd_clear);
		
		mObtainValidNumButton = (Button) findViewById(R.id.findpwd_obtain_validenum);
		mConfirmButton = (Button) findViewById(R.id.confirm_button);
				
		mPhoneNumIcon.setOnClickListener(this);
		mPwdIcon.setOnClickListener(this);
		mRepeatPwdIcon.setOnClickListener(this);
		mObtainValidNumButton.setOnClickListener(this);
		mConfirmButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		String phoneNum;
		String pwd;
		String repeatPwd;
		switch (view.getId()) {
		case R.id.findpwd_phonenum_clear:
			phoneNum = mEditPhoneNum.getText().toString(); 
			if(!isValidatePhoneNum(phoneNum)){
				mEditPhoneNum.setText("");
			}
			break;
		case R.id.findpwd_pwd_clear:
			pwd = mEditPwd.getText().toString();
			if(!isValidatePwd(pwd)){
				mEditPwd.setText("");
			}
			break;
		case R.id.findpwd_repeat_pwd_clear:
			pwd = mEditPwd.getText().toString();
			repeatPwd = mEditRepeatPwd.getText().toString();
			if(!isValidatePwd(pwd) || (!pwd.equals(repeatPwd))){
				mEditRepeatPwd.setText("");
			}
			break;
		case R.id.findpwd_obtain_validenum:
			//TODO 发送短信验证码
			
			
			break;
		case R.id.confirm_button:
			phoneNum = mEditPhoneNum.getText().toString(); 
			pwd = mEditPwd.getText().toString();
			repeatPwd = mEditRepeatPwd.getText().toString();
			String validNum = mEditValidNum.getText().toString();
			if(!isValidatePhoneNum(phoneNum)){
				//TODO 提示手机号填写有误
				
				return;
			}
			if(!isValidatePwd(pwd)){
				//TODO 提示密码设置有误
				
				return;
			}
			if(!pwd.equals(repeatPwd)){
				//TODO 提示两次输入密码不同
				
				return;
			}
			if(!isValideNumRight(validNum)){
				//TODO 提示验证码错误
				
				return;
			}
			//TODO 可以登录了
			
			break;
		default:
			break;
		}
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
		
		mEditRepeatPwd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String pwd = mEditPwd.getText().toString();
				String repeatPwd = mEditRepeatPwd.getText().toString();
				if(pwd.length() == 0){
					mRepeatPwdIcon.setVisibility(View.INVISIBLE);
				}else{
					mRepeatPwdIcon.setVisibility(View.VISIBLE);
				}
				if(isValidatePwd(pwd) && (pwd.equals(repeatPwd))){
					//TODO
					mRepeatPwdIcon.setImageResource(R.drawable.login_right_state);
				}else{
					//TODO
					mRepeatPwdIcon.setImageResource(R.drawable.login_wrong_state);
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
		//密码不可以超过20位
		if(pwd.isEmpty() || pwd.trim().length() < 6 || pwd.trim().length() > 20){
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
