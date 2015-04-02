package com.guozha.buy.entry.mpage.plan;

import java.io.Serializable;
import java.util.List;

/**
 * 菜谱详情
 * @author guozha
 *
 */
public class CookBookDetail implements Serializable{

	private static final long serialVersionUID = -6756092658136985759L;
	
	private int menuId;
	private String menuName;
	private String menuImg;
	private String menuDesc;
	private int cookieTime;
	private String cookieWay;
	private String hardType;
	private List<Seasoning> seasonings;
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
	public String getHardType() {
		return hardType;
	}
	public void setHardType(String hardType) {
		this.hardType = hardType;
	}
	public List<Seasoning> getSeasonings() {
		return seasonings;
	}
	public void setSeasonings(List<Seasoning> seasonings) {
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
