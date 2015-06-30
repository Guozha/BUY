package com.guozha.buy.controller;

import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.guozha.buy.R;
import com.guozha.buy.entry.global.UserInfor;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.SystemModel;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.SystemModelResult;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * 闪屏界面
 * @author lixiaoqiang
 *
 */
public class SplashActivity extends Activity{
	
	//private static final String PAGE_NAME = "SplashPage";
	
	private static final int SPLASH_TIME = 2000; //闪屏持续时间
	private static final int MSG_START_INIT = 0x0001;  //开始初始化
	private static final int MSG_TURN_MAIN = 0x0002;  //转向MainActivity
	
	private long mInitStartTime;
	private boolean mHasInit;
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	private SystemModel mSystemModel = new SystemModel(new MySystemModelResult());
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_START_INIT:
				mInitStartTime = SystemClock.elapsedRealtime();
				doInit();
				int remainDelay =(int)(SPLASH_TIME - 
						(SystemClock.elapsedRealtime() - mInitStartTime));
				if(remainDelay <= 0){
					turnOtherActivity();
				}else{
					handler.sendEmptyMessageDelayed(MSG_TURN_MAIN, remainDelay);
				}
				break;
			case MSG_TURN_MAIN:
				turnOtherActivity();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		mHasInit = false;
		initJPush();
		//友盟统计设置为debug模式
		//TODO 在发布的时候注意修改
		MobclickAgent.setDebugMode(false);
		//禁止默认的页面统计方式
		MobclickAgent.openActivityDurationTrack(false);
	}
	
	/**
	 * 初始化极光推送相关
	 */
	private void initJPush(){
		//极光推送相关
		//TODO 注意发布的时候修改Debug模式
		JPushInterface.setDebugMode(false); //设置为Debug模式
		JPushInterface.init(this); //初始化极光SDK
		
		//自定义Notification样式
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
		builder.statusBarDrawable = R.drawable.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; //设置为自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE
				| Notification.DEFAULT_LIGHTS; //设置为铃声和振动都要
		
		//设置默认样式
		JPushInterface.setPushNotificationBuilder(1, builder);
		JPushInterface.setDefaultPushNotificationBuilder(builder);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && !mHasInit){
			handler.sendEmptyMessage(MSG_START_INIT);
		}
	}
	
	/**
	 * 跳转到主界面
	 */
	private void turnOtherActivity(){
		Intent intent;
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			int currentVersionCode = packageInfo.versionCode;
			String currentVersionName = packageInfo.versionName;
			//如果版本号大于旧版本号则去引导页
			if(currentVersionCode > ConfigManager.getInstance().getVersionCode()){
				intent = new Intent(SplashActivity.this, GuideActivity.class);
				ConfigManager.getInstance().setVersionCode(currentVersionCode);
				ConfigManager.getInstance().setVersionName(currentVersionName);
			}else{
				intent = new Intent(SplashActivity.this, MainActivity.class);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			intent = new Intent(SplashActivity.this, MainActivity.class);
		}
		startActivity(intent);
		SplashActivity.this.finish();
	}
	
	/**
	 * 做初始化操作
	 */
	private void doInit(){
		//TODO 做一些业务逻辑（比如加载资源）
		//初始化配置文件
		ConfigManager.getInstance();
		
		//判断网络状态
		if(!Util.isNetConnection(this)){
			ToastUtil.showToast(this, "您的网络好像有问题哦~");
			return;
		}
	    //请求图片路径
		mSystemModel.requestImagePath(SplashActivity.this);
		//自动登录应用
		mSystemModel.requestSystemTime(SplashActivity.this);
		String token = ConfigManager.getInstance().getUserToken();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		//请求微信号
		mSystemModel.requestWeixinNum(SplashActivity.this, token, addressId);
		
		String mobileNum = ConfigManager.getInstance().getMobileNum();
		String pwd = ConfigManager.getInstance().getUserPwd();
		if(mobileNum != null && pwd != null){
			mUserModel.requestPwdLogin(SplashActivity.this, mobileNum, pwd);
		}
		//设置别名和标签
		//Set<String> tags = new HashSet<String>();
		int userId = ConfigManager.getInstance().getUserId();
		JPushInterface.setAliasAndTags(this, String.valueOf(userId), 
				null, new TagAliasCallback() {
			@Override
			public void gotResult(int responseCode, String alias, Set<String> tags) {
				LogUtil.e("responseCode = " +responseCode);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//极光推送 
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//极光推送
		JPushInterface.onPause(this);
	}
	
	class MyUserModelResult extends UserModelResult{

		@Override
		public void requestPasswordLogin(String returnCode, String msg,
				UserInfor userInfor) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode) && userInfor != null){
				ConfigManager.getInstance().setUserId(userInfor.getUserId());
				ConfigManager.getInstance().setUserToken(userInfor.getUserToken());
				ConfigManager.getInstance().setMobileNum(userInfor.getMobileNo());
				//TODO 登录完成应该请求地址列表，设置默认地址
				//判断是否有地址
				mUserModel.requestListAddress(SplashActivity.this, ConfigManager.getInstance().getUserId());
			}else{
				ToastUtil.showToast(SplashActivity.this, msg);
			}
		}
		
		@Override
		public void requestListAddressResult(List<AddressInfo> addressInfos) {
			if(addressInfos != null && !addressInfos.isEmpty()){
				ConfigManager.getInstance()
					.setChoosedAddressId(addressInfos.get(0).getAddressId());
			}else{
				ConfigManager.getInstance().setChoosedAddressId(-1);
			}
		}
	}
	
	class MySystemModelResult extends SystemModelResult{
		@Override
		public void requestSystemTime(long systemTime) {
			ConfigManager.getInstance().setTodayDate(systemTime);
		}
		
		@Override
		public void requestImagePathResult(String imgPath) {
			if(imgPath == null) return;
			ConfigManager.getInstance().setImagePath(imgPath);
		}
		
		@Override
		public void requestWeixinNumResult(String weixinnum) {
			if(weixinnum == null) return;
			ConfigManager.getInstance().setWeixinNum(weixinnum);
		}
	}
}
