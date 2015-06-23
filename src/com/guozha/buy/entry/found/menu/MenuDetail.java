package com.guozha.buy.entry.found.menu;

import java.util.List;

public class MenuDetail {
	private int menuId;
	private String menuName;
	private String menuImg;
	private String menuDesc;
	private int cookieTime;
	private String cookieWay;
	private String calories;
	private String packingFlag;
	
	private List<MenuSeason> seasonings;
	private List<MenuStep> menuSteps;
	private List<MenuGoods> menuGoods;
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
	public String getMenuDesc() {
		return menuDesc;
	}
	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
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
	public String getCalories() {
		return calories;
	}
	public void setCalories(String calories) {
		this.calories = calories;
	}
	public String getPackingFlag() {
		return packingFlag;
	}
	public void setPackingFlag(String packingFlag) {
		this.packingFlag = packingFlag;
	}
	public List<MenuSeason> getSeasonings() {
		return seasonings;
	}
	public void setSeasonings(List<MenuSeason> seasonings) {
		this.seasonings = seasonings;
	}
	public List<MenuStep> getMenuSteps() {
		return menuSteps;
	}
	public void setMenuSteps(List<MenuStep> menuSteps) {
		this.menuSteps = menuSteps;
	}
	public List<MenuGoods> getMenuGoods() {
		return menuGoods;
	}
	public void setMenuGoods(List<MenuGoods> menuGoods) {
		this.menuGoods = menuGoods;
	}
}
