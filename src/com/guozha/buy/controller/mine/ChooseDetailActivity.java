package com.guozha.buy.controller.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.entry.mine.address.KeyWord;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.UserModelResult;
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
	private String mAddrDetail;
	private ImageView mChooseDetailIcon;
	private View mOtherAddr;
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
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
				if(mShowWords.isEmpty()){
					mOtherAddr.setVisibility(View.VISIBLE);
				}else{
					mOtherAddr.setVisibility(View.GONE);
				}
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
				mAddrDetail = bundle.getString("addrDetail");
				if("其他地址".equals(mAddrDetail))
					mAddrDetail = "";
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
				intent.putExtra("addrDetail", mShowWords.get(position));
				setResult(1, intent);
				ChooseDetailActivity.this.finish();
			}
		});
		
		mDetailText = (EditText) findViewById(R.id.choose_detail_text);
		mChooseDetailIcon = (ImageView) findViewById(R.id.choose_detail_clear);
		mChooseDetailIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDetailText.setText("");
			}
		});
		if(mAddrDetail != null && !mAddrDetail.isEmpty()){
			mDetailText.setText(mAddrDetail);
			mDetailText.setSelection(mAddrDetail.length());
			mChooseDetailIcon.setVisibility(View.VISIBLE);
		}
		mDetailText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence charSeq, int start, int before, int count) {
				String text = charSeq.toString();
				if(text.length() == 0){
					mChooseDetailIcon.setVisibility(View.INVISIBLE);
				}else{
					mChooseDetailIcon.setVisibility(View.VISIBLE);
				}
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
		
		mOtherAddr = findViewById(R.id.other_addr_area);
		findViewById(R.id.other_addr_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("addrDetail", "其他地址");
				setResult(1, intent);
				ChooseDetailActivity.this.finish();
			}
		});
	}
	
	/**
	 * 请求关键词
	 */
	private void requestAddressBuilding(){
		String token = ConfigManager.getInstance().getUserToken();
		if(token == null) return;
		mUserModel.requestAddressBuilding(this, token, mCountryId);
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
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestAddressBuilding(List<KeyWord> keyWords) {
			mKeyWords = keyWords;
			Message message = new Message();
			message.what = HAND_DATA_COMPLETED;
			message.obj = null;
			handler.sendMessage(message);
		}
	}
}
