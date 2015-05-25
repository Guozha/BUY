package com.guozha.buy.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.entry.mine.address.KeyWord;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.umeng.analytics.MobclickAgent;

/**
 * 选择小区（详细地址）
 * @author PeggyTong
 *
 */
public class ChooseDetailActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "ChooseAddrDetailPage";
	
	public static final String BUNDLE_DATA = "countrys";
	private List<KeyWord> mKeyWords = null;
	private List<String> mShowWords = null;
	
	private static final int HAND_DATA_COMPLETED = 0x0001;
	private ListView mCantonList;	
	private int mCountryId;
	private EditText mDetailText;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				String key = (String)msg.obj;
				if(mCantonList == null || mKeyWords == null) return;
				mShowWords = new ArrayList<String>();
				if(key == null || "".equals(key.trim())){
					for(int i = 0; i < mKeyWords.size(); i++){
						mShowWords.add(mKeyWords.get(i).getBuildingName());
					}
				}else{
					for(int i = 0; i < mKeyWords.size(); i++){
						String keyWords = mKeyWords.get(i).getBuildingName();
						if(keyWords.contains(key)){
							mShowWords.add(keyWords);
						}
					}
				}
				mCantonList.setAdapter(new ArrayAdapter<String>(
						ChooseDetailActivity.this, R.layout.list_canton_item_cell, mShowWords));
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_choose_detail);
		customActionBarStyle("小区、楼宇");

		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mCountryId = bundle.getInt("countryId");
			}
		}
		initView();
		requestAddressBuilding();
		
		setResult(1, null);
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
				intent.putExtra("addrDetail", mKeyWords.get(position).getBuildingName());
				setResult(1, intent);
				ChooseDetailActivity.this.finish();
			}
		});
		
		mDetailText = (EditText) findViewById(R.id.choose_detail_text);
		mDetailText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence charSeq, int start, int before, int count) {
				String text = charSeq.toString();
				Message message = new Message();
				message.what = HAND_DATA_COMPLETED;
				message.obj = text;
				handler.sendMessage(message);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
	}
	
	/**
	 * 请求关键词
	 */
	private void requestAddressBuilding(){
		String token = ConfigManager.getInstance().getUserToken(ChooseDetailActivity.this);
		if(token == null) return;
		RequestParam paramPath = new RequestParam("account/address/listBuilding")
		.setParams("token", token)
		.setParams("countyId", mCountryId);
		HttpManager.getInstance(ChooseDetailActivity.this).volleyRequestByPost(
			HttpManager.URL + paramPath, 
			new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mKeyWords = gson.fromJson(response, new TypeToken<List<KeyWord>>() { }.getType());
					Message message = new Message();
					message.what = HAND_DATA_COMPLETED;
					message.obj = null;
					handler.sendMessage(message);
				}
			});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
