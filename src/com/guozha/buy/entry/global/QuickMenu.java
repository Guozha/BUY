package com.guozha.buy.entry.global;

import java.io.Serializable;

/**
 * 快捷菜单
 * @author PeggyTong
 *
 */
public class QuickMenu implements Serializable{

	private static final long serialVersionUID = 2832681425267302158L;
	
	private int menuId;
	private String name;
	
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
