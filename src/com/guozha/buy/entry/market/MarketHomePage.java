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
	
	private int pageCount;
	private List<MarketHomeItem> frontTypeList;
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public List<MarketHomeItem> getFrontTypeList() {
		return frontTypeList;
	}
	public void setFrontTypeList(List<MarketHomeItem> frontTypeList) {
		this.frontTypeList = frontTypeList;
	}
}
