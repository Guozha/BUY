package com.guozha.buy.activity.global;

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

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.mpage.CookBookDetailActivity;
import com.guozha.buy.adapter.SearchRelateRecipeListAdapter;
import com.guozha.buy.adapter.SearchResultListAdapter;
import com.guozha.buy.dialog.WeightSelectDialog;
import com.guozha.buy.entry.global.SearchRecipe;
import com.guozha.buy.entry.global.SearchResult;
import com.guozha.buy.entry.market.ItemSaleInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 查询结果界面
 * @author PeggyTong
 *
 */
public class SearchResultActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SearchResultPage";
	
	public static final int SEARCH_FAIL = 0;
	public static final int SEARCH_SUCCESS = 1;
	private static final int HAND_RESULT_DATA_COMPLETED = 0x0001;
	private static final int HAND_RELATE_DATA_COMPLETED = 0x0002;
	
	private GridView mSearchResult;
	private ListView mSearchRelate;
	private BitmapCache mBitmapCache = CustomApplication.getBitmapCache();
	
	private List<ItemSaleInfo> mSearchResultList;		
	private List<SearchRecipe> mSearchRelateList;
	
	private String mKeyWord;
	
	private Intent mIntent;
	
	private TextView mSearchResultCount;
	
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
		customActionBarStyle("搜索结果");
		
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
		mSearchRelate.setAdapter(
				new SearchRelateRecipeListAdapter(this, mSearchRelateList, mBitmapCache));
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
				Intent intent = new Intent(SearchResultActivity.this, WeightSelectDialog.class);
				ItemSaleInfo saleInfo = mSearchResultList.get(position);
				if(saleInfo != null){
					intent.putExtra("goodsId", String.valueOf(saleInfo.getGoodsId()));
					intent.putExtra("unitPrice", String.valueOf(saleInfo.getUnitPrice()));
					intent.putExtra("unit", saleInfo.getUnit());
				}
				startActivity(intent);
			}
		});
		
		mSearchRelate.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SearchResultActivity.this, CookBookDetailActivity.class);
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
		RequestParam paramPath = new RequestParam("search")
		.setParams("word", mKeyWord)
		.setParams("addressId", addressId);
		HttpManager.getInstance(this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					SearchResult searchResult = gson.fromJson(response, new TypeToken<SearchResult>() { }.getType());
					if(searchResult == null){
						return;
					}
					if(searchResult.getGoodsList() == null && searchResult.getMenuList() == null){
						return;
					}
					if(searchResult.getGoodsList() != null){
						mSearchResultList = searchResult.getGoodsList();
						LogUtil.e("mSearchResultSize == " + mSearchResultList.size());
						handler.sendEmptyMessage(HAND_RESULT_DATA_COMPLETED);
					}
					if(searchResult.getMenuList() != null){
						mSearchRelateList = searchResult.getMenuList();
						LogUtil.e("mSearchRecipeSize == " + mSearchRelateList.size());
						handler.sendEmptyMessage(HAND_RELATE_DATA_COMPLETED);
					}
				}
		});
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
}
