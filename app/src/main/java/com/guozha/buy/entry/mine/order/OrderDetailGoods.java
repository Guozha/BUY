package com.guozha.buy.entry.mine.order;

import java.io.Serializable;

public class OrderDetailGoods implements Serializable{
	
	private static final long serialVersionUID = 2222652503075071599L;
	
	private String goodsFromType;
	private int id;
	private int goodsId;
	private String goodsName;
	private String goodsImage;
	private String unit;
	private int unitPrice;
	private int amount;
	private int price;
	private String goodsStar;
	public String getGoodsFromType() {
		return goodsFromType;
	}
	public void setGoodsFromType(String goodsFromType) {
		this.goodsFromType = goodsFromType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getGoodsStar() {
		return goodsStar;
	}
	public void setGoodsStar(String goodsStar) {
		this.goodsStar = goodsStar;
	}
	
	
}
