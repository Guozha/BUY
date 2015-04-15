package com.guozha.buy.activity.mine;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.MyAdressListAdapter;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的地址
 * @author PeggyTong
 *
 */
public class MyAddressActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "AddressListPage";
	
	private static final int REQUEST_CODE = 0;
	
	private static final int HAND_REQUEST_ADDRESS_COMPLETED = 0x0001;  	//请求地址数据完成
	private static final int HAND_REFRESH_LIST_DATA = 0x0002; 			//刷新数据列表
	
	private ListView mAddressList;
	
	private List<AddressInfo> mAdressInfos = null;
	private View mEmptyView;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_REQUEST_ADDRESS_COMPLETED:
				updateAddressList();
				break;
			case HAND_REFRESH_LIST_DATA:
				initData();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_address);
		customActionBarStyle("我的地址");
		
		initView();
		initData();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mEmptyView = findViewById(R.id.empty_view);
		mAddressList = (ListView) findViewById(R.id.my_address_list);
		mAddressList.setAdapter(new MyAdressListAdapter(this, mAdressInfos));
		
		mAddressList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AddressInfo addressInfo = mAdressInfos.get(position);
				Intent intent = new Intent(MyAddressActivity.this, AddAddressActivity.class);
				intent.putExtra("addressInfo", addressInfo);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		
		findViewById(R.id.myaddress_add_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyAddressActivity.this, AddAddressActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		if(userId == -1){
			ToastUtil.showToast(MyAddressActivity.this, "您的身份验证存在问题");
		}
		RequestParam paramPath = new RequestParam("account/address/list")
		.setParams("userId", userId);
		HttpManager.getInstance(MyAddressActivity.this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mAdressInfos = gson.fromJson(response, 
							new TypeToken<List<AddressInfo>>() { }.getType());
					mHandler.sendEmptyMessage(HAND_REQUEST_ADDRESS_COMPLETED);
				}
		});
	}
	
	/**
	 * 更新地址列表
	 */
	private void updateAddressList(){
		if(mAddressList == null || mEmptyView == null) return;
		if(mAdressInfos == null || mAdressInfos.isEmpty()){
			mAddressList.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.VISIBLE);
		}else{
			mEmptyView.setVisibility(View.GONE);
			mAddressList.setVisibility(View.VISIBLE);
		}
		mAddressList.setAdapter(new MyAdressListAdapter(this, mAdressInfos));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(REQUEST_CODE == requestCode){
			mHandler.sendEmptyMessage(HAND_REFRESH_LIST_DATA);
		}
		super.onActivityResult(requestCode, resultCode, data);
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
