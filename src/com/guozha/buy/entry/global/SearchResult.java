package com.guozha.buy.entry.global;

import java.util.List;

import com.guozha.buy.entry.market.ItemSaleInfo;
import com.guozha.buy.entry.mine.collection.RecipeListItem;

/**
 * 查找结果
 * @author PeggyTong
 *
 */
public class SearchResult {

	private List<ItemSaleInfo> goodsList;		
	private List<RecipeListItem> menuList;
	
	public List<ItemSaleInfo> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<ItemSaleInfo> goodsList) {
		this.goodsList = goodsList;
	}
	public List<RecipeListItem> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<RecipeListItem> menuList) {
		this.menuList = menuList;
	}
}
