package com.guozha.buy.global;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.global.QuickMenu;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.MarketHomePage;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;

/**
 * 主界面数据管理类
 * @author PeggyTong
 *
 */
public class MainPageInitDataManager {
	
	public static final int HAND_INITDATA_MSG_ACCOUNTINFO = 0x0001;  //账户信息
	public static final int HAND_INITDATA_MSG_ITEMTYPE = 0x0002;  	 //菜单
	public static final int HAND_INITDATA_MSG_MARKETHOME = 0x0003;	 //逛菜场的主页数据
	public static final int HAND_INITDATA_MSG_FIRST_CATEGORY = 0x004;//一级类目
	public static final int HAND_INITDATA_MSG_CART_ITEM = 0x0005;	 //购物车
	
	private Context mContext;  //注意，这里是全局的context
	
	private Gson mGson;
	
	private AccountInfo mAccountInfo;
	private ArrayList<GoodsItemType> mGoodsItemTypes;
	private MarketHomePage mMarketHomePage;
	private List<QuickMenu> mQuickMenus = new ArrayList<QuickMenu>();
	
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
	
	/**
	 * 获取一级类目
	 * @param handler
	 * @return
	 */
	public List<QuickMenu> getQuickMenus(Handler handler){
		if(mQuickMenus == null){
			requestFirstItemClass(handler);
		}else{
			if(handler != null){
				handler.sendEmptyMessage(HAND_INITDATA_MSG_FIRST_CATEGORY);
			}
		}
		return mQuickMenus;
	}
	
	/**
	 * 获取逛菜场首页的列表数据
	 * @param handler
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public MarketHomePage getMarketHomePage(Handler handler, int pageNum, int pageSize){
		if(mMarketHomePage == null){
			requestGoodsBriefItemData(handler, pageNum, pageSize);
		}else{
			if(handler != null){
				handler.sendEmptyMessage(HAND_INITDATA_MSG_MARKETHOME);
			}
		}
		return mMarketHomePage;
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
	    RequestParam paramPath = new RequestParam("account/info")
	    .setParams("token", token)
	    .setParams("userId", userId);
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
				HttpManager.URL + "goods/frontType/list", new Listener<String>() {
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
	
	/**
	 * 获取一级类目
	 */
	private void requestFirstItemClass(final Handler handler){
		String paramsPath = "goods/frontType/typeList?frontTypeId=";
		HttpManager.getInstance(mContext).volleyRequestByPost(
				HttpManager.URL + paramsPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				List<GoodsItemType> goodsItemTypes = 
						gson.fromJson(response, new TypeToken<List<GoodsItemType>>() { }.getType());
				QuickMenu quickMenu;
				if(goodsItemTypes == null) return;
				for(int i = 0; i < goodsItemTypes.size(); i++){
					int id = goodsItemTypes.get(i).getFrontTypeId();
					String shortName = goodsItemTypes.get(i).getShortName();
					quickMenu = new QuickMenu(id, shortName);
					mQuickMenus.add(quickMenu);
				}
				if(mQuickMenus == null) return;
				List<QuickMenu> defaultQuickMenu = new ArrayList<QuickMenu>();
				for(int i = 0; i < mQuickMenus.size(); i++){
					if(i >= 5) break;
					LogUtil.e("QuickName = " + mQuickMenus.get(i).getName());
					defaultQuickMenu.add(mQuickMenus.get(i));
				}
				ConfigManager.getInstance().setQuickMenus(defaultQuickMenu);
				if(handler != null){
					handler.sendEmptyMessage(HAND_INITDATA_MSG_FIRST_CATEGORY);
				}
			}
		});
	}
	
	/**
	 * 获取(简要：6条）菜品信息
	 * @param handler
	 */
	private void requestGoodsBriefItemData(final Handler handler, int pageNum, int pageSize){
		String addressId = "";
		RequestParam paramPath = new RequestParam("goods/general/list")
		.setParams("addressId", addressId)
		.setParams("pageNum", pageNum)
		.setParams("pageSize", pageSize);
		HttpManager.getInstance(mContext).volleyRequestByPost(HttpManager.URL + paramPath, 
			new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mMarketHomePage = gson.fromJson(response, new TypeToken<MarketHomePage>() { }.getType());
					if(handler != null && mMarketHomePage != null){
						handler.sendEmptyMessage(HAND_INITDATA_MSG_MARKETHOME);
					}
				}
			});
	}
	
}
