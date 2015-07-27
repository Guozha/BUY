package com.guozha.buy.entry.cart;

import java.io.Serializable;

/**
 * 购物车列表的基类
 * @author PeggyTong
 *
 */
public class CartBaseItem implements Serializable{
	
	private static final long serialVersionUID = -8805063937649000000L;
	
	public enum CartItemType{
		CookBook,
		Market,
		undefine
	}

	private int cartId;    		//明细id
	private String goodsName;  	//商品名称
	private int amount;			//份量
	private String unit;		//计量单位
	private int unitPrice;		//单价
	private int price;			//价格
	private String cartStatus;	//状态 0失效、1可用
	private int minAmount;		//最小份量
	private CartItemType itemType;
	
	public CartBaseItem(){ }
	
	public CartBaseItem(int cartId, String goodsName, int amount, String unit,
			int unitPrice, int price, String cartStatus, CartItemType itemType) {
		super();
		this.cartId = cartId;
		this.goodsName = goodsName;
		this.amount = amount;
		this.unit = unit;
		this.unitPrice = unitPrice;
		this.price = price;
		this.cartStatus = cartStatus;
	}
	
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCartStatus() {
		return cartStatus;
	}
	public void setCartStatus(String cartStatus) {
		this.cartStatus = cartStatus;
	}
	public int getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(int minAmount) {
		this.minAmount = minAmount;
	}
	public CartItemType getItemType() {
		return itemType;
	}
	public void setItemType(CartItemType itemType) {
		this.itemType = itemType;
	}
	
}
