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
	
	private int quantity;				//总商品个数
	private int totalPrice;				//总价格
	private int serviceFee;				//系统配置的服务费
	private int currServiceFee;			//当前服务费
	private int serviceFeePrice;		//免服务费额
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
	public int getServiceFeePrice() {
		return serviceFeePrice;
	}
	public void setServiceFeePrice(int serviceFeePrice) {
		this.serviceFeePrice = serviceFeePrice;
	}
}
