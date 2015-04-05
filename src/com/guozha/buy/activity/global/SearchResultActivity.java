package com.guozha.buy.activity.global;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.entry.cart.CartTotalData;
import com.guozha.buy.entry.global.SearchResult;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;

/**
 * 查询结果界面
 * @author PeggyTong
 *
 */
public class SearchResultActivity extends BaseActivity{
	
	private GridView mSearchResult;
	private ListView mSearchRelate;
	
	private String mKeyWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		customActionBarStyle("搜索结果");
		
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mKeyWord = bundle.getString("SearchkeyWord");
			}
		}
		initView();
		initSearchedData();
	}
	
	/**
	 * 初始化界面
	 */
	private void initView(){
		mSearchResult = (GridView) findViewById(R.id.search_result_list);
		mSearchRelate = (ListView) findViewById(R.id.search_relate_recipe_list);
		
		
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
				}
		});
	}
}
