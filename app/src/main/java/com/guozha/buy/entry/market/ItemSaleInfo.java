package com.guozha.buy.entry.market;

import java.io.Serializable;

//单个物品的属性类
public class ItemSaleInfo implements Serializable{

	private static final long serialVersionUID = 6331248942615481326L;
	
	private int goodsId;		//商品ID
	private String goodsName;	//商品名称
	private int unitPrice;		//单价
	private String unit;		//计量单位
	private String goodsImg;	//商品图片
	private String goodsProp;	//商品性质
	private String bargainFlag;  //0不是特惠， 1是特惠
	private int bargainUnitPrice; //特惠单价
	
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
	public String getBargainFlag() {
		return bargainFlag;
	}
	public void setBargainFlag(String bargainFlag) {
		this.bargainFlag = bargainFlag;
	}
	public int getBargainUnitPrice() {
		return bargainUnitPrice;
	}
	public void setBargainUnitPrice(int bargainUnitPrice) {
		this.bargainUnitPrice = bargainUnitPrice;
	}
}
