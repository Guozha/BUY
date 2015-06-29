package com.guozha.buy.entry.found;

import java.util.List;

public class SubjectDetail {
	private String itemName;
	private String itemDesc;
	private String productType;
	private int goodsOrMenuId;
	private String unit;
	private int unitPrice;
	
	private List<SubjectDetailItem> itemDetailList;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public int getGoodsOrMenuId() {
		return goodsOrMenuId;
	}
	public void setGoodsOrMenuId(int goodsOrMenuId) {
		this.goodsOrMenuId = goodsOrMenuId;
	}
	public List<SubjectDetailItem> getItemDetailList() {
		return itemDetailList;
	}
	public void setItemDetailList(List<SubjectDetailItem> itemDetailList) {
		this.itemDetailList = itemDetailList;
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
}
