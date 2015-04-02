package com.guozha.buy.entry.mpage.plan;

import java.io.Serializable;

/**
 * 菜谱对应菜品
 * @author PeggyTong
 *
 */
public class MenuGoods implements Serializable{

	private static final long serialVersionUID = 5083607567680770001L;
	
	private int goodsId;
	private String goodsName;
	private int amount;
	private String unit;
	
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
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
