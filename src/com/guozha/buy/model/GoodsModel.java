package com.guozha.buy.model;

import java.util.List;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.global.WeightOption;
import com.guozha.buy.entry.market.GoodsDetail;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.ItemSaleInfoPage;
import com.guozha.buy.entry.market.MarketHomePage;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.GoodsModelResult;

public class GoodsModel extends BaseModel{
	
	private GoodsModelInterface mInterface;
	
	public GoodsModel(){
		mInterface = new GoodsModelResult();
	}
	
	public GoodsModel(GoodsModelInterface goodsModelInterface){
		mInterface = goodsModelInterface;
	}
	
	public interface GoodsModelInterface{
		
		/**
		 * 2.1.2 请求全部类目菜单结果
		 * @param goodsItemTypes
		 */
		public void requestGoodsTypesResult(List<GoodsItemType> goodsItemTypes);
		
		/**
		 * 2.1.3 查询首页产品列表
		 * @param marketHomePage
		 */
		public void requestGoodsListResult(MarketHomePage marketHomePage);
		
		/**
		 * 2.1.4 查询产品明细
		 * @param goodsDetail
		 */
		public void requestGoodsDetailResult(GoodsDetail goodsDetail);
		
		/**
		 * 2.1.5 重量配置
		 * @param options
		 */
		public void requestWeightPriceResult(List<WeightOption> options);
		
		/**
		 * 2.1.6 查询单个类目产品列表
		 * @param itemSaleInfoPage
		 */
		public void requestTypeGoodsListResult(ItemSaleInfoPage itemSaleInfoPage);
		
	}
	
	/**
	 * 2.1.2 全部类目菜单
	 * @param context
	 */
	public void requestGoodsTypes(final Context context){
		RequestParam paramPath = new RequestParam("goods/frontType/list");
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				List<GoodsItemType> goodsItemTypes = gson.fromJson(response, new TypeToken<List<GoodsItemType>>() { }.getType());
				mInterface.requestGoodsTypesResult(goodsItemTypes);
			}
		});
	}
	
	/**
	 * 2.1.3 查询首页产品列表
	 * @param context
	 * @param addressId
	 * @param pageNum
	 * @param pageSize
	 */
	public void requestGoodsList(final Context context, int addressId, int pageNum){
		RequestParam paramPath = new RequestParam("v31/goods/general/list")
		.setParams("addressId", addressId == -1 ? "" : String.valueOf(addressId))
		.setParams("pageNum", pageNum);
		HttpManager.getInstance(context).volleyRequestByPost(paramPath, 
			new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					MarketHomePage marketHomePage = gson.fromJson(response, new TypeToken<MarketHomePage>() { }.getType());
					mInterface.requestGoodsListResult(marketHomePage);
				}
			});
	}
	
	/**
	 * 2.1.4 查询产品明细
	 * @param context
	 * @param goodsId
	 * @param addressId
	 */
	public void requestGoodsDetail(final Context context, int goodsId, int addressId){
		RequestParam paramPath = new RequestParam("goods/general/detail")
		.setParams("goodsId", goodsId)
		.setParams("addressId", addressId == -1 ? "" : String.valueOf(addressId));
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				GoodsDetail goodsDetail = gson.fromJson(response, new TypeToken<GoodsDetail>() { }.getType());
				mInterface.requestGoodsDetailResult(goodsDetail);
			}
		});
	}
	
	/**
	 * 2.1.5 获取重量配置
	 * @param context
	 * @param goodsId
	 */
	public void requestWeightPrice(final Context context, int goodsId){
		RequestParam paramPath = new RequestParam("goods/amount")
		.setParams("goodsId", goodsId);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				List<WeightOption> options = gson.fromJson(response, new TypeToken<List<WeightOption>>() { }.getType());
				mInterface.requestWeightPriceResult(options);
			}
		});
	}
	
	/**
	 * 2.1.6 查询产品列表（单个类目）
	 */
	public void requestTypeGoodsList(final Context context, int frontTypeId, int addressId, int pageNum){
		RequestParam paramPath = new RequestParam("v31/goods/general/typeList")
		.setParams("frontTypeId", frontTypeId)
		.setParams("addressId", addressId)
		.setParams("pageNum", pageNum);
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				ItemSaleInfoPage itemSaleInfoPage = gson.fromJson(response, new TypeToken<ItemSaleInfoPage>() { }.getType());
				mInterface.requestTypeGoodsListResult(itemSaleInfoPage);
			}
		});
	}
}
