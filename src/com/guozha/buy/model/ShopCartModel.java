package com.guozha.buy.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.cart.CartTotalData;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.ShopCartModelResult;

public class ShopCartModel extends BaseModel{
	
	private ShopCartModelInterface mInterface;
	
	public ShopCartModel(){
		mInterface = new ShopCartModelResult();
	}
	
	public ShopCartModel(ShopCartModelInterface shopCartModelInterface){
		mInterface = shopCartModelInterface;
	}
	
	public interface ShopCartModelInterface{
		
		/**
		 * 3.1 请求购物车数据结果
		 * @param cartTotalData
		 */
		public void requestListCartItemResult(CartTotalData cartTotalData);
		
		/**
		 * 3.2 请求添加购物车结果
		 * @param returnCode
		 * @param msg
		 */
		public void requestAddCartResult(String returnCode, String msg);
		
		/**
		 * 3.3 请求修改购物车的结果
		 * @param returnCode
		 * @param msg
		 */
		public void requestUpdateCartResult(String returnCode, String msg);
		
		/**
		 * 3.4 请求删除购物车的结果
		 * @param returnCode
		 * @param msg
		 */
		public void requestDeleteCartResult(String returnCode, String msg);
		
		/**
		 * 3.6 菜谱添加购物车
		 * @param returnCode
		 * @param msg
		 */
		public void requestCartAddMenuResult(String returnCode, String msg);
	}
	
	/**
	 * 3.1 请求购物车数据
	 * @param handler
	 */
	public void requestListCartItem(final Context context, int userId, int addressId){
		RequestParam paramPath = new RequestParam("v31/cart/list")
		.setParams("userId", userId)
		.setParams("addressId", addressId == -1 ? "" : String.valueOf(addressId));
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				CartTotalData cartTotalData = gson.fromJson(response, new TypeToken<CartTotalData>() { }.getType());
				mInterface.requestListCartItemResult(cartTotalData);
			}
		});
	}
	
	/**
	 * 3. 7 食材添加到购物车
	 * @param context
	 * @param userId
	 * @param goodsId
	 * @param productType
	 * @param quantity
	 * @param token
	 * @param addressId
	 */
	public void requestAddCart(final Context context, int userId,
			int goodsId, int quantity, String token, int addressId) {
		RequestParam paramPath = new RequestParam("v31/cart/insertForGoods")
		.setParams("userId", userId)
		.setParams("goodsId", goodsId)
		.setParams("amount", quantity)
		.setParams("token", token)
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String returnCode = response.getString("returnCode");
					String msg = response.getString("msg");
					mInterface.requestAddCartResult(returnCode, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void requestCartAddMenu(final Context context, 
			int userId, int menuId, String token, int addressId, List<String> goodsIds){
		requestCartAddMenu(context, userId, menuId, token, addressId, goodsIds, 1);
	}
	
	public void requestCartAddMenu(final Context context, 
			int userId, int menuId, String token, int addressId){
		requestCartAddMenu(context, userId, menuId, token, addressId, null, 0);
	}
	
	/**
	 * 3.6 菜谱添加购物车
	 * @param context
	 * @param userId
	 * @param menuId
	 * @param token
	 * @param addressId
	 * @param goodsId
	 */
	public void requestCartAddMenu(final Context context, 
			int userId, int menuId, String token, int addressId, List<String> goodsIds, int goodsFlag){
		RequestParam paramPath = new RequestParam("v31/cart/insertForMenu")
		.setParams("userId", userId)
		.setParams("menuId", menuId)
		.setParams("token", token)
		.setParams("addressId", addressId)
		.setParams("goodsFlag", goodsFlag);
		if(goodsFlag != 0){
			paramPath.setParams("goodsId", goodsIds);
		}
		HttpManager.getInstance(context).volleyRequestByPost(paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String returnCode = response.getString("returnCode");
					String msg = response.getString("msg");
					mInterface.requestCartAddMenuResult(returnCode, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 3.3 请求更改购物车
	 * @param context
	 * @param cartId
	 * @param amount
	 * @param token
	 * @param userId
	 */
	public void requestUpdateCart(final Context context, int cartId, int amount, String token, int userId){
		RequestParam paramPath = new RequestParam("cart/update")
		.setParams("cartId", cartId)
		.setParams("userId", userId)
		.setParams("token", token)
		.setParams("amount", amount);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestUpdateCartResult(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * 3.4 删除购物车
	 * @param context
	 * @param cartId
	 * @param userId
	 * @param token
	 */
	public void requestDeleteCart(final Context context, int cartId, int userId, String token){
		RequestParam paramPath = new RequestParam("cart/delete")
		.setParams("cartId", cartId)
		.setParams("userId", userId)
		.setParams("token", token);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String returnCode = response.getString("returnCode");
					String msg = response.getString("msg");
					mInterface.requestDeleteCartResult(returnCode, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
