package com.guozha.buy.entry;

/**
 * 快捷菜单
 * @author PeggyTong
 *
 */
public class QuickMenus {

	private String id;
	private String showName;
	private String categoryId; //分类ID
	private boolean choosed;

	public QuickMenus(String id, String showName, String categoryId,
			boolean choosed) {
		super();
		this.id = id;
		this.showName = showName;
		this.categoryId = categoryId;
		this.choosed = choosed;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getShowName() {
		return showName;
	}
	
	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	public boolean isChoosed() {
		return choosed;
	}
	
	public void setChoosed(boolean choosed) {
		this.choosed = choosed;
	}
	
}
