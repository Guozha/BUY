package com.guozha.buy.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.entry.mine.address.Country;
import com.guozha.buy.global.net.HttpManager;

/**
 * 选择行政区
 * @author PeggyTong
 *
 */
public class ChooseCantonActivity extends BaseActivity{
	
	public static final String BUNDLE_DATA = "countrys";
	
	private static final int HAND_AREA_COMPLETED = 0x0001;
	private List<Country> mCountrys;
	private ListView mCantonList;	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_AREA_COMPLETED:
				if(mCantonList == null) return;
				List<String> cantons = new ArrayList<String>();
				for(int i = 0; i < mCountrys.size(); i++){
					cantons.add(mCountrys.get(i).getAreaName());
				}
				mCantonList.setAdapter(new ArrayAdapter<String>(
						ChooseCantonActivity.this, R.layout.list_canton_item_cell, cantons));
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_choose_canton);
		customActionBarStyle("行政区");

		initView();
		requestCountryList();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		mCantonList = (ListView) findViewById(R.id.canton_item_list);
		mCantonList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = getIntent();
				intent.putExtra("areaId", mCountrys.get(position).getAreaId());
				intent.putExtra("areaName", mCountrys.get(position).getAreaName());
				setResult(0, intent);
				ChooseCantonActivity.this.finish();
			}
		});
	}
	
	/**
	 * 请求区列表
	 */
	private void requestCountryList(){
		HttpManager.getInstance(ChooseCantonActivity.this).volleyRequestByPost(
			HttpManager.URL + "account/address/listArea?parentAreaId=2" , 
			new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mCountrys = gson.fromJson(response, new TypeToken<List<Country>>() { }.getType());
					handler.sendEmptyMessage(HAND_AREA_COMPLETED);
				}
			});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
