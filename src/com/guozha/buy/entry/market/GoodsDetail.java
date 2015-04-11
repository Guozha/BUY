package com.guozha.buy.entry.market;

import java.io.Serializable;

/**
 * 产品明细
 * @author PeggyTong
 *
 */
public class GoodsDetail implements Serializable{

	private static final long serialVersionUID = -1396745727584343677L;
	
	private int goodsId;
	private String goodsName;
	private int unitPrice;
	private String unit;
	private String goodsImg;
	private String memo;	
	private String prepareEndDate;
	private String goodsProp;
	private int arrivalDays;
	
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPrepareEndDate() {
		return prepareEndDate;
	}
	public void setPrepareEndDate(String prepareEndDate) {
		this.prepareEndDate = prepareEndDate;
	}
	public String getGoodsProp() {
		return goodsProp;
	}
	public void setGoodsProp(String goodsProp) {
		this.goodsProp = goodsProp;
	}
	public int getArrivalDays() {
		return arrivalDays;
	}
	public void setArrivalDays(int arrivalDays) {
		this.arrivalDays = arrivalDays;
	}
	
	
}
