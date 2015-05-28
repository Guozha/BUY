package com.guozha.buy.activity.global;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.receiver.SMSBroadcastReceiver;
import com.guozha.buy.server.ValidNumTimer;
import com.guozha.buy.server.ValidNumTimer.TimerObserver;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.RegularUtil;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 登录界面
 * @author PeggyTong
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener, TimerObserver{
	
	private static final int HAND_VALID_NUM_SEND = 0x0001;
	private static final int HAND_REFRESH_BUTTON = 0x0002;
	private static final int HAND_SEND_COMPLETED = 0x0003;
	private static final String PAGE_NAME = "LoginPage";
	private static final int REQUEST_CODE = 0;		//请求状态码		
	
	//登录成功后 跳转控制器的 路径
	public static final String SUCCESS_TURN_INTENT = "success_turn_intent";
	private TextView mLicenceText;
	private CheckBox mLicenceCheckBox;
	private EditText mEditPhoneNum;  
	private EditText mEditValidNum;
	private EditText mEditInvitation;  //邀请码
	private ImageView mPhoneNumIcon;
	private Button mObtainValiNumBtton;
	private Button mLoginButton;
	
	private String mSuccessIntent = null;
	
	private int mTimeCount = 0;
	
	//接收短信
	private SMSBroadcastReceiver mSMSBroadcastReceiver;
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_VALID_NUM_SEND:
				buttonChangeTimer();
				//开始倒计时
				ValidNumTimer.getInstance().startTimer();
				break;
			case HAND_REFRESH_BUTTON:
				mObtainValiNumBtton.setText("剩余" + mTimeCount + "秒");
				break;
			case HAND_SEND_COMPLETED:
				buttonChangeNomal("重新获取");
				break;
			}
		}
	};
	
	/**
	 * 验证码 按钮改成计时的样式
	 */
	private void buttonChangeTimer() {
		mObtainValiNumBtton.setTextColor(getResources().getColor(R.color.color_app_base_4));
		mObtainValiNumBtton.setBackgroundResource(R.drawable.main_dialog_right_background_pressed);
		mObtainValiNumBtton.setClickable(false);
		mObtainValiNumBtton.setFocusable(false);
	};
	
	/**
	 * 验证码 按钮的普通样式
	 */
	private void buttonChangeNomal(String buttonText){
        mObtainValiNumBtton.setTextColor(getResources().getColor(R.color.color_app_base_2));
        mObtainValiNumBtton.setBackgroundResource(R.drawable.main_dialog_right_background);
		mObtainValiNumBtton.setText(buttonText);
		mObtainValiNumBtton.setFocusable(true);
		mObtainValiNumBtton.setClickable(true);
	}
	
	@Override
	public void timeChanged(int time) {
		mTimeCount = time;
		if(time <= 0){
			mHandler.sendEmptyMessage(HAND_SEND_COMPLETED);
		}else{
			mHandler.sendEmptyMessage(HAND_REFRESH_BUTTON);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		customActionBarStyle("登录");
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mSuccessIntent = bundle.getString(SUCCESS_TURN_INTENT);
		}
		initView();
		textChangeWatch();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {
            	LogUtil.e("message = " + message);
            	ToastUtil.showToast(LoginActivity.this, message);
            	
            	mEditValidNum.setText(getNumbers(message));
            }
        });
        //注册时间倒计时观察者
        ValidNumTimer.getInstance().registObserver(this);
	}
	
	//截取数字 
	public String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		 //注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
        //注销时间倒计时观察者
        ValidNumTimer.getInstance().cancelObserver(this);
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		mEditPhoneNum = (EditText) findViewById(R.id.login_phonenum);
		mEditValidNum = (EditText) findViewById(R.id.login_validenum);
		mObtainValiNumBtton = (Button) findViewById(R.id.login_obtain_validenum);
		
		mPhoneNumIcon = (ImageView) findViewById(R.id.login_phonenum_clear);
		
		mEditInvitation = (EditText) findViewById(R.id.login_invitation);
		
		mLoginButton = (Button) findViewById(R.id.login_button);
		mLicenceText = (TextView) findViewById(R.id.login_licence);
		mLicenceCheckBox = (CheckBox) findViewById(R.id.login_protocal_affirm);
		mObtainValiNumBtton.setOnClickListener(this);
		mPhoneNumIcon.setOnClickListener(this);
		mLoginButton.setOnClickListener(this);
		mLicenceText.setOnClickListener(this);
		
		String mobileNum = ConfigManager.getInstance().getMobileNum();
		mEditPhoneNum.setText(mobileNum);
		
		if(ValidNumTimer.getInstance().getTimerCount() > 0){
			buttonChangeTimer();
		}else{
			buttonChangeNomal(getResources().getString(R.string.activity_regist_obtain_verify_button_text));
		}
	}
	
	@Override
	public void onClick(View view) {
		String phoneNum;
		String validNum;
		switch (view.getId()) {
		case R.id.login_phonenum_clear:
			phoneNum = mEditPhoneNum.getText().toString(); 
			if(!isValidatePhoneNum(phoneNum)){
				mEditPhoneNum.setText("");
			}
			break;
		case R.id.login_button:
			phoneNum = mEditPhoneNum.getText().toString(); 
			validNum = mEditValidNum.getText().toString();
			if(!isValidatePhoneNum(phoneNum)){
				ToastUtil.showToast(this, "手机号码不正确");
				return;
			}
			if(!mLicenceCheckBox.isChecked()){
				ToastUtil.showToast(this, "请先同意用户协议");
				return;
			}
			//请求登录
			//TODO 这里成了验证码了
			requestLogin(phoneNum, validNum);
			
			break;
		case R.id.login_obtain_validenum:
			phoneNum = mEditPhoneNum.getText().toString(); 
			if(!isValidatePhoneNum(phoneNum)){
				//TODO 提示填写有误
				ToastUtil.showToast(this, "手机号码不正确");
				return;
			}
			obtainPoneValidate(phoneNum);
			break;
		case R.id.login_licence:
			Intent intent = new Intent(LoginActivity.this, LicenceActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 获取验证码
	 * 
	 * @param phoneNum
	 */
	private void obtainPoneValidate(String phoneNum) {
		RequestParam paramPath = new RequestParam(
				"account/checkCodeForOnekeyLogin").setParams("mobileNo", phoneNum);
		
		LogUtil.e("path = " + HttpManager.URL + paramPath);
		
		HttpManager.getInstance(this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response
								.getString("returnCode");
						if ("1".equals(returnCode.trim())) {
							ToastUtil.showToast(LoginActivity.this,
									"验证码已发送");
							mHandler.sendEmptyMessage(HAND_VALID_NUM_SEND);
						} else {
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
	 * 请求登录
	 * @param phoneNum
	 * @param pwd
	 */
	private void requestLogin(String phoneNum, final String checkCode) {
		String invitationNum = mEditInvitation.getText().toString();
		RequestParam paramPaht = new RequestParam("account/onekeyLogin")
		.setParams("mobileNo", phoneNum)
		.setParams("checkCode", checkCode)
		.setParams("inviteCode", invitationNum);
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
						String pwd = response.getString("passwd");
						ConfigManager.getInstance().setUserId(userId);
						ConfigManager.getInstance().setUserToken(userToken);
						ConfigManager.getInstance().setUserPwd(pwd);
						ConfigManager.getInstance().setMobileNum(mobileNum);
						ToastUtil.showToast(LoginActivity.this, "登录成功");
						//请求地址数据
						MainPageInitDataManager.getInstance(CustomApplication.getContext()).getAddressInfos(null);
						if(mSuccessIntent != null){
							try {
								Intent intent = new Intent(LoginActivity.this, Class.forName(mSuccessIntent));
								startActivity(intent);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
						MainPageInitDataManager.mAccountUpdated = true;	//允许用户账户信息变化
						MainPageInitDataManager.mCartItemsUpdated = true; //允许更新购物车数据
						MainPageInitDataManager.mAddressUpdated = true;   //允许更新地址数据
						MainPageInitDataManager.getInstance(
								CustomApplication.getContext()).getAddressInfos(null);
						LoginActivity.this.finish();
					}else{
						String msg = response.getString("msg");
						ToastUtil.showToast(LoginActivity.this, msg);
					}
				} catch (JSONException e) {
					ToastUtil.showToast(LoginActivity.this, "数据解析异常");
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 检测文字变化
	 */
	private void textChangeWatch() {
		if(mEditPhoneNum == null) return;
		
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
					mPhoneNumIcon.setImageResource(R.drawable.login_right_state);
				}else{
					mPhoneNumIcon.setImageResource(R.drawable.login_wrong_state);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE){
			if(data != null){
				Bundle bundle = data.getExtras();
				if(bundle != null){
					if(bundle.getBoolean("successed")){
						LoginActivity.this.finish();
					}
				}
			}
		}
	}
}
