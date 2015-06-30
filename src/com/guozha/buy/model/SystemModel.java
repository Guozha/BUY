package com.guozha.buy.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.global.SearchResult;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.SystemModelResult;

public class SystemModel extends BaseModel{
	
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
		
		/**
		 * 用户反馈
		 * @param returnCode
		 * @param msg
		 */
		public void requestFeadbackResult(String returnCode, String msg);
		
		/**
		 * 邀请有奖结果
		 * @param returnCode
		 * @param msg
		 */
		public void requestInviteShareResult(String returnCode, int inviteId, String shareUrl, String msg);
	
		/**
		 * 邀请有奖汇总信息
		 * @param drawAmount
		 * @param usedAmount
		 * @param awardPrice
		 */
		public void requestInviteInfoResult(int drawAmount, int usedAmount, int awardPrice);
	
		/**
		 * 请求图片的路径
		 * @param imgPath
		 */
		public void requestImagePathResult(String imgPath);
		
		/**
		 * 获取服务微信号
		 * @param weixinnum
		 */
		public void requestWeixinNumResult(String weixinnum);
	}
	
	/**
	 * 10.1 请求系统当前时间
	 */
	public void requestSystemTime(final Context context) {
		RequestParam paramPath = new RequestParam("system/date");
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						long currentdate = response.getLong("gregorianDate");
						mInterface.requestSystemTime(currentdate);
					} catch (JSONException e) {
						jsonException(context);
						e.printStackTrace();
					}
				}
		});
	}
	
	/**
	 * 初始化数据
	 */
	public void requestSystemSearched(final Context context, String keyWord, int addressId){
		RequestParam paramPath = new RequestParam("v31/search")
		.setParams("word", keyWord)
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
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
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestWarnPlanResult(returnCode, msg);
					} catch (JSONException e) {
						jsonException(context);
						e.printStackTrace();
					}
				}
		});
	}
	
	/**
	 * 反馈
	 * @param feadback
	 */
	public void requestFeadback(final Context context, String token, int userId, String feadback){
		RequestParam paramPath = new RequestParam("account/opinion/insert")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("opinion", feadback);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestFeadbackResult(returnCode, msg);
					} catch (JSONException e) {
						jsonException(context);
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * 推荐有奖
	 * @param context
	 */
	public void requestInviteShare(final Context context, int userId, String token){
		RequestParam paramPath = new RequestParam("account/invite/insert")
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
						int inviteId = response.getInt("inviteId");
						String shareUrl = response.getString("shareUrl");
						mInterface.requestInviteShareResult(returnCode, inviteId, shareUrl, msg);
					} catch (JSONException e) {
						jsonException(context);
						e.printStackTrace();
					}
				}
		});
	}
	
	/***
	 * 推荐有奖信息
	 */
	public void requestInviteInfo(final Context context, int userId){
		RequestParam paramPath = new RequestParam("account/invite/info")
		.setParams("userId", userId);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						int drawAmount = response.getInt("drawAmount");
						int usedAmount = response.getInt("usedAmount");
						int awardPrice = response.getInt("awardPrice");
						mInterface.requestInviteInfoResult(drawAmount, usedAmount, awardPrice);
					} catch (JSONException e) {
						jsonException(context);
						e.printStackTrace();
					}
				}
		});
	}
	
	/**
	 * 请求图片路径
	 * @param context
	 */
	public void requestImagePath(final Context context){
		RequestParam paramPath = new RequestParam("system/imgPath");
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String imgPath = response.getString("imgPath");
					mInterface.requestImagePathResult(imgPath);
				} catch (JSONException e) {
					jsonException(context);
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 请求微信服务号
	 * @param context
	 */
	public void requestWeixinNum(final Context context, String token, int addressId){
		//TODO
		RequestParam paramPath = new RequestParam("serve/weixin")
		.setParams("token", token)
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String weixinnum = response.getString("weiXin");
					mInterface.requestWeixinNumResult(weixinnum);
				} catch (JSONException e) {
					jsonException(context);
					e.printStackTrace();
				}
			}
		});
	}
}
