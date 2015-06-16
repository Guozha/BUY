package com.guozha.buy.entry.found;

import java.util.List;

public class FoundMenuPage {

	private int totalCount;
	private int pageCount;

	private List<FoundMenu> menuList;

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

	public List<FoundMenu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<FoundMenu> menuList) {
		this.menuList = menuList;
	}
}
