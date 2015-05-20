package com.guozha.buy.activity.global;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
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
		
		//注意：下面的操作和网络状态有关（联网状态下才请求）
		
		//初始化入口界面数据(这里最好传全局的context)
		MainPageInitDataManager initDataManager = MainPageInitDataManager.getInstance();
		//自动登录应用
		String mobileNum = ConfigManager.getInstance().getMobileNum();
		String pwd = ConfigManager.getInstance().getUserPwd();
		if(mobileNum != null && pwd != null){
			requestLogin(mobileNum, pwd);
		}
		//获取一级类目
		initDataManager.getQuickMenus(null);
		//获取地址列表
		initDataManager.getAddressInfos(null);
		//获取账户信息
		initDataManager.getAccountInfo(null);
		//获取菜单信息
		initDataManager.getGoodsItemType(null);
		//获取逛菜场首页数据
		initDataManager.getMarketHomePage(null, 1, 4);
		//获取系统当前时间
		initDataManager.getSystemTime(null);
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
	
	/**
	 * 请求登录
	 * @param phoneNum
	 * @param pwd
	 */
	private void requestLogin(String phoneNum, String pwd) {
		RequestParam paramPath = new RequestParam("account/login")
		.setParams("mobileNo", phoneNum)
		.setParams("passwd", pwd);
		HttpManager.getInstance(this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
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
						ConfigManager.getInstance().setMobileNum(mobileNum);
					}else{
						//String msg = response.getString("msg");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
}
