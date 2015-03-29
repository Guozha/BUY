package com.guozha.buy.entry.cart;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车
 * @author PeggyTong
 *
 */
public class CartTotalData implements Serializable{
	
	private static final long serialVersionUID = -8960765511016236411L;
	
	private int quantity;
	private int totalPrice;
	private int serviceFee;
	private int currServiceFee;
	private List<CartCookItem> menuList;
	private List<CartMarketItem> goodsList;
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(int serviceFee) {
		this.serviceFee = serviceFee;
	}
	public int getCurrServiceFee() {
		return currServiceFee;
	}
	public void setCurrServiceFee(int currServiceFee) {
		this.currServiceFee = currServiceFee;
	}
	public List<CartCookItem> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<CartCookItem> menuList) {
		this.menuList = menuList;
	}
	public List<CartMarketItem> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<CartMarketItem> goodsList) {
		this.goodsList = goodsList;
	}
}
