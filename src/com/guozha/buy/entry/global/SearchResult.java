package com.guozha.buy.entry.global;

import java.util.List;

import com.guozha.buy.entry.market.ItemSaleInfo;

/**
 * 查找结果
 * @author PeggyTong
 *
 */
public class SearchResult {

	private List<ItemSaleInfo> goodsList;		
	private List<SearchRecipe> menuList;
	
	public List<ItemSaleInfo> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<ItemSaleInfo> goodsList) {
		this.goodsList = goodsList;
	}
	public List<SearchRecipe> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<SearchRecipe> menuList) {
		this.menuList = menuList;
	}
}
