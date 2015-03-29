package com.guozha.buy.global;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.guozha.buy.entry.global.QuickMenu;
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
	
	private List<QuickMenu> mQuickMenus;				   //快捷菜单列表
	
	private int mUserId;
	private String mUserToken;
	private String mUserPwd;
	private String mMobileNumber;
	private String mWarnTime;
	
	private static final String USER_ID = "user_id";  				//用户ID
	private static final String USER_TOKEN = "user_token";  		//用户TOKEN
	private static final String USER_PWD = "user_pwd";      		//用户密码
	private static final String MOBILE_NUMBER = "mobile_number"; 	//账号（手机号)
	private static final String WARN_TIME = "warn_time";			//提醒时间
	
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
		mWarnTime = sharedPreference.getString(WARN_TIME, "16:00");
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
	
	/**
	 * 设置快捷菜单
	 * @param quickMenus
	 */
	public void setQuickMenus(List<QuickMenu> quickMenus){
		if(quickMenus.isEmpty()){
			mQuickMenus = null;
		}else{
			mQuickMenus = quickMenus;
		}
		StringBuffer quickBuf = new StringBuffer();
		for(int i = 0; i < quickMenus.size(); i++){
			QuickMenu quickMenuId = quickMenus.get(i);
			if(quickMenuId == null) continue;
			quickBuf.append(quickMenuId.getMenuId());
			quickBuf.append(":");
			quickBuf.append(quickMenuId.getName());
			quickBuf.append(",");
		}
		String quickStr;
		if(quickBuf.length() >= 1){
			quickStr = quickBuf.deleteCharAt(quickBuf.length() -1).toString();
		}else{
			quickStr = null;
		}
		Editor editor = sharedPreference.edit();
		editor.putString("quickMenusId", quickStr);
		editor.commit();
	}
	
	/**
	 * 获取快捷菜单
	 * @return
	 */
	public List<QuickMenu> getQuickMenus(){
		if(mQuickMenus != null) return mQuickMenus;
		String quickStr = sharedPreference.getString("quickMenusId", null);
		if(quickStr == null) {
			//TODO 这里前面的5个ID不能变化，必须是（0-4）
			quickStr = "0:干货,1:蔬菜,2:肉类,3:水果,4:水产";
		}
		String[] menus = quickStr.split(",");
		List<QuickMenu> quickMenus = new ArrayList<QuickMenu>();
		QuickMenu quickMenu;
		for(int i = 0; i < menus.length; i++){
			String[] quickMenusArr = menus[i].split(":");
			if(quickMenusArr.length != 2) continue;
			quickMenu = new QuickMenu(Integer.parseInt(quickMenusArr[0]), quickMenusArr[1]);
			quickMenus.add(quickMenu);
		}
		return quickMenus;
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
	private void setConfig(String configType, String changedData){
		Editor editor = sharedPreference.edit();
		editor.putString(configType, changedData);
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
	
	////////////////////////////////逻辑相关//////////////////////////////////
	
	/**
	 * 清空用户信息
	 */
	public void clearUserInfor(){
		setUserId(-1);
		setUserPwd(null);
		setUserToken(null);
		setMobileNum(null);
	}

}
