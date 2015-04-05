package com.guozha.buy.entry.mpage.prespecial;

import java.io.Serializable;
import java.util.List;

/**
 * 特供预售分页
 * @author PeggyTong
 *
 */
public class PreSpecialPage implements Serializable{

	private static final long serialVersionUID = 1458916535965350045L;
	
	private int pageCount;		//总页数
	private int totalCount;		//总条数
	private int pageNum;		//第几页
	private int pageSize;		//一页多少页
	private List<PreSpecialItem> goodsList;  //特供列表
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
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
	public List<PreSpecialItem> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<PreSpecialItem> goodsList) {
		this.goodsList = goodsList;
	}
}
