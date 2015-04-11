package com.guozha.buy.entry.mine.order;

import java.util.List;

public class ExpandListData {
	
	private int id;
	private String type;
	private String name;
	private String amount;
	private String price;
	private List<OrderDetailGoods> menuslist;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public List<OrderDetailGoods> getMenuslist() {
		return menuslist;
	}
	public void setMenuslist(List<OrderDetailGoods> menuslist) {
		this.menuslist = menuslist;
	}
}
