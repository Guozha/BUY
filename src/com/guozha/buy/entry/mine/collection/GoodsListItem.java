package com.guozha.buy.entry.mine.collection;

import java.io.Serializable;

/**
 * 食材收藏
 * @author PeggyTong
 *
 */
public class GoodsListItem implements Serializable{

	private static final long serialVersionUID = -1739019850950406892L;
	
	private int myGoodsId; 	//收藏食材id
	private int goodsId;		//食材id
	private String goodsName;	//食材名称
	private String goodsImg;	//食材图片
	private int unitPrice;			//价格
	private String unit;		//单位
	private String activeFlag;	//激活标识
	
	public int getMyGoodsId() {
		return myGoodsId;
	}
	public void setMyGoodsId(int myGoodsId) {
		this.myGoodsId = myGoodsId;
	}
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
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
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
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
}
