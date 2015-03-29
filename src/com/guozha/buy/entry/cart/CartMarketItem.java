package com.guozha.buy.entry.cart;

import java.io.Serializable;

/**
 * 购物车中逛菜场买的菜
 * @author PeggyTong
 *
 */
public class CartMarketItem extends CartBaseItem implements Serializable{

	private static final long serialVersionUID = -9211655769099911580L;
	
	public CartMarketItem(){
		super();
		setItemType(CartItemType.Market);
	}

	public CartMarketItem(int cartId, String menuName, int amount, String unit,
			int unitPrice, int price, String status) {
		super(cartId, menuName, amount, unit, unitPrice, price, status, CartItemType.Market);
	}
}
