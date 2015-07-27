package com.guozha.buy.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.found.FoundMenuPage;
import com.guozha.buy.entry.found.menu.MenuDetail;
import com.guozha.buy.entry.market.RelationRecipe;
import com.guozha.buy.entry.mpage.BestMenuPage;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.MenuModelResult;
import com.guozha.buy.util.LogUtil;

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
		
		/**
		 * 8.6 菜谱详情
		 * @param menuDetail
		 */
		public void requestMenuDetailResult(MenuDetail menuDetail);
		
		/**
		 * 1.11.2 菜谱收藏
		 * @param returnCode
		 * @param msg
		 */
		public void requestCollectionMenuResult(String returnCode, String msg);
	}
	
	public void requestMenusByGoods(final Context context, int goodsId){
		RequestParam paramPath = new RequestParam("v31/menu/goodsMenuList")
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
	public void requestBestMenuList(final Context context, int addressId, int pageNum){
		RequestParam paramPath = new RequestParam("v31/pick/list")
		.setParams("addressId", addressId)
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
	
	/**
	 * 8.6 请求菜谱详情
	 * @param context
	 * @param menuId
	 */
	public void requestMenuDetail(final Context context, int menuId, int addressId){
		RequestParam paramPath = new RequestParam("v31/menu/detail")
		.setParams("addressId", addressId)
		.setParams("menuId", menuId);
		HttpManager.getInstance(context).volleyRequestByPost(paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				MenuDetail menuDetail = gson.fromJson(response, new TypeToken<MenuDetail>() { }.getType());
				mInterface.requestMenuDetailResult(menuDetail);
			}
		});
	}
	
	/**
	 * 1.11.2 请求收藏菜谱
	 */
	public void requestCollectionMenu(final Context context, String token, int userId, int menuId){
		RequestParam paramPath = new RequestParam("account/myfavo/insertMenuFavo")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("menuIds", menuId);
		HttpManager.getInstance(context).volleyRequestByPost(paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String returnCode = response
							.getString("returnCode");
					String msg = response.getString("msg");
					mInterface.requestCollectionMenuResult(returnCode, msg);
				} catch (JSONException e) {
					jsonException(context);
					e.printStackTrace();
				}
			}
		});
	}
}
