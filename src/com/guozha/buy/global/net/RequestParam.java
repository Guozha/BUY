package com.guozha.buy.global.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guozha.buy.util.HttpUtil;

/**
 * 请求参数
 * @author PeggyTong
 *
 */
public class RequestParam {
	
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
		mParamsPath = mUri + HttpUtil.generatedAddress(mParams);
		return mParamsPath;
	}
}
