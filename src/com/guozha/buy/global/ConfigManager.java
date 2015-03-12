package com.guozha.buy.global;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 简单配置管理类
 * @author PeggyTong
 *
 */
public class ConfigManager {
	
	private static final String STORE_NAME = "settings";
	
	private static final ConfigManager instance = new ConfigManager();
	
	//快捷菜单选中项
	//private static final String QUICK_MENU_CHECK_BOX_SELECT = "quick_menu_check_box_select";  
	
	/**
	 * 获取配置管理对象
	 * @return
	 */
	public static ConfigManager getInstance(){
		return instance;
	}
	
	private ConfigManager(){
		SharedPreferences shared = CustomApplication.getContext()
				.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
		
	}

}
