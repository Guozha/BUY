package com.guozha.buy.util;

/**
 * 单位转换工具类
 * @author PeggyTong
 *
 */
public class UnitConvertUtil {
	
	private static final int UNIT1_KE = 1;  //克
	private static final int UNIT2_HE = 2;  //盒
	private static final int UNIT3_KUAI = 3;  //块
	private static final int UNIT4_DAI = 4;  //袋
	private static final int UNIT5_BAO = 5;  //包
	private static final int UNIT6_PIN = 6;  //瓶
	private static final int UNIT7_LAN = 7;  //蓝
	private static final int UNIT8_FEN = 8;  //份
	private static final int UNIT9_DUI = 9;  //对  
	private static final int UNIT10_FU = 10;  //副
	private static final int UNIT11_GE = 11;  //个
	private static final int UNIT12_KAN = 12; //看
	private static final int UNIT13_ZHI = 13; //只
	private static final int UNIT14_ZHI2 = 14; //支
	private static final int UNIT15_XIANG = 15; //箱
	
	
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
			case UNIT1_KE:
				if(amount < 50){
					return "克";
				}else if(amount < 500){
					return "两";
				}else{
					return "斤";
				}
			case UNIT2_HE:
				return "盒";
			case UNIT3_KUAI:
				return "块";
			case UNIT4_DAI:
				return "袋";
			case UNIT5_BAO:
				return "包";
			case UNIT6_PIN:
				return "瓶";
			case UNIT7_LAN:
				return "篮";
			case UNIT8_FEN:
				return "份";
			case UNIT9_DUI:
				return "对";
			case UNIT10_FU:
				return "副";
			case UNIT11_GE:
				return "个";
			case UNIT12_KAN:
				return "看";
			case UNIT13_ZHI:
				return "只";
			case UNIT14_ZHI2:
				return "支";
			case UNIT15_XIANG:
				return "箱";
			}
			return "";
		}catch(Exception e){
			return "";
		}
	}

	/**
	 * 获取转换后的数量及单位
	 * @param amount
	 * @param unit
	 * @return
	 */
	public static String getSwitchedWeight(int amount, String unit){
		String amountStr = getSwitchWeightNum(amount, unit);
		return amountStr + getSwichedUnit(amount, unit);
	}
	
	/**
	 * 获取转换后的数量
	 * @param amount
	 * @param unit
	 * @return
	 */
	public static String getSwitchWeightNum(int amount, String unit){
		String amountStr = "";
		try{
			int unitId = Integer.parseInt(unit);
			if(unitId == UNIT1_KE){
				double amountDouble = 0.0;
				if(amount < 50){
					amountDouble = amount;
				}else if(amount < 500){
					amountDouble = (Math.round((float)amount / 5)) / 10.0;
				}else{
					amountDouble = (Math.round((float)amount / 5)) / 100.0;
				}
				amountStr = String.valueOf(amountDouble);
			}else{
				amountStr = String.valueOf(amount);
			}
			return amountStr;
		}catch(Exception e){
		}
		return amountStr;
	}
	
	/**
	 * 获取转换后的价格
	 * @param price
	 * @return
	 */
	public static double getSwitchedMoney(int price){
		return price / 100.00;
	}
	
	public static double getSwitchedMoney(float price){
		return price / 100.00;
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
	 * 获取递增量
	 * @param amount
	 * @param unit
	 * @return
	 */
	public static int getPlusAmount(int amount, String unit){
		if("斤".equals(unit)){
			return 50;
		}else if("两".equals(unit)){
			return 50;
		}else{
			return 1;
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
			if(UNIT1_KE == unitId){
				return getSwitchedMoney((float)amount * unitPrice / 500);
			}
			return getSwitchedMoney(amount * unitPrice);
		}catch(Exception e){
			return 0.0;
		}
	}
}
