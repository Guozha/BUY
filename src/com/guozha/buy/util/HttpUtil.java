package com.guozha.buy.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpUtil {

	/**
	 * 生成字符串地址参数
	 * @param params
	 * @return
	 */
	public static String generatedAddress(Map<String, String> params){
		if(params == null || params.isEmpty()) return "";
		StringBuffer sbuffer = new StringBuffer("?");
		
		Set<Entry<String, String>> set = params.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		iterator.hasNext();
		Entry<String, String> entry = iterator.next();
		sbuffer.append(entry.getKey()).append("=").append(entry.getValue());
		while(iterator.hasNext()){
			entry = iterator.next();
			sbuffer.append("&&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		return sbuffer.toString();
	}
}
