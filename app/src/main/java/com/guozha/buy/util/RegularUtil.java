package com.guozha.buy.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证相关的工具类
 * @author PeggyTong
 *
 */
public class RegularUtil {

	/**
	 * 验证手机号码格式
	 * @param phoneNum
	 * @return
	 */
	public static boolean regularPhoneNum(String phoneNum){
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(phoneNum);
		return matcher.matches();
	}
	
	
	/**
	 * 验证是否为数字
	 * @param str
	 * @return
	 */
	public static boolean regularNumber(String str){
		Pattern pattern = Pattern.compile("^[0-9]*$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * 验证URL是否可用
	 * @param url
	 * @return
	 */
	public static boolean regularUrl(String url){
		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 验证邮箱地址格式是否正确
	 * @param email
	 * @return
	 */
	public static boolean isEmailValid(String email){
		boolean valid = true;
		final String pattern1 = 
				"^([a-z0-9A-Z_]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			valid = false;
		}
		return valid;
	}
}
