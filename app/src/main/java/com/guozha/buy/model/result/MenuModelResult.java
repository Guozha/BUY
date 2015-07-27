package com.guozha.buy.model.result;

import java.util.List;

import com.guozha.buy.entry.found.FoundMenuPage;
import com.guozha.buy.entry.found.menu.MenuDetail;
import com.guozha.buy.entry.market.RelationRecipe;
import com.guozha.buy.entry.mpage.BestMenuPage;
import com.guozha.buy.model.MenuModel.MenuModelInterface;

public class MenuModelResult implements MenuModelInterface{

	@Override
	public void requestMenuByGoodsResult(List<RelationRecipe> relationRecipes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestBestMenuPageResult(BestMenuPage bestMenuPage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestFoundMenuListResult(FoundMenuPage foundMenuPage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestMenuDetailResult(MenuDetail menuDetail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestCollectionMenuResult(String returnCode, String msg) {
		// TODO Auto-generated method stub
		
	}

}
