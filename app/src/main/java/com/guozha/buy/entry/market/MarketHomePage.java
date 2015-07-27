package com.guozha.buy.entry.market;

import java.io.Serializable;
import java.util.List;

/**
 * 逛菜场首页的分页
 * @author PeggyTong
 *
 */
public class MarketHomePage implements Serializable{
	
	private static final long serialVersionUID = 4753855610129544781L;
	
	private Integer totalCount;
	private Integer pageSize;
	private Integer pageNum;
	private Integer pageCount;
	private List<MarketHomeItem> frontTypeList;
	
	public List<MarketHomeItem> getFrontTypeList() {
		return frontTypeList;
	}
	public void setFrontTypeList(List<MarketHomeItem> frontTypeList) {
		this.frontTypeList = frontTypeList;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
}
