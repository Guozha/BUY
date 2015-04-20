package com.guozha.buy.global;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartMarketItem;
import com.guozha.buy.entry.cart.CartTotalData;
import com.guozha.buy.entry.global.QuickMenu;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.GoodsSecondItemType;
import com.guozha.buy.entry.market.MarketHomePage;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.entry.mpage.TodayInfo;
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
	public static final int HAND_INTTDATA_MSG_ADDRESS_LIST = 0x0006; //地址列表
	public static final int HAND_INITDATA_MSG_TODAY_INFO = 0x0007;	 //今日时间
	public static final int HAND_INITDATA_MSG_CURRENT_TIME = 0x0008;	 //获取当前时间
	public static final int HAND_INITDATA_MSG_PLAN_MENU_STATUS = 0x0009;	//今日菜票计划状态
	
	public static boolean mAccountUpdated = false;  //用户账户信息发生了变化
	public static boolean mAddressUpdated = false; 	//地址信息是否发生了变化
	public static boolean mCartItemsUpdated = false; //购物车是否发生了变化
	public static boolean mMarketItemUpdated = false; //商品条目是否发生了变化
	
	private Context mContext;  //注意，这里是全局的context
	
	private Gson mGson;
	
	private AccountInfo mAccountInfo;
	private ArrayList<GoodsItemType> mGoodsItemTypes;
	private MarketHomePage mMarketHomePage;
	private List<QuickMenu> mQuickMenus;
	private List<AddressInfo> mAddressInfos;
	private CartTotalData mCartTotalData;
	private boolean mPlanMenuStatus = false;
	
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
		if(mAccountInfo == null || mAccountUpdated){
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
		if(mMarketHomePage == null || mMarketItemUpdated){
			requestGoodsBriefItemData(handler, pageNum, pageSize);
		}else{
			if(handler != null){
				handler.sendEmptyMessage(HAND_INITDATA_MSG_MARKETHOME);
			}
		}
		return mMarketHomePage;
	}
	
	/**
	 * 获取用户添加的地址
	 * @param handler
	 * @return
	 */
	public List<AddressInfo> getAddressInfos(Handler handler){
		if(mAddressInfos == null || mAddressUpdated){
			requestAdressInfoListData(handler);
		}else{
			if(handler != null){
				handler.sendEmptyMessage(HAND_INTTDATA_MSG_ADDRESS_LIST);
			}
		}
		return mAddressInfos;
	}
	
	/**
	 * 获取购物车数据
	 * @param handler
	 * @return
	 */
	public CartTotalData getCartItems(Handler handler){
		if(mCartTotalData == null || mCartItemsUpdated){
			requestCartItemsData(handler);
		}else{
			if(handler != null){
				handler.sendEmptyMessage(HAND_INITDATA_MSG_CART_ITEM);
			}
		}
		return mCartTotalData;
	}
	
	/**
	 * 获取购物车物品数量
	 * @return
	 */
	public int getCartItemsNum(){
		if(mCartTotalData == null) return 0;
		int num = 0;
		List<CartMarketItem> marketItems = mCartTotalData.getGoodsList();
		List<CartCookItem> cookItems = mCartTotalData.getMenuList();
		if(marketItems != null){
			num = num + marketItems.size();
		}
		if(cookItems != null){
			num = num + cookItems.size();
		}
		return num;
	}
	
	private TodayInfo mTodayInfo = null;
	
	/**
	 * 获取今日信息
	 * @param handler
	 * @return
	 */
	public TodayInfo getTodayInfo(Handler handler){
		//每次进来都重新请求
		requestMPageTodayMessage(handler);
		if(mTodayInfo != null && handler != null){
			handler.sendEmptyMessage(HAND_INITDATA_MSG_TODAY_INFO);
		}
		return mTodayInfo;
	}
	
	/**
	 * 获取今日菜谱计划状态
	 * @param handler
	 */
	public boolean getMenuPlaneStatus(final Handler handler){
		requestPlanMenusStatus(handler);
		return mPlanMenuStatus;
	}
	
	/**
	 * 获取系统当前时间
	 * @param handler
	 */
	public void getSystemTime(final Handler handler){
		HttpManager.getInstance(mContext).volleyJsonRequestByPost(
			HttpManager.URL + "system/date", new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						long currentdate = response.getLong("gregorianDate");
						ConfigManager.getInstance().setTodayDate(currentdate);
						if(handler == null) return;
						handler.sendEmptyMessage(HAND_INITDATA_MSG_CURRENT_TIME);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
	
	//////////////////////////////HTTP-请求////////////////////////////////
	
	/**
	 * 请求购物车数据
	 * @param handler
	 */
	private void requestCartItemsData(final Handler handler){
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		if(token == null) return;
		RequestParam paramPath = new RequestParam("cart/list")
		.setParams("userId", userId)
		.setParams("addressId", addressId == -1 ? "" : String.valueOf(addressId));
		HttpManager.getInstance(mContext).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				mCartTotalData = gson.fromJson(response, new TypeToken<CartTotalData>() { }.getType());
				if(handler != null && mCartTotalData != null){
					mCartItemsUpdated = false;
					handler.sendEmptyMessage(HAND_INITDATA_MSG_CART_ITEM);
				}
			}
		});
	}
	

	
	/**
	 * 获取用户信息
	 * @param 
	 * @return 
	 */
	private void requestAccountInfo(final Handler handler) {
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
					mAccountUpdated = false;
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
				if(mGoodsItemTypes != null){
					formatGoodsItemTypes();
				}
				if(handler != null && mGoodsItemTypes != null){
					handler.sendEmptyMessage(HAND_INITDATA_MSG_ITEMTYPE);
				}
			}
		});
	}
	
	/**
	 * 转换格式
	 */
	private void formatGoodsItemTypes(){
		for(int i = 0; i < mGoodsItemTypes.size(); i++){
			GoodsItemType itemType = mGoodsItemTypes.get(i);
			GoodsSecondItemType secondItemType = new GoodsSecondItemType(
					itemType.getFrontTypeId(), itemType.getShortName(), "查看全部...");
			List<GoodsSecondItemType> secondItemList = itemType.getFrontTypeList();
			secondItemList.add(0, secondItemType);
		}
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
				mQuickMenus = new ArrayList<QuickMenu>();
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
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("goods/general/list")
		.setParams("addressId", addressId == -1 ? "" : String.valueOf(addressId))
		.setParams("pageNum", pageNum)
		.setParams("pageSize", pageSize);
		HttpManager.getInstance(mContext).volleyRequestByPost(HttpManager.URL + paramPath, 
			new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mMarketHomePage = gson.fromJson(response, new TypeToken<MarketHomePage>() { }.getType());
					if(handler != null && mMarketHomePage != null){
						mMarketItemUpdated = false;
						handler.sendEmptyMessage(HAND_INITDATA_MSG_MARKETHOME);
					}
				}
			});
	}
	
	/**
	 * 请求地址信息列表
	 * @param handler
	 */
	private void requestAdressInfoListData(final Handler handler){
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("account/address/list")
		.setParams("userId", userId);
		HttpManager.getInstance(mContext).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mAddressInfos = gson.fromJson(response, 
							new TypeToken<List<AddressInfo>>() { }.getType());
					setDefaultAddressChoosed();
					if(handler != null && mAddressInfos != null){
						mAddressUpdated = false;
						handler.sendEmptyMessage(HAND_INTTDATA_MSG_ADDRESS_LIST);
					}
				}

				
		});
	}
	
	/**
	 * 设置默认的地址被自动选中
	 */
	private void setDefaultAddressChoosed() {
		if(mAddressInfos == null) return;
		for(int i = 0; i < mAddressInfos.size(); i++){
			AddressInfo addressInfo = mAddressInfos.get(i);
			if("1".equals(addressInfo.getDefaultFlag())){
				ConfigManager.getInstance().setChoosedAddressId(addressInfo.getAddressId());
			}
		}
	}
	
	/**
	 * 请求MPage的当日信息
	 */
	private void requestMPageTodayMessage(final Handler handler){
		RequestParam paramPath = new RequestParam("menuplan/todayInfo");
		HttpManager.getInstance(mContext).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String calendarSolar = response.getString("today");
						String calendarLunar = response.getString("lunarToday");
						String todayDescript = response.getString("dayDesc");
						mTodayInfo = new TodayInfo(calendarSolar, calendarLunar, todayDescript);
						if(handler != null && mTodayInfo != null){
							handler.sendEmptyMessage(HAND_INITDATA_MSG_TODAY_INFO);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
	
	/**
	 * 请求今日菜谱计划状态
	 * @param handler
	 */
	private void requestPlanMenusStatus(final Handler handler) {
		String token = ConfigManager.getInstance().getUserToken();
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("menuplan/exist")
		.setParams("token", token)
		.setParams("userId", userId);
		
		HttpManager.getInstance(mContext).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							mPlanMenuStatus = true;
						}else{
							mPlanMenuStatus = false;
						}
						if(handler != null){
							handler.sendEmptyMessage(HAND_INITDATA_MSG_PLAN_MENU_STATUS);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
	
}
