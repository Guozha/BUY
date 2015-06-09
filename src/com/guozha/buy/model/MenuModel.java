package com.guozha.buy.model;

import java.util.List;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.market.RelationRecipe;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
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
		
		public void requestMenuByGoodsResult(List<RelationRecipe> relationRecipes);
	}
	
	public void requestMenusByGoods(final Context context, int goodsId){
		RequestParam paramPath = new RequestParam("menuplan/goodsMenuList")
		.setParams("goodsId", goodsId);
		HttpManager.getInstance(context).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				List<RelationRecipe> relationRecipes = gson.fromJson(response, new TypeToken<List<RelationRecipe>>() { }.getType());
				mInterface.requestMenuByGoodsResult(relationRecipes);
			}
		});
	}

}
