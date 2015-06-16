package com.guozha.buy.global.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guozha.buy.controller.DebugActivity;
import com.guozha.buy.util.HttpUtil;

/**
 * 请求参数
 * @author PeggyTong
 *
 */
public class RequestParam {
	
	/**
	 * 服务器路径
	 */
	//正式服
	public static String URL = DebugActivity.TEST_URL;
	//public static String IMG_URL = URL;
	
	private Map<String, String> mParams = new HashMap<String, String>();
	
	private String mParamsPath;
	private String mUri;
	
	public RequestParam(String uri){
		if(uri == null) mUri = "";
		mUri = uri;
	}
	
	/**
	 * 设置参数
	 * @param key
	 * @param value
	 */
	public RequestParam setParams(String key, String value){
		//遇到空格需要转换
		if(value != null){
			value = value.trim().replaceAll(" ", "_");
		}
		mParams.put(key, value);
		return this;
	}
	
	/**
	 * 设置参数
	 * @param key
	 * @param value
	 * @return
	 */
	public RequestParam setParams(String key, int value){
		mParams.put(key, String.valueOf(value));
		return this;
	}

	
	/**
	 * 设置参数
	 * @param key
	 * @param values
	 */
	public void setParams(String key, String[] values){
		
	}
	
	/**
	 * 设置参数
	 * @param key
	 * @param values
	 */
	public void setParams(String key, List<String> values){
		
	}
	
	@Override
	public String toString() {
		mParamsPath =  URL + mUri + HttpUtil.generatedAddress(mParams);
		String resultPath = "";
		try {
			//ISO-8859-1
			resultPath = new String(mParamsPath.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//return resultPath;
		return mParamsPath;
	}
}
