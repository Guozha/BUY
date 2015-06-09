package com.guozha.buy.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.mine.collection.CollectionDir;
import com.guozha.buy.entry.mine.collection.GoodsListItem;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.CollectionModelResult;

public class CollectionModel {
	
	private CollectionModelInterface mInterface;
	
	public CollectionModel(){
		mInterface = new CollectionModelResult();
	}
	
	public CollectionModel(CollectionModelInterface collectionModelInterface){
		mInterface = collectionModelInterface;
	}
	
	public interface CollectionModelInterface{
		
		/**
		 * 1.11.1 请求收藏食材
		 * @param returnCode
		 * @param msg
		 */
		public void requestCollectionGoodsResult(String returnCode, String msg);
		
		/**
		 * 1.11.3 查询食材收藏
		 * @param goodsListItem
		 */
		public void requestGoodsCollectList(List<GoodsListItem> goodsListItem);
		
		/**
		 * 1.11.4 查询菜谱收藏
		 * @param collectionDir
		 */
		public void requestMenuCollectList(List<CollectionDir> collectionDir);
		
		/**
		 * 1.11.5 添加收藏文件夹
		 * @param returnCode
		 * @param msg
		 */
		public void requestAddCollectDirResult(String returnCode, String msg);
		
		/**
		 * 1.11.6 请求更改收藏夹
		 * @param returnCode
		 * @param msg
		 */
		public void requestModifyCollectFolder(String returnCode, String msg);
		
		/**
		 * 1.11.7 请求删除文件夹
		 * @param returnCode
		 * @param msg
		 */
		public void requestDeleCollectDirResult(String returnCode, String msg);
		
		/**
		 * 1.11.8 删除菜谱收藏
		 * @param returnode
		 * @param msg
		 */
		public void requestDeleMenuCollectItemResult(String returnCode, String msg);
		
		/**
		 * 1.11.9 删除食材收藏
		 * @param returnCode
		 * @param msg
		 */
		public void requestDeletGoodsCollectResult(String returnCode, String msg);
		
	}
	

	/**
	 * 1.11.1 请求收藏食材
	 */
	public void requestCollectionGoods(final Context context, String token, int userId, int goodsId) {
		RequestParam paramPath = new RequestParam("account/myfavo/insertGoodsFavo")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("goodsId", goodsId);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestCollectionGoodsResult(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
		});
	}

	/**
	 * 1.11.3 查询食材收藏
	 */
	public void requestGoodsCollectList(final Context context, int userId, int addressId){
		RequestParam paramPath = new RequestParam("account/myfavo/listGoodsFavo")
		.setParams("userId", userId)
		.setParams("addressId", addressId);
		
		HttpManager.getInstance(context).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<GoodsListItem> goodsListItem = gson.fromJson(response, new TypeToken<List<GoodsListItem>>() { }.getType());
					mInterface.requestGoodsCollectList(goodsListItem);
				}
			});
	}
	
	/**
	 * 1.11.4 查询菜谱收藏
	 * @param context
	 * @param userId
	 * @param addressId
	 */
	public void requestMenuCollectList(final Context context, int userId, int addressId){
		RequestParam paramPath = new RequestParam("account/myfavo/listMenuFavo")
		.setParams("userId", userId)
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<CollectionDir> collectionDir = gson.fromJson(response, new TypeToken<List<CollectionDir>>() { }.getType());
					mInterface.requestMenuCollectList(collectionDir);
				}
			});
	}
	
	/**
	 * 1.11.5 添加文件夹
	 * @param context
	 * @param userId
	 * @param token
	 * @param folderName
	 */
	public void requestAddCollectDir(final Context context, int userId, String token, String folderName){
		RequestParam paramPath = new RequestParam("account/myfavo/insertDir")
		.setParams("userId", userId)
		.setParams("token", token)
		.setParams("dirName", folderName);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode");
					String msg = response.getString("msg");
					mInterface.requestAddCollectDirResult(returnCode, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 1.11.6 调整菜谱收藏
	 * @param context
	 * @param token
	 * @param menuId
	 * @param dirId
	 */
	public void requestModifyCollectFolder(final Context context, String token, int menuId,int dirId){
		RequestParam paramPath = new RequestParam("account/myfavo/adjustMenuFavo")
		.setParams("token", token)
		.setParams("myMenuId", menuId)
		.setParams("myDirId", dirId);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestModifyCollectFolder(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * 1.11.7 请求删除收藏夹
	 * @param context
	 * @param token
	 * @param dirId
	 */
	public void requestDeleCollectDir(final Context context, String token, int dirId){
		RequestParam paramPath = new RequestParam("account/myfavo/deleteMyDir")
		.setParams("token", token)
		.setParams("myDirId", dirId);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestDeleCollectDirResult(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * 请求删除收藏菜谱
	 */
	public void requestDeleMenuCollectItem(final Context context, String token, int menuId){
		RequestParam paramPath = new RequestParam("account/myfavo/deleteMyMenu")
		.setParams("token", token)
		.setParams("myMenuId", menuId);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestDeleMenuCollectItemResult(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
			});
	}
	
	/**
	 * 请求删除菜品
	 * @param goodsId
	 */
	public void requestDeletGoodsCollect(final Context context, String token, int goodsId) {
		RequestParam paramPath = new RequestParam("account/myfavo/deleteMyGoods")
		.setParams("token", token)
		.setParams("myGoodsId", goodsId);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestDeletGoodsCollectResult(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
}
