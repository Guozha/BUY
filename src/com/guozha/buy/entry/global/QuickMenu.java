package com.guozha.buy.entry.global;

import java.io.Serializable;

/**
 * 快捷菜单
 * @author PeggyTong
 *
 */
public class QuickMenu implements Serializable{

	private static final long serialVersionUID = -3878306234523432266L;
	
	private int menuId;
	private String name;
	
	public QuickMenu(){ }
	
	public QuickMenu(int menuId, String name){
		this.menuId = menuId;
		this.name = name;
	}
	
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
