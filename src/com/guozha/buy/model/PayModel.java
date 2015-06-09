package com.guozha.buy.model;

import java.util.List;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.cart.PayValidateResult;
import com.guozha.buy.entry.cart.PayWayEntry;
import com.guozha.buy.entry.mine.UsefulTicket;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.PayModelResult;

public class PayModel {
	
	private PayModelInterface mInterface;
	
	public PayModel(){
		mInterface = new PayModelResult();
	}
	
	public PayModel(PayModelInterface payModelInterface){
		mInterface = payModelInterface;
	}
	
	public interface PayModelInterface{
		
		/**
		 * 获取支付方式列表
		 * @param payWayEntry
		 */
		public void requestPayWaysResult(List<PayWayEntry> payWayList);
		
		public void requestPreparePayResult(PayValidateResult payValidateResult);
		
		public void requestValidTicketResult(UsefulTicket userfulTicket);
		
	}

	public void requestPayWays(final Context context, int addressId){
		RequestParam paramPath = new RequestParam("payment/listPayWay")
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<PayWayEntry> payWayList= gson.fromJson(response, new TypeToken<List<PayWayEntry>>() { }.getType());
					mInterface.requestPayWaysResult(payWayList);
				}
		});
	}
	
	public void requestPreparePay(final Context context, String token, int userId, int orderId, 
			int ticketId, int payWayId, int balanceDecPrice, int userBeanAmount){
		RequestParam paramPath = new RequestParam("payment/preparePay")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("orderId", orderId)
		.setParams("useTicketId", ticketId == -1 ? "0": String.valueOf(ticketId))
		.setParams("payWayId", payWayId)
		.setParams("balanceDecPrice", balanceDecPrice)
		.setParams("useBeanAmount", userBeanAmount);
		HttpManager.getInstance(context).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					PayValidateResult payValidateResult = 
							gson.fromJson(response, new TypeToken<PayValidateResult>() { }.getType());
					mInterface.requestPreparePayResult(payValidateResult);
				}
		});
	}
	
	/**
	 * 获取有效菜票
	 */
	public void requestValidTicket(final Context context, String token, int userId, int money){
		RequestParam paramPath = new RequestParam("account/ticket/listValid")
		.setParams("userId", userId)
		.setParams("money", money)
		.setParams("token", token);

		HttpManager.getInstance(context).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					UsefulTicket userfulTicket = gson.fromJson(response, new TypeToken<UsefulTicket>() { }.getType());
					mInterface.requestValidTicketResult(userfulTicket);
				}
		});
	}
}
