package com.guozha.buy.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.guozha.buy.entry.global.UserInfor;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.util.LogUtil;
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
		
	}

	
	
	/**
	 * 获取验证码
	 * 
	 * @param phoneNum
	 */
	public void obtainPhoneValidate(final Context context, String phoneNum) {
		RequestParam paramPath = new RequestParam(
				"account/checkCodeForOnekeyLogin").setParams("mobileNo", phoneNum);
		
		LogUtil.e("path = " + HttpManager.URL + paramPath);
		
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
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
		RequestParam paramPaht = new RequestParam("account/onekeyLogin")
		.setParams("mobileNo", phoneNum)
		.setParams("checkCode", checkCode)
		.setParams("inviteCode", invitateCode);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPaht, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
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
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
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
}
