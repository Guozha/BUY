package com.guozha.buy.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 简单配置管理类
 * @author PeggyTong
 *
 */
public class ConfigManager {
	
	private static final String STORE_NAME = "settings";
	
	private static final ConfigManager instance = new ConfigManager();
	private SharedPreferences sharedPreference;
	
	private String mUserId;
	private String mUserToken;
	private String mMobileNumber;
	
	private static final String USER_ID = "user_id";  //用户ID
	private static final String USER_TOKEN = "user_token";  //用户TOKEN
	private static final String MOBILE_NUMBER = "mobile_number"; //账号（手机号)
	
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
		mUserId = sharedPreference.getString(USER_ID, "-1");
		mUserToken = sharedPreference.getString(USER_TOKEN, "-1");
		mMobileNumber = sharedPreference.getString(MOBILE_NUMBER, "-1");
	}
	
	/**
	 * 设置配置
	 * @param configType
	 * @param changedData
	 */
	private void setConfig(String configType, String changedData){
		if(sharedPreference == null) return;
		Editor editor = sharedPreference.edit();
		editor.putString(configType, changedData);
		editor.commit();
	}
	
	/**
	 * 获取用户ID
	 * @return
	 */
	public String getUserId(){
		return mUserId;
	}
	
	/**
	 * 设置用户ID
	 */
	public void setUserId(String userId){
		if(mUserId.equals(userId)) return;
		mUserId = userId;
		setConfig(USER_ID, userId);
	}
	
	/**
	 * 获取用户Token
	 * @return
	 */
	public String getUserToken(){
		return mUserToken;
	}
	
	/**
	 * 设置用户Token
	 * @param userToken
	 */
	public void setUserToken(String userToken){
		if(mUserToken.equals(userToken)) return;
		mUserToken = userToken;
		setConfig(USER_TOKEN, userToken);
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
		if(mMobileNumber.equals(mobileNum)) return;
		mMobileNumber = mobileNum;
		setConfig(MOBILE_NUMBER, mobileNum);
	}

}
