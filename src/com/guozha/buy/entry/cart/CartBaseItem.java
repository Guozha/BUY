package com.guozha.buy.entry.cart;

/**
 * 购物车列表的基类
 * @author PeggyTong
 *
 */
public class CartBaseItem {
	
	public enum CartItemType{
		CookBook,
		Market,
		undefine
	}

	private String itemId;
	private String itemName;  //材料名称
	private int itemCount;     //重量
	private String itemUnit; //质量单位
	private String itemPrice; //价格
	private CartItemType itemType;
	
	
	
	public CartBaseItem(String itemId, String itemName, int itemCount,
			String itemUnit, String itemPrice, CartItemType itemType) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCount = itemCount;
		this.itemUnit = itemUnit;
		this.itemPrice = itemPrice;
		this.itemType = itemType;
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public String getItemUnit() {
		return itemUnit;
	}
	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public CartItemType getItemType() {
		return itemType;
	}

	public void setItemType(CartItemType itemType) {
		this.itemType = itemType;
	}
	
}
