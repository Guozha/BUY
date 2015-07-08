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
	
	/**
	 * 获取菜谱类型
	 * @param key
	 * @return
	 */
	public static String getTicketType(String key){
		int type;
		try{
			type = Integer.parseInt(key);
		}catch(Exception e){
			return "";
		}
		switch (type) {
		case 1:
			return "（注册赠送）";
		case 2:
			return "（好友赠送）";
		case 3:
			return "（售后赠送）";
		case 4:
			return "（推广收益）";
		}
		return "";
	}
	
	/**
	 * 获取订单状态
	 * @param key
	 * @return
	 */
	public static String getOrderStatus(String key){
		int type;
		try{
			type = Integer.parseInt(key);
		}catch(Exception e){
			return "";
		}
		switch (type) {
		case 0:
			return "已取消";
		case 1:
			return "未支付";
		case 2:
			return "新订单";
		case 3:
			return "处理中";
		case 4:
			return "称重中";
		case 5:
			return "配送中";
		case 6:
			return "已签收";
		case 99:
			return "超时";
		}
		return "";
	}
	
	/**
	 * @param payStatus  支付状态
	 * @param arriviPayStatus 是否货到付款
 	 * @param commentStatus 是否评价过
	 * @return
	 */
	public static String getOrderStatusString(String status, String arriviPayStatus, String commentStatus){
		StringBuffer orderStatu = new StringBuffer();
		orderStatu.append(ConstantUtil.getOrderStatus(status));
		if("1".equals(arriviPayStatus)){
			orderStatu.append("(货到付款)");
		}
		if("06".equals(status)){
			if("1".equals(commentStatus)){
				orderStatu.append(",已评价");
			}else{
				orderStatu.append(",未评价");
			}
		}
		return orderStatu.toString();
	}
}
