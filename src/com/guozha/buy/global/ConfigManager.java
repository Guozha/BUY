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
	
	private int mUserId;
	private String mUserToken;
	private String mUserPwd;
	private String mMobileNumber;
	
	private static final String USER_ID = "user_id";  				//用户ID
	private static final String USER_TOKEN = "user_token";  		//用户TOKEN
	private static final String USER_PWD = "user_pwd";      		//用户密码
	private static final String MOBILE_NUMBER = "mobile_number"; 	//账号（手机号)
	
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
	}
	
	/**
	 * 设置配置
	 * @param configType
	 * @param changedData
	 */
	private void setConfig(String configType, int changedData){
		editorData(configType, null, changedData);
	}
	
	/**
	 * 设置配置
	 * @param configType
	 * @param changedData
	 */
	private void setConfig(String configType, String changedData){
		editorData(configType, changedData, -1);
	}
	
	private void editorData(String configType, String strData, int intData){
		if(sharedPreference == null) return;
		Editor editor = sharedPreference.edit();		
		if(strData == null){
			editor.putInt(configType, intData);
		}else{
			editor.putString(configType, strData);
		}
		editor.commit();
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
		setConfig(USER_TOKEN, pwd);
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

	
}
