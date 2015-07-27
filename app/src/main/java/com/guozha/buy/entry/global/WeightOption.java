package com.guozha.buy.entry.global;

import java.io.Serializable;

/**
 * 重量选择实体类
 * @author PeggyTong
 *
 */
public class WeightOption implements Serializable{

	private static final long serialVersionUID = -1620436640012247635L;

	private int goodsId;
	private int amount;
	private int defaultFlag; 	//是否是默认重量
	
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(int defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
}
