package com.guozha.buy.entry.mpage;

import java.io.Serializable;

public class BestMenuItem implements Serializable{

	private static final long serialVersionUID = -6124523452187139364L;
	private int menuId;
	private String menuName;
	private String menuImg;
	private String pickImg;
	private int bgColor;
	private int fontColor;
	private String pickDesc;
	
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
	public String getPickImg() {
		return pickImg;
	}
	public void setPickImg(String pickImg) {
		this.pickImg = pickImg;
	}
	public int getBgColor() {
		return bgColor;
	}
	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}
	public int getFontColor() {
		return fontColor;
	}
	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}
	public String getPickDesc() {
		return pickDesc;
	}
	public void setPickDesc(String pickDesc) {
		this.pickDesc = pickDesc;
	}
}
