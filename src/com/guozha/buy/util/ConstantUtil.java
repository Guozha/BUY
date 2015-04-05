package com.guozha.buy.util;

/**
 * 和服务器约定的常量转换
 * @author PeggyTong
 *
 */
public class ConstantUtil {

	/**
	 * 获取菜谱制作难易程度
	 * @param type
	 * @return
	 */
	public static String getCookHardType(String key){
		int type;
		try{
			type = Integer.parseInt(key);
		}catch(Exception e){
			return "";
		}
		switch (type) {
		case 1:
			return "简单";
		case 2:
			return "中等";
		case 3:
			return "复杂";
		}
		return "";
	}
}
