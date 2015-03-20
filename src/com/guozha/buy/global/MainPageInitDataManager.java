package com.guozha.buy.global;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.LogUtil;

/**
 * 主界面数据管理类
 * @author PeggyTong
 *
 */
public class MainPageInitDataManager {
	
	public static final int HAND_INITDATA_MSG_ACCOUNTINFO = 0x0001;  //账户信息
	public static final int HAND_INITDATA_MSG_ITEMTYPE = 0X0002;  //菜单
	
	private Context mContext;  //注意，这里是全局的context
	
	private Gson mGson;
	
	private AccountInfo mAccountInfo;
	private ArrayList<GoodsItemType> mGoodsItemTypes;
	
	
	
	private static MainPageInitDataManager mInitDataManager;
	
	private MainPageInitDataManager(Context context){
		this.mContext = context;
		this.mGson = new GsonBuilder().enableComplexMapKeySerialization().create(); 
	}
	
	/**
	 * 获取实例
	 * @param context
	 * @return
	 */
	public static MainPageInitDataManager getInstance(Context context){
		if(mInitDataManager == null){
			mInitDataManager = new MainPageInitDataManager(context);
		}
		return mInitDataManager;
	}
	
	/**
	 * 清空数据
	 */
	public void clearAllData(){
		if(mInitDataManager.mAccountInfo != null){
			mInitDataManager.mAccountInfo = null;
		}
	}
	
	///////////////////////////////GET-方法//////////////////////////////
	
	/**
	 * 获取账号信息
	 * @return
	 */
	public AccountInfo getAccountInfo(Handler handler){
		if(mAccountInfo == null){
			requestAccountInfo(handler);
		}else{
			if(handler != null){
				handler.sendEmptyMessage(HAND_INITDATA_MSG_ACCOUNTINFO);
			}
		}
		return mAccountInfo;
	}
	
	/**
	 * 获取菜品菜单
	 * @param handler
	 * @return
	 */
	public ArrayList<GoodsItemType> getGoodsItemType(Handler handler){
		if(mGoodsItemTypes == null){
			requestGoodsItemTypedData(handler);
		}else{
			if(handler != null){
				handler.sendEmptyMessage(HAND_INITDATA_MSG_ITEMTYPE);
			}
		}
		return mGoodsItemTypes;
	}
	
	//////////////////////////////HTTP-请求////////////////////////////////
	
	/**
	 * 获取用户信息
	 * @param 
	 * @return 
	 */
	private void requestAccountInfo(final Handler handler) {
		LogUtil.e("获取用户信息");
		ConfigManager configManager = ConfigManager.getInstance();
		HttpManager httpManager = HttpManager.getInstance(mContext);
		String token = configManager.getUserToken();
		int userId = configManager.getUserId();
		if(token == null || userId == -1) return;
	    String paramPath = "account/info?token=" + token + "&&userId=" + userId;
		httpManager.volleyRequestByPost(HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				mAccountInfo = mGson.fromJson(response, AccountInfo.class);
				if(handler != null && mAccountInfo != null){
					handler.sendEmptyMessage(HAND_INITDATA_MSG_ACCOUNTINFO);
				}
			}
		});
	}
	
	/**
	 * 获取菜单条目列表数据
	 */
	private void requestGoodsItemTypedData(final Handler handler){
		HttpManager.getInstance(mContext).volleyRequestByPost(
				HttpManager.URL + "goods/frontTypes", new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				mGoodsItemTypes = gson.fromJson(response, new TypeToken<ArrayList<GoodsItemType>>() { }.getType());
				if(handler != null && mGoodsItemTypes != null){
					handler.sendEmptyMessage(HAND_INITDATA_MSG_ITEMTYPE);
				}
			}
		});
	}
	
}
