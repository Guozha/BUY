package com.guozha.buy.util;

/**
 * 单位转换工具类
 * @author PeggyTong
 *
 */
public class UnitConvertUtil {
	
	private static final int UNIT_KE = 1;  //克
	private static final int UNIT_HE = 2;  //盒
	private static final int UNIT_KUAI = 3;  //块
	private static final int UNIT_DAI = 4;  //袋
	private static final int UNIT_BAO = 5;  //包
	private static final int UNIT_PIN = 6;  //瓶
	private static final int UNIT_LAN = 7;  //蓝
	private static final int UNIT_FEN = 8;  //份
	
	public static int getAmountByUnit(String unittext){
		if("两".equals(unittext)){
			return 50;
		}else if("斤".equals(unittext)){
			return 500;
		}else{
			return 1;
		}
	}
	
	/**
	 * 获取转换后的单位
	 * @param unit
	 * @return
	 */
	public static String getSwichedUnit(int amount, String unit){
		try{
			int unitId = Integer.parseInt(unit);
			switch (unitId) {
			case UNIT_KE:
				if(amount < 50){
					return "克";
				}else if(amount < 500){
					return "两";
				}else{
					return "斤";
				}
			case UNIT_HE:
				return "盒";
			case UNIT_KUAI:
				return "块";
			case UNIT_DAI:
				return "袋";
			case UNIT_BAO:
				return "包";
			case UNIT_PIN:
				return "瓶";
			case UNIT_LAN:
				return "篮";
			case UNIT_FEN:
				return "份";
			}
			return "";
		}catch(Exception e){
			return "";
		}
	}

	/**
	 * 获取转换后的数量单位
	 * @param amount
	 * @param unit
	 * @return
	 */
	public static String getSwitchedWeight(int amount, String unit){
		try{
			int unitId = Integer.parseInt(unit);
			switch (unitId) {
			case UNIT_KE:
				if(amount < 50){
					return amount + "克";
				}else if(amount < 500){
					return (Math.round((float)amount / 5)) / 10.0 + "两";
				}else{
					return (Math.round((float)amount / 5)) / 100.0 + "斤";
				}
			case UNIT_HE:
				unit = "盒";
				return amount + unit;
			case UNIT_KUAI:
				unit = "块";
				return amount + unit;
			case UNIT_DAI:
				unit = "袋";
				return amount + unit;
			case UNIT_BAO:
				unit = "包";
				return amount + unit;
			case UNIT_PIN:
				unit = "瓶";
				return amount + unit;
			case UNIT_LAN:
				unit = "篮";
				return amount + unit;
			case UNIT_FEN:
				unit = "份";
				return amount + unit;
			}
			return "";
		}catch(Exception e){
			return "";
		}
	}
	
	/**
	 * 获取转换后的价格
	 * @param price
	 * @return
	 */
	public static double getSwitchedMoney(int price){
		return price / 100.0;
	}
	
	public static double getSwitchedMoney(float price){
		return price / 100.0;
	}
	
	/**
	 * 转换成服务器提交时的接口单位和数据
	 * @param amout
	 * @param unit
	 * @return
	 */
	public static int getCommitWeight(int amount, String unit){
		if("斤".equals(unit)){
			return amount  * 500;
		}else if("两".equals(unit)){
			return amount * 50;
		}else{
			return amount;
		}
	}
	
	/**
	 * 获取界面显示的星期几
	 * @param weak
	 * @return
	 */
	public static String getWeakString(int weak){
		if(weak == 0) return "";
		weak = weak % 7;
		if(weak == 0){
			weak = 7;
		}
		switch (weak) {
		case 1:
			return "星期一";
		case 2:
			return "星期二";
		case 3:
			return "星期三";
		case 4:
			return "星期四";
		case 5:
			return "星期五";
		case 6:
			return "星期六";
		case 7:
			return "星期日";
		}
		return "";
	}
	
	/**
	 * 获取菜豆对应的人民币
	 * @param beanNum
	 * @return
	 */
	public static double getBeanMoney(int beanNum){
		return getSwitchedMoney(beanNum);
	}
	
	/**
	 * 获取价格更加数量和单价
	 * @param amount
	 * @param unitPrice
	 * @param unit
	 * @return
	 */
	public static double getPriceByAmount(int amount, int unitPrice, String unit){
		LogUtil.e("getPrice___amount = " + amount);
		LogUtil.e("getPrice___unitPrice = " + unitPrice);
		LogUtil.e("getPrice___unit = " + unit);
		try{
			int unitId = Integer.parseInt(unit);
			if(UNIT_KE == unitId){
				return getSwitchedMoney((float)amount * unitPrice / 500);
			}
			return getSwitchedMoney(amount * unitPrice);
		}catch(Exception e){
			return 0.0;
		}
	}
}
