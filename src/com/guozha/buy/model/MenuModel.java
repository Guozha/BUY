package com.guozha.buy.model;

import java.util.List;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.found.FoundMenuPage;
import com.guozha.buy.entry.market.RelationRecipe;
import com.guozha.buy.entry.mpage.BestMenuPage;
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
		
		/**
		 * 11.1 查询精选列表结果
		 * @param bestMenuPage
		 */
		public void requestBestMenuPageResult(BestMenuPage bestMenuPage);
		
		/**
		 * 12.4 查询某分类下的菜谱
		 * @param foundMenuPage
		 */
		public void requestFoundMenuListResult(FoundMenuPage foundMenuPage);
	}
	
	public void requestMenusByGoods(final Context context, int goodsId){
		RequestParam paramPath = new RequestParam("menuplan/goodsMenuList")
		.setParams("goodsId", goodsId);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				List<RelationRecipe> relationRecipes = gson.fromJson(response, new TypeToken<List<RelationRecipe>>() { }.getType());
				mInterface.requestMenuByGoodsResult(relationRecipes);
			}
		});
	}
	
	/**
	 * 11.1 查询精选列表
	 * @param context
	 * @param pageNum
	 * @param pageSize
	 */
	public void requestBestMenuList(final Context context, int pageNum){
		RequestParam paramPath = new RequestParam("/v31/pick/list")
		.setParams("pageNum", pageNum);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					BestMenuPage bestMenuPage = gson.fromJson(response, new TypeToken<BestMenuPage>() { }.getType());
					mInterface.requestBestMenuPageResult(bestMenuPage);
				}
		});
	}
	
	/**
	 * 12.4 查看某分类下的菜谱
	 * @param context
	 * @param pageNum
	 * @param pageSize
	 * @param menuTypeId
	 */
	public void requestFoundMenuList(final Context context, int pageNum, int menuTypeId){
		RequestParam paramPath = new RequestParam("v31/found/menuType/listMenu")
		.setParams("pageNum", pageNum)
		.setParams("menuTypeId", menuTypeId);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				FoundMenuPage foundMenuPage = gson.fromJson(response, new TypeToken<FoundMenuPage>() { }.getType());
				mInterface.requestFoundMenuListResult(foundMenuPage);
			}
		});
	}
}
