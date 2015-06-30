package com.guozha.buy.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.cart.TimeList;
import com.guozha.buy.entry.mine.order.OrderDetail;
import com.guozha.buy.entry.mine.order.OrderResult;
import com.guozha.buy.entry.mine.order.OrderSummaryPage;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.OrderModelResult;
import com.guozha.buy.util.LogUtil;

/**
 * 订单相关的数据获取
 * @author PeggyTong
 *
 */
public class OrderModel extends BaseModel{
	
	public static final String ORDRE_UNFINISH = "1";
	public static final String ORDER_FINISHED = "2";
	
	private OrderModelInterface mInterface;
	
	public OrderModel(OrderModelInterface orderModelInterface){
		mInterface = orderModelInterface;
	}
	
	public OrderModel(){
		mInterface = new OrderModelResult();
	}
	
	public interface OrderModelInterface{
		/**
		 * 41. 获取配送时间结果
		 * @param timeList
		 */
		public void requestOrderTimesResult(TimeList timeList);
		
		/**
		 * 4.2 获取请求订单结果
		 * @param returnCode
		 * @param msg
		 * @param orderId
		 */
		public void requestSubmitOrderResult(String returnCode, String msg, int orderId);
		
		/**
		 * 4.5 查询订单结果
		 * @param orderSummaryPage
		 */
		public void requestListOrderResult(OrderSummaryPage orderSummaryPage);
		
		/**
		 * 4.6 查看主单信息结果
		 * @param totalPrice
		 * @param serviceFee
		 */
		public void requestOrderInforResult(int totalPrice, int serviceFee);
		
		/**
		 * 4.7 查看订单详情结果
		 * @param orderDetail
		 */
		public void requestOrderDetailResult(OrderDetail orderDetail);
		
		/**
		 * 4.8 请求取消订单结果
		 * @param returnCode
		 * @param msg
		 */
		public void requestCancelOrderResult(String returnCode, String msg);
		
		/**
		 * 4.13 请求订单评价结果
		 * @param returnCode
		 * @param msg
		 */
		public void requestOrderMarkResult(String returnCode, String msg);
		
		/**
		 * 4.12 确认订单，获取商品总额和服务费
		 * @param returnCode
		 * @param msg
		 * @param totalPrice
		 * @param serviceFee
		 */
		public void requestOrderConfirmResult(String returnCode, String msg, int totalPrice, int serviceFee);
		
