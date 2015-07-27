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
	
	private int pageCount;					//总的页数
	private int totalCount;					//总条数
	private int pageNum;					//第几页
	private int pageSize;					//一页多少条
	
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
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
