package com.guozha.buy.entry.cart;

import java.io.Serializable;

/**
 * 菜谱的材料
 * @author PeggyTong
 *
 */
public class CartCookMaterial implements Serializable{

	private static final long serialVersionUID = 2164297757732551580L;
	
	private String goodsName;  //材料名称
	private int amount;  //材料数量
	private String unit;   //材料单位
	
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
