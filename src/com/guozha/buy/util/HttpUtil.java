package com.guozha.buy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	
	/**
	 * 使用MD5算法对传入的key进行加密并返回。
	 */
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}
	
	/**
	 * byte[]数组转hex
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
