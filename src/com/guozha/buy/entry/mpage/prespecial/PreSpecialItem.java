package com.guozha.buy.entry.mpage.prespecial;

import java.io.Serializable;

/**
 * 特供预售
 * @author PeggyTong
 *
 */
public class PreSpecialItem implements Serializable{

	private static final long serialVersionUID = -1490293047751990947L;
	
	private int goodsId;
	private String goodsName;
	private int unitPrice;
	private String unit;
	private String goodsImg;
	private String goodsProp;
	private int buyTimes;
	
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
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public String getGoodsProp() {
		return goodsProp;
	}
	public void setGoodsProp(String goodsProp) {
		this.goodsProp = goodsProp;
	}
	public int getBuyTimes() {
		return buyTimes;
	}
	public void setBuyTimes(int buyTimes) {
		this.buyTimes = buyTimes;
	}
}
