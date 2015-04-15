package com.guozha.buy.entry.mine.order;

import java.util.List;

public class ExpandListData {
	
	private int id;
	private int type;	//类型 0代表菜谱 1代表菜品
	private String name;	//商品名称
	private int amount;	//数量
	private int price;	//价格
	private String unit;	//单位
	private List<OrderDetailGoods> menuslist;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public List<OrderDetailGoods> getMenuslist() {
		return menuslist;
	}
	public void setMenuslist(List<OrderDetailGoods> menuslist) {
		this.menuslist = menuslist;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
