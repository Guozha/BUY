package com.guozha.buy.entry.mine.order;

import java.io.Serializable;
import java.util.List;

public class OrderDetailMenus implements Serializable{

	private static final long serialVersionUID = -7193240972787243064L;
	
	private int menuId;
	private String menuName;
	private String menuImg;
	private int unitPrice;
	private int amount;
	private int price;
	private List<OrderDetailGoods> goodsInfoList;
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuImg() {
		return menuImg;
	}
	public void setMenuImg(String menuImg) {
		this.menuImg = menuImg;
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
	public List<OrderDetailGoods> getGoodsInfoList() {
		return goodsInfoList;
	}
	public void setGoodsInfoList(List<OrderDetailGoods> goodsInfoList) {
		this.goodsInfoList = goodsInfoList;
	}
}
