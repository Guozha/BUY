package com.guozha.buy.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.controller.SetWarnTimeActivity;
import com.guozha.buy.entry.global.SearchResult;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.SystemModelResult;
import com.guozha.buy.util.ToastUtil;

public class SystemModel {
	
	private SystemModelInterface mInterface;
	
	public SystemModel(){
		mInterface = new SystemModelResult();
	}
	
	public SystemModel(SystemModelInterface systemModelInterface){
		mInterface = systemModelInterface;
	}

	
	public interface SystemModelInterface{
		
		/**
		 * 10.1 请求系统当前时间结果
		 * @param systemTime
		 */
		public void requestSystemTime(long systemTime);
		
		/**
		 * 查询
		 * @param searchResult
		 */
		public void requestSystemSearch(SearchResult searchResult);
		
		/**
		 * 提醒
		 */
		public void requestWarnPlanResult(String returnCode, String msg);
		
	}
	
	/**
	 * 10.1 请求系统当前时间
	 */
	public void requestSystemTime(final Context context) {
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + "system/date", new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						long currentdate = response.getLong("gregorianDate");
						mInterface.requestSystemTime(currentdate);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
	
	/**
	 * 初始化数据
	 */
	public void requestSystemSearched(final Context context, String keyWord, int addressId){
		RequestParam paramPath = new RequestParam("search")
		.setParams("word", keyWord)
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					SearchResult searchResult = gson.fromJson(response, new TypeToken<SearchResult>() { }.getType());
					mInterface.requestSystemSearch(searchResult);
				}
		});
	}
	
	/**
	 * 请求设置提醒时间
	 * @param warnTime
	 */
	public void requestWarnPlan(final Context context, String token, int userId, String valueTime) {
		RequestParam paramPath = new RequestParam("menuplan/alert")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("planNotiTime", valueTime);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestWarnPlanResult(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
		});
	}
}
