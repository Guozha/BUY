package com.guozha.buy.global.net;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
		if(value == null) value = "";
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
	public RequestParam setParams(String key, Set<String> values){
		if(values == null || values.isEmpty()) {
			setParams(key, "");
			return this;
		}
		Iterator<String> iterator = values.iterator();
		StringBuffer value = new StringBuffer();
		while(iterator.hasNext()){
			value.append(iterator.next());
			value.append(",");
		}
		String valueStr = value.substring(0, value.lastIndexOf(","));
		setParams(key, valueStr);
		return this;
	}
	
	/**
	 * 设置参数
	 * @param key
	 * @param values
	 * @return
	 */
	public RequestParam setParams(String key,String[] values){
		if(values == null || values.length == 0){
			setParams(key, "");
			return this;
		}
		Set<String> set = new HashSet<String>(Arrays.asList(values));
		setParams(key, set);
		return this;
	}
	
	/**
	 * 设置参数
	 * @param key
	 * @param values
	 * @return
	 */
	public RequestParam setParams(String key, int[] values){
		StringBuffer value = new StringBuffer();
		for(int i = 0; i < values.length; i++){
			value.append(values[i]);
			value.append(",");
		}
		String valueStr = value.substring(0, value.lastIndexOf(","));
		setParams(key, valueStr);
		return this;
	}
	
	public Map<String, String> getParams(){
		return mParams;
	}
	
	public String getUrl(){
		return URL + mUri;
	}
	
	@Override
	public String toString() {
		mParamsPath =  URL + mUri + HttpUtil.generatedAddress(mParams);
		String resultPath = "";
		try {
			resultPath = new String(mParamsPath.getBytes(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return resultPath;
	}
}
