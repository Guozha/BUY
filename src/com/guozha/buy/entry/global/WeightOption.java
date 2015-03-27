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
	private int unitPrice;
	private int price;
	private String unit;
	
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
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}
