package com.guozha.buy.model;

import com.guozha.buy.model.result.MenuModelResult;

public class MenuModel extends BaseModel{
	
	private MenuModelInterface mInterface;
	
	public MenuModel(){
		mInterface = new MenuModelResult();
	}
	
	public MenuModel(MenuModelInterface menuModeInterface){
		mInterface = menuModeInterface;
	}
	
	public interface MenuModelInterface{
		
	}

}
