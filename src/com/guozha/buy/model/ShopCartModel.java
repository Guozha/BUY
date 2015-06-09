package com.guozha.buy.model;

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
	}
	
	/**
	 * 3.1 请求购物车数据
	 * @param handler
	 */
	public void requestListCartItem(final Context context, int userId, int addressId){
		RequestParam paramPath = new RequestParam("cart/list")
		.setParams("userId", userId)
		.setParams("addressId", addressId == -1 ? "" : String.valueOf(addressId));
		HttpManager.getInstance(context).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				CartTotalData cartTotalData = gson.fromJson(response, new TypeToken<CartTotalData>() { }.getType());
				mInterface.requestListCartItemResult(cartTotalData);
			}
		});
	}
	
	/**
	 * 3. 2请求添加到购物车
	 * @param quantity
	 */
	public void requestAddCart(final Context context, int userId,
			int goodsId, String productType, int quantity, String token, int addressId) {
		RequestParam paramPath = new RequestParam("cart/insert")
		.setParams("userId", userId)
		.setParams("id", goodsId)
		.setParams("productType", "02")  //02是商品
		.setParams("amount", quantity)
		.setParams("token", token)
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode");
					String msg = response.getString("msg");
					mInterface.requestAddCartResult(returnCode, msg);
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
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
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
		HttpManager.getInstance(context).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
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
