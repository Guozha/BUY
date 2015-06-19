package com.guozha.buy.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.global.UserInfor;
import com.guozha.buy.entry.mine.MarketTicket;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.entry.mine.address.Country;
import com.guozha.buy.entry.mine.address.KeyWord;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.util.ToastUtil;

/**
 * 用户相关的数据获取
 * @author PeggyTong
 *
 */
public class UserModel extends BaseModel{
	
	private UserModelInterface mInterface;
	
	public UserModel(){
		mInterface = new UserModelResult();
	}
	
	public UserModel(UserModelInterface userModelInterface){
		mInterface = userModelInterface;
	}
	
	public interface UserModelInterface{
		/**
		 * 获取验证码结果 
		 * @param returnCode
		 * @param msg
		 */
		public void obtainPhoneValidateResult(String returnCode, String msg);
		
		/**
		 * 请求验证码登录
		 * @param returnCode
		 * @param msg
		 * @param userInfor
		 */
		public void requestCheckLogin(String returnCode, String msg, UserInfor userInfor);
		
		/**
		 * 请求密码登录
		 * @param returnCode
		 * @param msg
		 * @param userInfor
		 */
		public void requestPasswordLogin(String returnCode, String msg, UserInfor userInfor);
		
		/**
		 * 获取用户信息
		 * @param accountInfo
		 */
		public void requestAccountInfoResult(AccountInfo accountInfo);
		
		/**
		 * 1.7.1 查询地址结果
		 * @param adressInfos
		 */
		public void requestListAddressResult(List<AddressInfo> adressInfos);
		
		/**
		 * 1.7.2 获取行政区列表
		 * @param countrys
		 */
		public void requestCountryListResult(List<Country> countrys);
		
		/**
		 * 1.7.3 获取小区列表
		 * @param keyWords
		 */
		public void requestAddressBuilding(List<KeyWord> keyWords);
		
		/**
		 * 1.7.4 添加地址
		 * @param returnCode
		 * @param buildFlag
		 * @param msg
		 */
		public void requestAddAddressResult(String returnCode, String buildFlag, String msg);
		
		/**
		 * 1.7.5 删除地址
		 * @param returnCode
		 * @param msg
		 */
		public void requestDeleteAddressResult(String returnCode, String msg);
		
		/**
		 * 1.7.6 请求设置默认地址
		 * @param returnCode
		 * @param msg
		 */
		public void requestDefaultAddressResult(String returnCode, String msg);
		
		/**
		 * 退出登录
		 * @param returnCode
		 * @param msg
		 */
		public void requestLoginOutResult(String returnCode, String msg);
		
		/**
		 * 获取我的菜票列表
		 * @param marketTickets
		 */
		public void requestMyTicketResult(List<MarketTicket> marketTickets);
	}

	
	
	/**
	 * 获取验证码
	 * 
	 * @param phoneNum
	 */
	public void obtainPhoneValidate(final Context context, String phoneNum) {
		RequestParam paramPath = new RequestParam(
				"account/checkCodeForOnekeyLogin")
		.setParams("mobileNo", phoneNum);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						String returnCode = response
								.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.obtainPhoneValidateResult(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * 请求验证码登录
	 * @param phoneNum
	 * @param pwd
	 */
	public void requestCheckLogin(final Context context, 
			String phoneNum, final String checkCode, String invitateCode) {
		RequestParam paramPath = new RequestParam("account/onekeyLogin")
		.setParams("mobileNo", phoneNum)
		.setParams("checkCode", checkCode)
		.setParams("inviteCode", invitateCode);
		HttpManager.getInstance(context).volleyRequestByPost(paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String returnCode = response.getString("returnCode");
					String msg = response.getString("msg");
					UserInfor userInfor = null;
					if(BaseModel.REQUEST_SUCCESS.equals(returnCode.trim())){
						Integer userId = response.getInt("userId");
						String userToken = response.getString("token");
						String mobileNum = response.getString("mobileNo");
						String pwd = response.getString("passwd");
						userInfor = new UserInfor();
						userInfor.setUserId(userId);
						userInfor.setUserToken(userToken);
						userInfor.setMobileNo(mobileNum);
						userInfor.setPassword(pwd);
					}
					mInterface.requestCheckLogin(returnCode, msg, userInfor);
				} catch (JSONException e) {
					ToastUtil.showToast(context, "数据解析异常");
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 请求账号密码登录
	 * @param phoneNum
	 * @param pwd
	 */
	public void requestPwdLogin(final Context context, String phoneNum, String pwd) {
		RequestParam paramPath = new RequestParam("account/login")
		.setParams("mobileNo", phoneNum)
		.setParams("passwd", pwd);
		final String password = pwd;
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
			@Override
			public void onResponse(String responseStr) {
				try {
					JSONObject response = new JSONObject(responseStr);
					String returnCode = response.getString("returnCode");
					String msg = response.getString("msg");
					UserInfor userInfor = null;
					if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
						Integer userId = response.getInt("userId");
						String userToken = response.getString("token");
						String mobileNum = response.getString("mobileNo");
						
						userInfor = new UserInfor();
						userInfor.setUserId(userId);
						userInfor.setUserToken(userToken);
						userInfor.setMobileNo(mobileNum);
						userInfor.setPassword(password);
					}
					mInterface.requestPasswordLogin(returnCode, msg, userInfor);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}   
	
	/**
	 * 获取用户信息
	 * @param 
	 * @return 
	 */
	public void requestAccountInfo(final Context context, String token, int userId) {
	    RequestParam paramPath = new RequestParam("account/info")
	    .setParams("token", token)
	    .setParams("userId", userId);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				AccountInfo accountInfo = gson.fromJson(response, AccountInfo.class);
				mInterface.requestAccountInfoResult(accountInfo);
			}
		});
	}
	
	/**
	 * 请求退出登录
	 */
	public void requestLoginOut(final Context context, String token) {
		RequestParam paramPath = new RequestParam("account/logout")
		.setParams("token", token);
		HttpManager.getInstance(context)
			.volleyRequestByPost(
					paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					try {
						JSONObject response = new JSONObject(responseStr);
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestLoginOutResult(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
		});
	}
	
	/**
	 * 1.7.1 查询地址
	 * @param context
	 * @param userId
	 */
	public void requestListAddress(final Context context, int userId){
		RequestParam paramPath = new RequestParam("account/address/list")
		.setParams("userId", userId);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<AddressInfo> adressInfos = gson.fromJson(response, 
							new TypeToken<List<AddressInfo>>() { }.getType());
					mInterface.requestListAddressResult(adressInfos);
				}
		});
	}
	
