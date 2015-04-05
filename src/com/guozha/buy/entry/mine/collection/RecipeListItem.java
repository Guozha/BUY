package com.guozha.buy.entry.mine.collection;

import java.io.Serializable;
import java.util.List;

/**
 * 菜谱收藏
 * @author PeggyTong
 *
 */
public class RecipeListItem implements Serializable{
	
	private static final long serialVersionUID = 5069237596455086691L;
	
	private int myMenuId;
	private int myDirId;
	private int menuId; 
	private String menuName;
	private String menuImg;
	private String activeFlag;
	private List<Material> goodsList;
	
	public int getMyMenuId() {
		return myMenuId;
	}
	public void setMyMenuId(int myMenuId) {
		this.myMenuId = myMenuId;
	}
	public int getMyDirId() {
		return myDirId;
	}
	public void setMyDirId(int myDirId) {
		this.myDirId = myDirId;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuImg() {
		return menuImg;
	}
	public void setMenuImg(String menuImg) {
		this.menuImg = menuImg;
	}
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	public List<Material> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<Material> goodsList) {
		this.goodsList = goodsList;
	}
}
