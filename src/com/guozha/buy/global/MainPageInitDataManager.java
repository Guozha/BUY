package com.guozha.buy.global;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartMarketItem;
import com.guozha.buy.entry.cart.CartTotalData;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.GoodsSecondItemType;
import com.guozha.buy.entry.market.MarketHomePage;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.entry.mpage.TodayInfo;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.GoodsModel;
import com.guozha.buy.model.ShopCartModel;
import com.guozha.buy.model.result.GoodsModelResult;
import com.guozha.buy.model.result.ShopCartModelResult;

/**
 * 主界面数据管理类
 * @author PeggyTong
 *
 */
public class MainPageInitDataManager {
	
	private Context mContext;  //注意，这里是全局的context
	
	private Gson mGson;
	
	private AccountInfo mAccountInfo;
	private List<GoodsItemType> mGoodsItemTypes;
	private MarketHomePage mMarketHomePage;
	private List<AddressInfo> mAddressInfos;
	private CartTotalData mCartTotalData;
	private TodayInfo mTodayInfo = null;
	private long mSystemTime;
	
	private ShopCartModel mShopCartModel;
	private GoodsModel mGoodsModel;
	
	
	private static MainPageInitDataManager mInitDataManager;
	
	private MainPageInitDataManager(Context context){
		this.mContext = context;
		this.mGson = new GsonBuilder().enableComplexMapKeySerialization().create(); 
		initModel();
	}
	
	private void initModel(){
		mShopCartModel = new ShopCartModel(new MyShopCartModelResult());
		mGoodsModel = new GoodsModel(new MyGoodsModelResult());
	}
	
	/**
	 * 获取实例
	 * @param context
	 * @return
	 */
	public static MainPageInitDataManager getInstance(){
		if(mInitDataManager == null){
			mInitDataManager = new MainPageInitDataManager(CustomApplication.getContext());
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
	
	/**
	 * 初始化界面数据
	 * @param handler
	 */
	public void initPageData(){
		requestSystemTime();		//请求系统当前时间
		requestCartItemsData();		//请求购物车信息
		requestAccountInfo();   	//获取用户信息
		requestGoodsItemTypedData(); //获取菜单条目列表数据
		requestGoodsBriefItemData(1, 4); //获取(简要：6条）菜品信息
		requestAdressInfoListData();   //请求地址信息列表
	}
	
	///////////////////////////////GET-方法//////////////////////////////
	
	/**
	 * 获取账号信息
	 * @return
	 */
	public AccountInfo getAccountInfo(){
		return mAccountInfo;
	}
	
	/**
	 * 获取菜品菜单
	 * @param handler
	 * @return
	 */
	public List<GoodsItemType> getGoodsItemType(){
		return mGoodsItemTypes;
	}
	
	/**
	 * 获取逛菜场首页的列表数据
	 * @param handler
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public MarketHomePage getMarketHomePage(int pageNum, int pageSize){
		return mMarketHomePage;
	}
	
	/**
	 * 获取今日信息
	 * 
	 * @param handler
	 * @return
	 */
	public TodayInfo getTodayInfo() {
		return mTodayInfo;
	}
	
	/**
	 * 获取用户添加的地址
	 * @param handler
	 * @return
	 */
	public List<AddressInfo> getAddressInfos(){
		return mAddressInfos;
	}
	
	/**
	 * 获取购物车数据
	 * @param handler
	 * @return
	 */
	public CartTotalData getCartItems(){
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
	
	public long getSystemTime(){
		return mSystemTime;
	}

	//////////////////////////////HTTP-请求////////////////////////////////
	
	/**
	 * 请求系统当前时间
	 */
	private void requestSystemTime() {
		HttpManager.getInstance(mContext).volleyJsonRequestByPost(
			HttpManager.URL + "system/date", new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						long currentdate = response.getLong("gregorianDate");
						ConfigManager.getInstance().setTodayDate(currentdate);
						mSystemTime = currentdate;
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
	
	/**
	 * 请求购物车数据
	 * @param handler
	 */
	private void requestCartItemsData(){
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		if(token == null) return;
		
		mShopCartModel.requestListCartItem(mContext, userId, addressId);
	}
	
	/**
	 * 获取用户信息
	 * @param 
	 * @return 
	 */
	private void requestAccountInfo() {
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
			}
		});
	}
	
	/**
	 * 获取菜单条目列表数据
	 */
	private void requestGoodsItemTypedData(){
		mGoodsModel.requestGoodsTypes(mContext);
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
	 * 获取(简要：6条）菜品信息
	 * @param handler
	 */
	private void requestGoodsBriefItemData(int pageNum, int pageSize){
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		mGoodsModel.requestGoodsList(mContext, addressId, pageNum, pageSize);
	}
	
	/**
	 * 请求地址信息列表
	 * @param handler
	 */
	private void requestAdressInfoListData(){
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
	
	class MyShopCartModelResult extends ShopCartModelResult{
		@Override
		public void requestListCartItemResult(CartTotalData cartTotalData) {
			mCartTotalData = cartTotalData;
		}
	}
	
	class MyGoodsModelResult extends GoodsModelResult{
		@Override
		public void requestGoodsTypesResult(List<GoodsItemType> goodsItemTypes) {
			mGoodsItemTypes = goodsItemTypes;
			if(mGoodsItemTypes != null){
				formatGoodsItemTypes();
			}
		}
		
		@Override
		public void requestGoodsListResult(MarketHomePage marketHomePage) {
			mMarketHomePage = marketHomePage;
		}
	}
	
}
