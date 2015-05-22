package com.guozha.buy.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
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
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.ui.dialog.CustomDialog;
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
	
	private EditText mEditInvitation;	//邀请码
	
	private Button mObtainValidNumButton;
	private Button mRegistButton;
	
	private CheckBox mProtocalAffirmCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		setResult(0);
		
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
		
		mEditInvitation = (EditText) findViewById(R.id.regist_invitation);
		
		findViewById(R.id.regist_licence).setOnClickListener(this);
		
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
			String invitationNum = mEditInvitation.getText().toString();
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
			requestRegist(phoneNum, pwd, validNum, invitationNum);
			break;
		case R.id.regist_licence:
			Intent intent = new Intent(RegistActivity.this, LicenceActivity.class);
			startActivity(intent);
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
	private void requestRegist(final String phoneNum, final String pwd, String validNum, String invitationNum) {		
		RequestParam paramPath = new RequestParam("account/register")
		.setParams("mobileNo", phoneNum)
		.setParams("passwd", pwd)
		.setParams("inviteCode", invitationNum)
		.setParams("checkCode", validNum);
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
						requestLogin(phoneNum, pwd);
					}else if("2".equals(returnCode)){
						mEditInvitation.setText("");
						CustomDialog invitDialog = 
								new CustomDialog(RegistActivity.this, R.layout.dialog_invitnum_error);
						invitDialog.getWindow();
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
	 * 请求登录
	 * @param phoneNum
	 * @param pwd
	 */
	private void requestLogin(String phoneNum, final String pwd) {
		RequestParam paramPaht = new RequestParam("account/login")
		.setParams("mobileNo", phoneNum)
		.setParams("passwd", pwd);
		HttpManager.getInstance(this).volleyJsonRequestByPost(
			HttpManager.URL + paramPaht, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode");
					if("1".equals(returnCode.trim())){
						Integer userId = response.getInt("userId");
						String userToken = response.getString("token");
						String mobileNum = response.getString("mobileNo");
						ConfigManager.getInstance().setUserId(userId);
						ConfigManager.getInstance().setUserToken(userToken);
						ConfigManager.getInstance().setUserPwd(pwd);
						ConfigManager.getInstance().setMobileNum(mobileNum);
						//请求地址数据
						MainPageInitDataManager.getInstance().getAddressInfos(null);
						MainPageInitDataManager.mAccountUpdated = true;  //允许用户账户信息变化
						MainPageInitDataManager.mCartItemsUpdated = true; //允许更新购物车数据
						MainPageInitDataManager.mAddressUpdated = true;   //允许更新地址数据
						Intent intent = getIntent();
						if(intent != null){
							intent.putExtra("successed", true);
							setResult(0, intent);
						}
						RegistActivity.this.finish();
					}else{
						String msg = response.getString("msg");
						ToastUtil.showToast(RegistActivity.this, msg);
					}
				} catch (JSONException e) {
					ToastUtil.showToast(RegistActivity.this, "数据解析异常");
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
		RequestParam paramPath = new RequestParam("account/checkCodeForRegister")
		.setParams("mobileNo", phoneNum);
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