		/**
		 * 4.13 下单并支付
		 * @param orderResult
		 */
		public void requestOrderNomalInsertResult(String returnCode, String msg, int orderId);
		
		
		public void requestPayCountResult(OrderResult orderResult);
	}
	
	
	/**
	 * 4.1 获取配送时段
	 * @param context
	 * @param addressId
	 */
	public void requestOrderTimes(final Context context, int addressId){
		RequestParam paramPath = new RequestParam("order/times")
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				TimeList timeList = gson.fromJson(response, new TypeToken<TimeList>() { }.getType());
				mInterface.requestOrderTimesResult(timeList);
			}
		});
	}
	
	/**
	 * 4.2 请求提交订单
	 * @param context
	 * @param token
	 * @param userId
	 * @param addressId
	 * @param upTime
	 * @param downTime
	 * @param memo
	 */
	public void requestSubmitOrder(final Context context, String token, 
			int userId, int addressId, String upTime, String downTime, String memo){
		RequestParam paramPath = new RequestParam("order/insert")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("addressId", addressId)
		.setParams("wantUpTime", upTime)
		.setParams("wantDownTime", downTime)
		.setParams("memo", memo);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String returnCode = response.getString("returnCode");
					int orderId = response.getInt("orderId");
					String msg = response.getString("msg");
					mInterface.requestSubmitOrderResult(returnCode, msg, orderId);
				} catch (JSONException e) {
					jsonException(context);
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 *  4.5 查询订单
	 * @param context
	 * @param pageNum
	 * @param pageSize
	 */
	public void requestListOrder(final Context context, int userId, String searchType, int pageNum, int pageSize){
		RequestParam paramPath = new RequestParam("order/list")
		.setParams("userId", userId)
		.setParams("searchType", searchType) //1 表示进行中 2 表示已完成
		.setParams("pageNum", pageNum)
		.setParams("pageSize", pageSize);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					OrderSummaryPage orderSummaryPage = gson.fromJson(response, new TypeToken<OrderSummaryPage>() { }.getType());
					mInterface.requestListOrderResult(orderSummaryPage);
				}
			});
	}
	
	
	/**
	 * 4.6 查看主单信息
	 * @param context
	 * @param orderId
	 */
	public void requestOrderInfo(final Context context, int orderId){
		//请求主单信息
		RequestParam paramPath = new RequestParam("order/info")
		.setParams("orderId", orderId);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						//mOrderNum = response.getString("orderNo");  //订单号
						int totalPrice = response.getInt("totalPrice");//商品金额
						int serviceFee = response.getInt("serviceFee");	//服务费
						mInterface.requestOrderInforResult(totalPrice, serviceFee);
					} catch (JSONException e) {
						jsonException(context);
						e.printStackTrace();
					}
				}
		});
	}
	
	/**
	 * 4.7 查看订单详情
	 * @param context
	 * @param orderId
	 */
	public void requestOrderDetail(final Context context, int orderId){
		RequestParam paramPath = new RequestParam("order/detail")
		.setParams("orderId", orderId);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					OrderDetail orderDetail = gson.fromJson(response, new TypeToken<OrderDetail>() { }.getType());
					mInterface.requestOrderDetailResult(orderDetail);
				}
		});
	}
	
	/**
	 * 4.8 请求取消订单
	 * @param context
	 * @param token
	 * @param orderId
	 * @param status
	 */
	public void requestCancelOrder(final Context context, String token, int orderId, String status) {
		
		RequestParam paramPath = new RequestParam("order/cancel")
		.setParams("token", token)
		.setParams("orderId", orderId)
		.setParams("status", status);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestCancelOrderResult(returnCode, msg);
					} catch (JSONException e) {
						jsonException(context);
						e.printStackTrace();
					}
				}
		});
	}
	
	/**
	 * 4.13 订单评价
	 * @param context
	 * @param token
	 * @param orderId
	 * @param commentDesc
	 */
	public void requestOrderMark(final Context context, String token, int orderId, int serviceStar, String commentDesc){
		RequestParam paramPath = new RequestParam("v31/order/mark")
		.setParams("token", token)
		.setParams("orderId", orderId)
		.setParams("commentDesc", commentDesc)
		.setParams("serviceStar", serviceStar);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestOrderMarkResult(returnCode, msg);
					} catch (JSONException e) {
						jsonException(context);
						e.printStackTrace();
					}
				}
		});
	}
	
	/**
	 * 4.12 确认订单并获取商品的总额和服务费
	 */
	public void requestOrderConfirm(final Context context, String token, int userId, int addressId){
		RequestParam paramPath = new RequestParam("v31/order/confirmOrder")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyRequestByPost(paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String returnCode = response.getString("returnCode");
					String msg = response.getString("msg");
					int totalPrice = response.getInt("totalPrice");
					int serviceFee = response.getInt("serviceFee");
					mInterface.requestOrderConfirmResult(returnCode, msg, totalPrice, serviceFee);
				} catch (JSONException e) {
					jsonException(context);
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 4.13 下单并支付
	 * @param context
	 * @param token
	 * @param userId
	 * @param orderId
	 * @param balanceDecPrice
	 * @param useTicketId
	 * @param useBeanAmount
	 * @param payWayId
	 * @param addressId
	 * @param wantUpTime
	 * @param wantDownTime
	 * @param memo
	 */
	public void requestOrderNomalInsert(final Context context, String token, int userId, int addressId,
			String wantUpTime, String wantDownTime, String memo){
		RequestParam paramPath = new RequestParam("v31/order/insertNormal")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("addressId", addressId)
		.setParams("wantUpTime", wantUpTime)
		.setParams("wantDownTime", wantDownTime)
		.setParams("memo", memo);
		
		HttpManager.getInstance(context).volleyRequestByPost(paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String returnCode = response.getString("returnCode");
					String msg = response.getString("msg");
					int orderId = response.getInt("orderId");
					mInterface.requestOrderNomalInsertResult(returnCode, msg, orderId);
				} catch (JSONException e) {
					jsonException(context);
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 6.3 支付订单—V3.1
	 * @param context
	 * @param token
	 * @param userId
	 * @param orderId
	 * @param balanceDecPrice
	 * @param useTicketId
	 * @param useBeanAmount
	 * @param payWayId
	 */
	public void requestPayCount(final Context context, String token, int userId, int orderId,
			int balanceDecPrice, int useTicketId, int payWayId){
		RequestParam paramPath = new RequestParam("v31/payment/pay")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("orderId", orderId)
		.setParams("balanceDecPrice", balanceDecPrice)
		.setParams("useTicketId", useTicketId)
		//.setParams("useBeanAmount", useBeanAmount)
		.setParams("payWayId", payWayId);
		
		HttpManager.getInstance(context).volleyRequestByPost(paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				OrderResult orderResult = gson.fromJson(response, new TypeToken<OrderResult>() { }.getType());
				mInterface.requestPayCountResult(orderResult);
			}
		});
	}
}
