package com.guozha.buy.entry.mpage;

import java.util.List;

public class BestMenuPage {

	private int totalCount; //总记录数
	private int pageCount;  //总页数
	
	private List<BestMenuItem> menuPickList;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<BestMenuItem> getMenuPickList() {
		return menuPickList;
	}

	public void setMenuPickList(List<BestMenuItem> menuPickList) {
		this.menuPickList = menuPickList;
	}
	
	
}
