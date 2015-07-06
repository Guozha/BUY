package com.guozha.buy.controller;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.CookBookListAdapter;
import com.guozha.buy.adapter.SearchResultListAdapter;
import com.guozha.buy.controller.dialog.WeightSelectDialog;
import com.guozha.buy.controller.found.MenuDetailActivity;
import com.guozha.buy.entry.global.SearchResult;
import com.guozha.buy.entry.market.ItemSaleInfo;
import com.guozha.buy.entry.market.RelationRecipe;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.SystemModel;
import com.guozha.buy.model.result.SystemModelResult;
import com.umeng.analytics.MobclickAgent;

/**
 * 查询结果界面
 * @author PeggyTong
 *
 */
public class SearchResultActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "搜索结果";
	
	public static final int SEARCH_FAIL = 0;
	public static final int SEARCH_SUCCESS = 1;
	private static final int HAND_RESULT_DATA_COMPLETED = 0x0001;
	private static final int HAND_RELATE_DATA_COMPLETED = 0x0002;
	
	private GridView mSearchResult;
	private ListView mSearchRelate;
	private BitmapCache mBitmapCache = BitmapCache.getInstance();
	
	private List<ItemSaleInfo> mSearchResultList;		
	private List<RelationRecipe> mSearchRelateList;
	
	private String mKeyWord;
	
	private Intent mIntent;
	
	private TextView mSearchResultCount;
	
	private SystemModel mSystemModel = new SystemModel(new MySystemModelResult());
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_RESULT_DATA_COMPLETED:
				updateSearchResultView();
				break;
			case HAND_RELATE_DATA_COMPLETED:
				updateSearchRelateView();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		customActionBarStyle(PAGE_NAME);
		
		mIntent = getIntent();
		if(mIntent != null){
			Bundle bundle = mIntent.getExtras();
			if(bundle != null){
				mKeyWord = bundle.getString("KeyWord");
			}
		}
		initView();
		initSearchedData();
	}
	
	/**
	 * 更新查询结果列表
	 */
	private void updateSearchResultView(){
		if(mSearchResult == null) return;
		mSearchResultCount.setText("共搜索到" + mSearchResultList.size() + "个结果");
		mSearchResult.setAdapter(
				new SearchResultListAdapter(this, mSearchResultList, mBitmapCache));
	}
	
	/**
	 * 更新查询相关菜谱列表
	 */
	private void updateSearchRelateView(){
		if(mSearchRelate == null) return;
		//mSearchRelate.setAdapter(
		//		new SearchRelateRecipeListAdapter(this, mSearchRelateList, mBitmapCache));
		mSearchRelate.setAdapter(new CookBookListAdapter(this, mSearchRelateList, mBitmapCache));
	}
	
	/**
	 * 初始化界面
	 */
	private void initView(){
		mSearchResult = (GridView) findViewById(R.id.search_result_list);
		mSearchRelate = (ListView) findViewById(R.id.search_relate_recipe_list);
		mSearchResultCount = (TextView) findViewById(R.id.search_result_count);
		
		mSearchResult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String token = ConfigManager.getInstance().getUserToken(SearchResultActivity.this);
				if(token == null) return;
				int addressId = ConfigManager.getInstance().getChoosedAddressId(SearchResultActivity.this);
				if(addressId == -1) return;
				Intent intent = new Intent(SearchResultActivity.this, WeightSelectDialog.class);
				ItemSaleInfo saleInfo = mSearchResultList.get(position);
				if(saleInfo != null){
					int price = "1".equals(saleInfo.getBargainFlag()) ? saleInfo.getBargainUnitPrice() : saleInfo.getUnitPrice();
					intent.putExtra("goodsId", String.valueOf(saleInfo.getGoodsId()));
					intent.putExtra("unitPrice", String.valueOf(price));
					intent.putExtra("unit", saleInfo.getUnit());
				}
				startActivity(intent);
			}
		});
		
		mSearchRelate.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SearchResultActivity.this, MenuDetailActivity.class);
				intent.putExtra("menuId", mSearchRelateList.get(position).getMenuId());
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initSearchedData(){
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		mSystemModel.requestSystemSearched(this, mKeyWord, addressId);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//友盟界面统计
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
	
	class MySystemModelResult extends SystemModelResult{
		@Override
		public void requestSystemSearch(SearchResult searchResult) {
			if(searchResult == null){
				return;
			}
			if(searchResult.getGoodsList() == null && searchResult.getMenuList() == null){
				return;
			}
			if(searchResult.getGoodsList() != null){
				mSearchResultList = searchResult.getGoodsList();
				handler.sendEmptyMessage(HAND_RESULT_DATA_COMPLETED);
			}
			if(searchResult.getMenuList() != null){
				mSearchRelateList = searchResult.getMenuList();
				handler.sendEmptyMessage(HAND_RELATE_DATA_COMPLETED);
			}
		}
	}
}
