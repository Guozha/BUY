package com.guozha.buy.entry.found;

import java.util.List;

public class MenuFirstType {
	private int menuTypeId;
	private String menuTypeName;
	private List<MenuSecondType> menuTypeList;
	public int getMenuTypeId() {
		return menuTypeId;
	}
	public void setMenuTypeId(int menuTypeId) {
		this.menuTypeId = menuTypeId;
	}
	public String getMenuTypeName() {
		return menuTypeName;
	}
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
	public List<MenuSecondType> getMenuTypeList() {
		return menuTypeList;
	}
	public void setMenuTypeList(List<MenuSecondType> menuTypeList) {
		this.menuTypeList = menuTypeList;
	}
	
}
