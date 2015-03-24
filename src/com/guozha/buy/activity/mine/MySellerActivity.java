package com.guozha.buy.activity.mine;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.MySellerListAdapter;
import com.guozha.buy.entry.mine.seller.Seller;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的卖家
 * @author PeggyTong
 *
 */
public class MySellerActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "MySellerPage";
	
	private static final int HAND_SELLER_LIST_COMPLETED = 0x0001; //数据请求完毕
	
	private ListView mMySellerList;
	
	private List<Seller> mSellers;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_SELLER_LIST_COMPLETED:
				if(mMySellerList == null) return;
				mMySellerList.setAdapter(new MySellerListAdapter(MySellerActivity.this, mSellers));
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_seller);
		customActionBarStyle("常用卖家");
		
		mMySellerList = (ListView) findViewById(R.id.my_seller_list);
		
		initData();
	}
	
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		HttpManager.getInstance(MySellerActivity.this).volleyRequestByPost(
			HttpManager.URL + "account/myseller/list?userId=" + userId, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mSellers = gson.fromJson(response, new TypeToken<List<Seller>>() { }.getType());
					for(int i = 0; i < mSellers.size(); i++){
						Seller seller = mSellers.get(i);
						LogUtil.e("sellerName = " + seller.getSellerName());
						LogUtil.e("transCount = " + seller.getTransCount());
					}
					handler.sendEmptyMessage(HAND_SELLER_LIST_COMPLETED);
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
		
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
}
