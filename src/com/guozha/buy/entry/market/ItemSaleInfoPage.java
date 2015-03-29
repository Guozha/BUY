package com.guozha.buy.entry.market;

import java.io.Serializable;
import java.util.List;

/**
 * 商品列表分页
 * @author PeggyTong
 *
 */
public class ItemSaleInfoPage implements Serializable{
	
	private static final long serialVersionUID = 1568621471215131391L;
	
	private int pageCount;
	private List<ItemSaleInfo> goodsList;
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public List<ItemSaleInfo> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<ItemSaleInfo> goodsList) {
		this.goodsList = goodsList;
	}
	
}
