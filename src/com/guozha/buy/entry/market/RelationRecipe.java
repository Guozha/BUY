package com.guozha.buy.entry.market;

import java.io.Serializable;
import java.util.List;

/**
 * 菜品关联菜谱
 * @author PeggyTong
 *
 */
public class RelationRecipe implements Serializable{

	private static final long serialVersionUID = 5308801119361779530L;
	
	private int menuId;
	private String menuName;
	private String menuImg;
	private int cookieTime;
	private String cookieWay;
	private String hardType;
	private List<RelationRecipeMaterial> goodsList;
	
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
	public int getCookieTime() {
		return cookieTime;
	}
	public void setCookieTime(int cookieTime) {
		this.cookieTime = cookieTime;
	}
	public String getCookieWay() {
		return cookieWay;
	}
	public void setCookieWay(String cookieWay) {
		this.cookieWay = cookieWay;
	}
	public String getHardType() {
		return hardType;
	}
	public void setHardType(String hardType) {
		this.hardType = hardType;
	}
	public List<RelationRecipeMaterial> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<RelationRecipeMaterial> goodsList) {
		this.goodsList = goodsList;
	}
}