	/**
	 * 1.7.2 获取行政区列表
	 */
	public void requestCountryList(final Context context){
		RequestParam paramPath = new RequestParam("account/address/listArea")
		.setParams("parentAreaId", 2);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<Country> countrys = gson.fromJson(response, new TypeToken<List<Country>>() { }.getType());
					mInterface.requestCountryListResult(countrys);
				}
			});
	}
	
	/**
	 * 1.7.3 获取小区列表
	 */
	public void requestAddressBuilding(final Context context, String token, int countryId){
		RequestParam paramPath = new RequestParam("account/address/listBuilding")
		.setParams("token", token)
		.setParams("countyId", countryId);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<KeyWord> keyWords = gson.fromJson(response, new TypeToken<List<KeyWord>>() { }.getType());
					mInterface.requestAddressBuilding(keyWords);
				}
			});
	}
	

	/**
	 * 请求添加地址
	 */
	public void requestAddAddress(final Context context, String token, int userId, String receiveName,
			String mobileNo, int countryId, String buildName, String detailAddr, String flag) {
		RequestParam paramPath = new RequestParam("account/address/insert")
		.setParams("token", token)
		.setParams("userId", String.valueOf(userId))
		.setParams("receiveName", receiveName)
		.setParams("mobileNo", mobileNo)
		.setParams("provinceId", "1")
		.setParams("cityId", "2")
		.setParams("countyId", countryId)
		.setParams("buildingName", buildName)
		.setParams("detailAddr", detailAddr)
		.setParams("defaultFlag", flag);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
					@Override
					public void onResponse(String responseStr) {
						try {
							JSONObject response = new JSONObject(responseStr);
							String returnCode = response.getString("returnCode");
							String buildFlag = response.getString("buildingFlag");
							String msg = response.getString("msg");
							mInterface.requestAddAddressResult(returnCode, buildFlag, msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	/**
	 * 1.7.5 删除地址
	 * @param context
	 * @param userId
	 * @param token
	 * @param addressId
	 */
	public void requestDeleteAddress(final Context context, int userId, String token, int addressId){
		RequestParam paramPath = new RequestParam("account/address/delete")
		.setParams("userId", userId)
		.setParams("token", token)
		.setParams("addressId", addressId);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String responseStr) {
					//相当于修改，没有判断的必要
					try {
						JSONObject response = new JSONObject(responseStr);
						String returnCode = response.getString("returnCode");
						String msg = response.getString("msg");
						mInterface.requestDeleteAddressResult(returnCode, msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * 1.7.5 设置默认地址
	 * @param context
	 * @param token
	 * @param addressId
	 * @param userId
	 */
	public void requestDefaultAddress(final Context context, String token, int addressId, int userId){
		RequestParam paramPath = new RequestParam("account/address/default")
		.setParams("token", token)
		.setParams("addressId", addressId)
		.setParams("userId", userId);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
					@Override
					public void onResponse(String responseStr) {
						try {
							JSONObject response = new JSONObject(responseStr);
							String returnCode = response.getString("returnCode");
							String msg = response.getString("msg");
							mInterface.requestDefaultAddressResult(returnCode, msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	/**
	 * 获取并设置我的菜票
	 */
	public void requestMyTicket(final Context context, int suerId){
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("account/ticket/list")
		.setParams("userId", userId);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				List<MarketTicket> marketTickets = gson.fromJson(response, new TypeToken<List<MarketTicket>>() { }.getType());
				mInterface.requestMyTicketResult(marketTickets);
			}
		});
	}
}
