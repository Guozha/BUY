package com.guozha.buy.global;

import java.io.InputStream;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.controller.LoginActivity;
import com.guozha.buy.controller.mine.AddAddressActivity;
import com.guozha.buy.util.XMLUtil;

/**
 * 简单配置管理类
 * @author PeggyTong
 *
 */
public class ConfigManager{
	
	private static final String STORE_NAME = "settings";
	
	private static final ConfigManager instance = new ConfigManager();
	private SharedPreferences sharedPreference;
	
	private Map<String, Map<String, String>> mConstantXML; //常量配置文件
	
	private int mUserId;
	private String mUserToken;
	private String mUserPwd;
	private String mMobileNumber;
	private String mWarnTime;
	private int mChoosedAddressId;					   //用户当前选择的地址Id
	private int mVersionCode;						   //当前版本号
	private String mVersionName;					   //版本名称
	private boolean mWarnTimeOpend;					   //提醒开关是否打开
	private long mTodayDate;						   //今天的日期 
	private int mCartNumber = -1; 					   //购物车数量
	private String mImagePath;						   //图片路径
	private int mDefaultPayWay; 					   //默认支付方式
	private String mWeixinNum;							   //微信服务号
	
	/*
	 * 费用活动规则相关
	 */
	private String mServiceFeeRuleTitle;
	private String mServiceFeeRuleContent;
	private String mInvateRuleTitle;
	private String mInvateRuleContent;
	
	private static final String USER_ID = "user_id";  				//用户ID
	private static final String USER_TOKEN = "user_token";  		//用户TOKEN
	private static final String USER_PWD = "user_pwd";      		//用户密码
	private static final String MOBILE_NUMBER = "mobile_number"; 	//账号（手机号)
	private static final String WARN_TIME = "warn_time";			//提醒时间
	private static final String CHOOSED_ADDRESS_ID = "choosed_address_id";//用户选择的地址Id
	private static final String VERSION_CODE = "version_code";		//当前版本号
	private static final String VERSION_NAME = "version_name";		//当前版本名称
	private static final String WARN_TIME_OPEND = "warn_time_opend";//提醒开关是否打开
	private static final String TODAY_DATE = "today_date";			//今天的日期
	private static final String IMAGE_PATH = "image_path";			//图片路径
	private static final String DEFAULT_PAYWAY = "default_payway";	//默认支付方式
	private static final String WEIXIN_NUM = "weixin_num";			//微信服务号
	
	/*
	 * 费用活动规则相关
	 */
	private static final String SERVICE_FEE_RULE_TITLE = "service_fee_rule_title";
	private static final String SERVICE_FEE_RULE_CONTENT = "service_fee_rule_content";
	private static final String INVATE_RULE_TITLE = "invate_rule_title";
	private static final String INVATE_RULE_CONTENT = "invate_rule_content";
	
	
	/**
	 * 获取配置管理对象
	 * @return
	 */
	public static ConfigManager getInstance(){
		return instance;
	}
	
	private ConfigManager(){
		sharedPreference = CustomApplication.getContext()
				.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
		mUserId = sharedPreference.getInt(USER_ID, -1);
		mUserToken = sharedPreference.getString(USER_TOKEN, null);
		mUserPwd = sharedPreference.getString(USER_PWD, null);
		mMobileNumber = sharedPreference.getString(MOBILE_NUMBER, null);
		mWarnTime = sharedPreference.getString(WARN_TIME, null);
		mChoosedAddressId = sharedPreference.getInt(CHOOSED_ADDRESS_ID, -1);
		mVersionCode = sharedPreference.getInt(VERSION_CODE, -1);
		mVersionName = sharedPreference.getString(VERSION_NAME, null);
		mWarnTimeOpend = sharedPreference.getBoolean(WARN_TIME_OPEND, false);
		mTodayDate = sharedPreference.getLong(TODAY_DATE, -1);
		mImagePath = sharedPreference.getString(IMAGE_PATH, "");
		mDefaultPayWay = sharedPreference.getInt(DEFAULT_PAYWAY, 1);
		mWeixinNum = sharedPreference.getString(WEIXIN_NUM, "aizhangshaohz");
		
		mServiceFeeRuleTitle = sharedPreference.getString(SERVICE_FEE_RULE_TITLE, "服务费规则");
		mServiceFeeRuleContent = sharedPreference.getString(SERVICE_FEE_RULE_CONTENT, "很抱歉，规则获取失败");
		mInvateRuleTitle = sharedPreference.getString(INVATE_RULE_TITLE, "活动规则");
		mInvateRuleContent = sharedPreference.getString(INVATE_RULE_CONTENT, "很抱歉，规则获取失败");
		initConfigXML();
	}
	
