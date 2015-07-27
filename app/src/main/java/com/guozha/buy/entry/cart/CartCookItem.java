package com.guozha.buy.entry.cart;

import java.io.Serializable;
import java.util.List;


/**
 * 购物车菜谱列表
 * @author PeggyTong
 *
 */
public class CartCookItem extends CartBaseItem implements Serializable{

	private static final long serialVersionUID = 298821125292832999L;
	
	private List<CartCookMaterial> goodsList; //菜谱的材料
	
	public CartCookItem(){
		super();
		setItemType(CartItemType.CookBook);
	}
	
	public CartCookItem(int cartId, String menuName, int amount, String unit,
			int unitPrice, int price, String status) {
		super(cartId, menuName, amount, unit, unitPrice, price, status, CartItemType.CookBook);
	}

	public List<CartCookMaterial> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<CartCookMaterial> goodsList) {
		this.goodsList = goodsList;
	}
}


