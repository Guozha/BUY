package com.guozha.buy.controller.mine;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.entry.mine.address.KeyWord;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.RegularUtil;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 添加地址
 * @author PeggyTong
 *
 */
public class AddAddressActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "AddAddressPage";
	
	private static final int HAND_CHOOSED_COUNTRY = 0x0001;  //选择了区
	private static final int HAND_KEYWORD_COMPLETED = 0x0002;  //关键字获取成功
	private static final int HAND_DELETE_OLD_ADDRESS = 0x0003; //删除就的地址
	private static final int HAND_FINISH_WINDOW = 0x0004; //退出
	private static final int REQUEST_CODE = 0;
	
	private EditText mReceiveName;   //收货人
	private EditText mMobileNum;	 //电话
	private TextView mAddressCity;   //城市
	private TextView mAddressCountry; //区
	private AutoCompleteTextView mAddressDetai;  //详细地址
	private EditText mAddressDetaiNum; //门牌号
	
	private ImageView mAddressCountryIcon;
	private ImageView mAddressCityIcon;
	
	private int mCountryId = -1;
	
	private int defaultFlag = 0;  //是否默认地址
	
	private List<KeyWord> mKeyWords = null;
	private AddressInfo mAddressInfo = null;
	
	private Button mRequestAddButton;
	private Button mRequestSettingDefaultButton;
	
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_CHOOSED_COUNTRY:
				//请求关键词
				requestAddressBuilding();
				break;
			case HAND_KEYWORD_COMPLETED:
				//添加关键字
				addKeyWordToText();
				break;
			case HAND_DELETE_OLD_ADDRESS:
				//删除旧地址
				deleteOldAddress();
				break;
			case HAND_FINISH_WINDOW:
				setResult(0);
				AddAddressActivity.this.finish();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		customActionBarStyle("添加地址");
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mAddressInfo = 
					(AddressInfo) bundle.getSerializable("addressInfo");
		}
		initView();
		MainPageInitDataManager.mAddressUpdated = true; //设置地址列表发生了变化
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		mReceiveName = (EditText) findViewById(R.id.add_address_receivename);
		mMobileNum = (EditText) findViewById(R.id.add_address_mobileno);
		mAddressCity = (TextView) findViewById(R.id.add_address_city);
		mAddressCountry = (TextView) findViewById(R.id.add_address_country);
		mAddressDetai = (AutoCompleteTextView) findViewById(R.id.add_address_detailaddr);
		mAddressDetaiNum = (EditText) findViewById(R.id.add_address_detail_number);
		mRequestAddButton = (Button) findViewById(R.id.add_address_request_button);
		mRequestSettingDefaultButton = (Button) findViewById(R.id.add_address_setting_default);
		
		mAddressCityIcon = (ImageView) findViewById(R.id.add_address_city_icon);
		mAddressCountryIcon = (ImageView) findViewById(R.id.add_address_country_icon);
		
		if(mAddressInfo != null){
			mReceiveName.setText(mAddressInfo.getReceiveName());
			mMobileNum.setText(mAddressInfo.getMobileNo());
			mAddressCity.setText(mAddressInfo.getCityName());
			mAddressCountry.setText(mAddressInfo.getCountyName());
			mAddressDetai.setText(mAddressInfo.getBuildingName());
			mAddressDetaiNum.setText(mAddressInfo.getDetailAddr());
			mReceiveName.setEnabled(false);
			mMobileNum.setEnabled(false);
			mAddressCity.setEnabled(false);
			mAddressCountry.setEnabled(false);
			mAddressDetai.setEnabled(false);
			mAddressDetaiNum.setEnabled(false);
			mRequestAddButton.setText("删除");
			mRequestSettingDefaultButton.setVisibility(View.VISIBLE);
			mAddressCityIcon.setVisibility(View.INVISIBLE);
			mAddressCountryIcon.setVisibility(View.INVISIBLE);
			mReceiveName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			mMobileNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			mAddressDetai.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			mAddressDetaiNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}else{
			findViewById(R.id.add_canton_button).setOnClickListener(this);
			findViewById(R.id.add_city_button).setOnClickListener(this);
			mRequestAddButton.setText("添加");
			mRequestSettingDefaultButton.setVisibility(View.GONE);
			mAddressDetai.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				@Override
				public void afterTextChanged(Editable s) {
					if(mCountryId == -1){
						ToastUtil.showToast(AddAddressActivity.this, "请先选择所在区");
						requestCatonList();
					}
				}
			});
		}
		
		mRequestAddButton.setOnClickListener(this);
		mRequestSettingDefaultButton.setOnClickListener(this);
	}
	
	/**
	 * 请求关键词
	 */
	private void requestAddressBuilding(){
		String token = ConfigManager.getInstance().getUserToken(AddAddressActivity.this);
		if(token == null) return;
		RequestParam paramPath = new RequestParam("account/address/listBuilding")
		.setParams("token", token)
		.setParams("countyId", mCountryId);
		HttpManager.getInstance(AddAddressActivity.this).volleyRequestByPost(
			HttpManager.URL + paramPath, 
			new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mKeyWords = gson.fromJson(response, new TypeToken<List<KeyWord>>() { }.getType());
					handler.sendEmptyMessage(HAND_KEYWORD_COMPLETED);
				}
			});
	}
	
	/**
	 * 添加关键字
	 */
	private void addKeyWordToText(){
		List<String> keyWords = new ArrayList<String>();
		for(int i = 0; i < mKeyWords.size(); i++){
			keyWords.add(mKeyWords.get(i).getBuildingName());
		}
		mAddressDetai.setAdapter(new ArrayAdapter<String>(
				AddAddressActivity.this, R.layout.list_keyword_item_cell, keyWords));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add_city_button:     //请求市级列表
			mAddressCity.setText("杭州市");
			ToastUtil.showToast(AddAddressActivity.this, "抱歉，目前只支持杭州地区");
			break;
		case R.id.add_canton_button:   //请求区列表
			requestCatonList();
			break;
		case R.id.add_address_setting_default: //设为默认
			if(valideRequestAddress()){
				if(mAddressInfo == null){
					defaultFlag = 1;
					ToastUtil.showToast(AddAddressActivity.this, "设置默认成功");
				}else{
					requestDefaultAddress();
				}
			}
			break;
		case R.id.add_address_request_button:
			if(mAddressInfo != null){
				final CustomDialog dialog = 
						new CustomDialog(AddAddressActivity.this, R.layout.dialog_delete_notify);
				dialog.setDismissButtonId(R.id.cancel_button);
				dialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						handler.sendEmptyMessage(HAND_DELETE_OLD_ADDRESS);
						dialog.dismiss();
					}
				});
				return;
			}
			if(!valideRequestAddress()) return;
			//请求添加地址
			requestAddAddress();
			break;
		}
	}

	private void requestCatonList() {
		Intent intent = new Intent(AddAddressActivity.this, ChooseCantonActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}

	/**
	 * 请求添加地址
	 */
	private void requestAddAddress() {
		int userId = ConfigManager.getInstance().getUserId();
		String userToken = ConfigManager.getInstance().getUserToken(AddAddressActivity.this);
		if(userId == -1 || userToken == null){
			return;
		}
		RequestParam paramPath = new RequestParam("account/address/insert")
		.setParams("token", userToken)
		.setParams("userId", String.valueOf(userId))
		.setParams("receiveName", mReceiveName.getText().toString())
		.setParams("mobileNo", mMobileNum.getText().toString())
		.setParams("provinceId", "1")
		.setParams("cityId", "2")
		.setParams("countyId", String.valueOf(mCountryId))
		.setParams("buildingName", mAddressDetai.getText().toString())
		.setParams("detailAddr", mAddressDetaiNum.getText().toString())
		.setParams("defaultFlag", String.valueOf(defaultFlag));
		HttpManager.getInstance(
			AddAddressActivity.this).volleyJsonRequestByGet(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							String returnCode = response.getString("returnCode");
							if("1".equals(returnCode)){
								String buildingFlag = response.getString("buildingFlag");
								
								if(mAddressInfo != null){
									//请求删除旧的（这个相当于修改）
									//handler.sendEmptyMessage(HAND_DELETE_OLD_ADDRESS);
									ToastUtil.showToast(AddAddressActivity.this, "成功处理");
								}else{
									if("0".equals(buildingFlag)){//未覆盖
										showNotCoveredDialog();
									}else{
										ToastUtil.showToast(AddAddressActivity.this, "成功添加");
										handler.sendEmptyMessage(HAND_FINISH_WINDOW);
									}
								}
								
								
							}else{
								ToastUtil.showToast(AddAddressActivity.this, "访问服务器异常");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	/**
	 * 显示没有覆盖提示框
	 */
	private void showNotCoveredDialog(){
		final CustomDialog dialog = new CustomDialog(this, R.layout.dialog_not_covered);
		dialog.getViewById(R.id.cancel_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				handler.sendEmptyMessage(HAND_FINISH_WINDOW);
			}
		});
		dialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				handler.sendEmptyMessage(HAND_FINISH_WINDOW);
			}
		});
	}
	
	/**
	 * 删除旧的地址
	 */
	private void deleteOldAddress(){
		String token = ConfigManager.getInstance().getUserToken(AddAddressActivity.this);
		int userId = ConfigManager.getInstance().getUserId();
		if(token == null || userId == -1) return;
		RequestParam paramPath = new RequestParam("account/address/delete")
		.setParams("userId", userId)
		.setParams("token", token)
		.setParams("addressId", mAddressInfo.getAddressId());
		HttpManager.getInstance(AddAddressActivity.this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, 
			new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					//相当于修改，没有判断的必要
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							handler.sendEmptyMessage(HAND_FINISH_WINDOW);
						}else{
							ToastUtil.showToast(AddAddressActivity.this, "删除失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * 验证请求的地址信息
	 */
	private boolean valideRequestAddress(){
		String receiveName = mReceiveName.getText().toString();
		if(receiveName.isEmpty()){
			ToastUtil.showToast(AddAddressActivity.this, "请填写收货人");
			return false;
		}
		String mobileNum = mMobileNum.getText().toString();
		if(!isValidatePhoneNum(mobileNum)){
			ToastUtil.showToast(AddAddressActivity.this, "手机号码格式不正确");
			return false;
		}
		String country = mAddressCountry.getText().toString();
		if(country.isEmpty()){
			ToastUtil.showToast(AddAddressActivity.this, "请选择所在区");
			return false;
		}
		String detailAddr = mAddressDetai.getText().toString();
		if(detailAddr.isEmpty()){
			ToastUtil.showToast(AddAddressActivity.this, "请填写详细地址");
			return false;
		}
		String detailAddrNum = mAddressDetaiNum.getText().toString();
		if(detailAddrNum.isEmpty()){
			ToastUtil.showToast(AddAddressActivity.this, "请填写门牌号");
			return false;
		}
		return true;
	}
	
	/**
	 * 判断输入的手机号码是否合格
	 * @param phoneNum
	 */
	private boolean isValidatePhoneNum(String phoneNum) {
		if(phoneNum.isEmpty() || 
				!RegularUtil.regularPhoneNum(phoneNum.trim())){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 请求设置为默认地址
	 */
	private void requestDefaultAddress(){
		String token = ConfigManager.getInstance().getUserToken(AddAddressActivity.this);
		int userId = ConfigManager.getInstance().getUserId();
		if(token == null){
			return;
		}
		RequestParam paramPath = new RequestParam("account/address/default")
		.setParams("token", token)
		.setParams("addressId", mAddressInfo.getAddressId())
		.setParams("userId", userId);
		HttpManager.getInstance(AddAddressActivity.this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							String returnCode = response.getString("returnCode");
							if("1".equals(returnCode)){
								ToastUtil.showToast(AddAddressActivity.this, "默认地址已修改");
							}else{
								ToastUtil.showToast(AddAddressActivity.this, "修改失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE && data != null){
			Bundle bundle = data.getExtras();
			mCountryId = bundle.getInt("areaId");
			String areaName = bundle.getString("areaName");
			mAddressCountry.setText(areaName);
			mAddressDetai.setText("");
			handler.sendEmptyMessage(HAND_CHOOSED_COUNTRY);
			ToastUtil.showToast(AddAddressActivity.this, areaName);
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