	/**
	 * 初始化constant.xml文件
	 */
	private void initConfigXML(){
		if(mConstantXML != null) return;
		new Thread(){
			public void run() {
				try {
					InputStream inStream = 
						CustomApplication.getContext().getAssets().open("constant.xml");
					mConstantXML = XMLUtil.getConfigXml(inStream);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			};
		}.start();
	}
	
	private OnConfigChangeListener mConfigChangeListener;
	
	public void setOnConfigChangeListener(OnConfigChangeListener configChangeListener){
		mConfigChangeListener = configChangeListener;
	}
	
	public interface OnConfigChangeListener{
		public void cartNumChanged();
	}
	
	/**
	 * 设置配置
	 * @param configType
	 * @param changedData
	 */
	private void setConfig(String configType, int changedData){
		Editor editor = sharedPreference.edit();	
		editor.putInt(configType, changedData);
		editor.commit();
	}
	
	/**
	 * 设置配置
	 * @param configType
	 * @param changedData
	 */
	private void setConfig(String configType, long changedData){
		Editor editor = sharedPreference.edit();	
		editor.putLong(configType, changedData);
		editor.commit();
	}
	
	/**
	 * 设置配置
	 * @param configType
	 * @param changedData
	 */
	private void setConfig(String configType, String changedData){
		Editor editor = sharedPreference.edit();
		editor.putString(configType, changedData);
		editor.commit();
	}
	
	/**
	 * 设置配置
	 * @param configType
	 * @param changeData
	 */
	private void setConfig(String configType, boolean changeData){
		Editor editor = sharedPreference.edit();
		editor.putBoolean(configType, changeData);
		editor.commit();
	}
	
	/**
	 * 设置购物车数量
	 * @param cartNumber
	 */
	public void setCartNumber(int cartNumber){
		if(mCartNumber == cartNumber) return;
		mCartNumber = cartNumber;
		if(mConfigChangeListener != null){
			mConfigChangeListener.cartNumChanged();
		}
	}
	
	/**
	 * 获取购物车数量
	 * @return
	 */
	public int getCartNumber(){
		return mCartNumber;
	}
	
	/**
	 * 获取用户ID
	 * @return
	 */
	public int getUserId(){
		return mUserId;
	}
	
	/**
	 * 设置用户ID
	 */
	public void setUserId(int userId){
		if(mUserId == userId) return;
		mUserId = userId;
		setConfig(USER_ID, userId);
	}
	
	public int getChoosedAddressId(){
		return mChoosedAddressId;
	}
	
	/**
	 * 获取选择的地址
	 * @param context
	 * @return
	 */
	public int getChoosedAddressId(final Context context){
		if(mChoosedAddressId == -1){
			Intent intent = new Intent(context, AddAddressActivity.class);
			context.startActivity(intent);
		}
		return mChoosedAddressId;
	}
	
	/**
	 * 设置选择的地址ID
	 * @param choosedId
	 */
	public void setChoosedAddressId(int choosedId){
		if(mChoosedAddressId == choosedId) return;
		mChoosedAddressId = choosedId;
		setConfig(CHOOSED_ADDRESS_ID, choosedId);
	}
	
	public String getUserToken(){
		return mUserToken;
	}
	
	public String getUserToken(Context context){
		if(mUserToken == null){
			Intent intent = new Intent(context, LoginActivity.class);
			context.startActivity(intent);
		}
		return mUserToken;
	}
	
	/**
	 * 设置用户Token
	 * @param userToken
	 */
	public void setUserToken(String userToken){
		if(mUserToken != null && mUserToken.equals(userToken)) return;
		mUserToken = userToken;
		setConfig(USER_TOKEN, userToken);
	}
	
	/**
	 * 获取密码
	 * @return
	 */
	public String getUserPwd(){
		return mUserPwd;
	}
	
	/**
	 * 保存密码
	 * @param pwd
	 */
	public void setUserPwd(String pwd){
		if(mUserPwd != null && mUserPwd.equals(pwd)) return;
		mUserPwd = pwd;
		setConfig(USER_PWD, pwd);
	}
	
	/**
	 * 获取用户手机号码
	 * @return
	 */
	public String getMobileNum(){
		return mMobileNumber;
	}
	
	/**
	 * 更改用户手机号码
	 * @param mobileNum
	 */
	public void setMobileNum(String mobileNum){
		if(mMobileNumber != null && mMobileNumber.equals(mobileNum)) return;
		mMobileNumber = mobileNum;
		setConfig(MOBILE_NUMBER, mobileNum);
	}
	
	/**
	 * 获取提醒时间
	 * @return
	 */
	public String getWarnTime(){
		return mWarnTime;
	}
	
	/**
	 * 设置提醒时间
	 * @param warnTime
	 */
	public void setWarnTime(String warnTime){
		if(mWarnTime != null && mWarnTime.equals(warnTime)) return;
		mWarnTime = warnTime;
		setConfig(WARN_TIME, warnTime);
	}
	
	/**
	 * 设置图片路径
	 * @param imagePath
	 */
	public void setImagePath(String imagePath){
		if(mImagePath == imagePath) return;
		mImagePath = imagePath;
		setConfig(IMAGE_PATH, mImagePath);
	}
	
	/**
	 * 获取图片路径
	 * @return
	 */
	public String getImagePath(){
		return mImagePath;
	}
	
	/**
	 * 设置默认支付方式
	 * @param defaultPayWay
	 */
	public void setDefaultPayWay(int defaultPayWay){
		if(mDefaultPayWay == defaultPayWay) return;
		mDefaultPayWay = defaultPayWay;
		setConfig(DEFAULT_PAYWAY, mDefaultPayWay);
	}
	
	/**
	 * 获取默认支付方式
	 * @return
	 */
	public int getDefaultPayWay(){
		return mDefaultPayWay;
	}
	
	/**
	 * 获取常亮列表
	 * @return
	 */
	public Map<String, Map<String, String>> getConstantXML(){
		return mConstantXML;
	}
	
	/**
	 * 根据常亮类型返回map列表
	 * @param type
	 * @return
	 */
	public Map<String, String> getConstantMap(String type){
		if(mConstantXML == null) return null;
		return mConstantXML.get(type);
	}
	
	/**
	 * 获取常量的value值
	 * @param type
	 * @param key
	 * @return
	 */
	public String getConstantValue(String type, String key){
		Map<String, String> map = getConstantMap(key);
		if(map == null) return null;
		return map.get(key);
	}
	
	/**
	 * 设置版本号
	 * @param versionCode
	 */
	public void setVersionCode(int versionCode){
		if(mVersionCode == versionCode) return;
		mVersionCode = versionCode;
		setConfig(VERSION_CODE, versionCode);
	}
	
	/**
	 * 获取版本号
	 * @return
	 */
	public int getVersionCode(){
		return mVersionCode;
	}
	
	/**
	 * 设置版本名称
	 * @param versionName
	 */
	public void setVersionName(String versionName){
		if(mVersionName != null && mVersionName.equals(versionName)) return;
		mVersionName = versionName;
		setConfig(VERSION_NAME, versionName);
	}
	
	/**
	 * 获取版本名称
	 * @return
	 */
	public String getVersionName(){
		return mVersionName;
	}
	
	/**
	 * 设置提醒开关
	 * @param warnTimeOpend
	 */
	public void setWarnTimeOpend(boolean warnTimeOpend){
		if(warnTimeOpend == mWarnTimeOpend) return;
		mWarnTimeOpend = warnTimeOpend;
		setConfig(WARN_TIME_OPEND, warnTimeOpend);
	}
	
	/**
	 * 获取提醒开关状态
	 * @return
	 */
	public boolean getWarnTimeOpend(){
		return mWarnTimeOpend;
	}
	
	/**
	 * 设置今天的日期
	 * @param todayDate
	 */
	public void setTodayDate(long todayDate){
		if(mTodayDate == todayDate) return;
		mTodayDate = todayDate;
		setConfig(TODAY_DATE, todayDate);
	}
	
	/**
	 * 获取今天的日期
	 * @return
	 */
	public long getTodayDate(){
		return mTodayDate;
	}
	
	/**
	 * 获取微信服务号
	 * @return
	 */
	public String getWeixinNum(){
		return mWeixinNum;
	}
	
	/**
	 * 设置微信服务号
	 * @param weixinnum
	 */
	public void setWeixinNum(String weixinnum){
		if(weixinnum != null && weixinnum.equals(mWeixinNum))return;
		mWeixinNum = weixinnum;
		setConfig(WEIXIN_NUM, mWeixinNum);
	}
	
	////////////////////////规则相关////////////////////////
	
	public String getServiceFeeRuleTitle(){
		return mServiceFeeRuleTitle;
	}
	
	public String getServiceFeeRuleContent(){
		return mServiceFeeRuleContent;
	}
	
	public String getInvateRuleTitle(){
		return mInvateRuleTitle;
	}
	
	public String getInvateRuleContent(){
		return mInvateRuleContent;
	}
	
	public void setServiceFeeRuleTitle(String title){
		if(title != null && title.equals(mServiceFeeRuleTitle)) return;
		mServiceFeeRuleTitle = title;
		setConfig(SERVICE_FEE_RULE_TITLE, mServiceFeeRuleTitle);
	}
	
	public void setServiceFeeRuleContent(String content){
		if(content != null && content.equals(mServiceFeeRuleContent)) return;
		mServiceFeeRuleContent = content;
		setConfig(SERVICE_FEE_RULE_CONTENT, mServiceFeeRuleContent);
	}
	
	public void setInvateRuleTitle(String title){
		if(title != null && title.equals(mInvateRuleTitle)) return;
		mInvateRuleTitle = title;
		setConfig(INVATE_RULE_TITLE, mInvateRuleTitle);
	}
	
	public void setInvateRuleContent(String content){
		if(content != null && content.equals(mInvateRuleContent)) return;
		mInvateRuleContent = content;
		setConfig(INVATE_RULE_CONTENT, mInvateRuleContent);
	}
	
	
	////////////////////////////////逻辑相关//////////////////////////////////
	
	/**
	 * 清空用户信息
	 */
	public void clearUserInfor(){
		setUserId(-1);
		setUserToken(null);
	}
	
	public void loginOutClear(){
		clearUserInfor();
		setChoosedAddressId(-1);
		setCartNumber(0);
		setUserPwd(null);
	}
}
