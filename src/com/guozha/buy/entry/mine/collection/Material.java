package com.guozha.buy.entry.mine.collection;

import java.io.Serializable;

/**
 * 菜谱所需材料
 * @author PeggyTong
 *
 */
public class Material implements Serializable{
	
	private static final long serialVersionUID = -5873142847798404141L;
	
	private String goodsName;	//食材名称
	private int amount;			//份量
	private String unit;		//单位
	
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
