package com.guozha.buy.entry.cart;

/**
 * 购物车中逛菜场买的菜
 * @author PeggyTong
 *
 */
public class CartMarketItem extends CartBaseItem{

	public CartMarketItem(String itemId, String itemName, int itemCount,
			String itemUnit, String itemPrice) {
		super(itemId, itemName, itemCount, itemUnit, itemPrice, CartBaseItem.CartItemType.Market);
	}
	
}
