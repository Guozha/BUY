package com.guozha.buy.entry.cart;

import java.util.List;


/**
 * 购物车菜谱列表
 * @author PeggyTong
 *
 */
public class CartCookItem extends CartBaseItem{

	private List<CartCookMaterial> cookMaterials; //菜谱的材料
	
	public CartCookItem(String itemId, String itemName, int itemCount,
			String itemUnit, String itemPrice, List<CartCookMaterial> cookMaterials) {
		super(itemId, itemName, itemCount, itemUnit, itemPrice, CartBaseItem.CartItemType.CookBook);
		this.cookMaterials = cookMaterials;
	}

	public List<CartCookMaterial> getCookMaterials() {
		return cookMaterials;
	}

	public void setCookMaterials(List<CartCookMaterial> cookMaterials) {
		this.cookMaterials = cookMaterials;
	}
}


