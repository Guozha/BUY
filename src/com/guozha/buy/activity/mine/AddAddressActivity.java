package com.guozha.buy.activity.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.dialog.CustomDialog;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.entry.mine.address.Country;
import com.guozha.buy.entry.mine.address.KeyWord;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.HttpUtil;
import com.guozha.buy.util.LogUtil;
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
	
	private int mCountryId;
	
	private int defaultFlag = 0;  //是否默认地址
	
	private List<Country> mCountrys = null;
	private List<KeyWord> mKeyWords = null;
	private AddressInfo mAddressInfo = null;
	
	private Button mRequestAddButton;
	
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
		if(mAddressInfo != null){
			mReceiveName.setText(mAddressInfo.getReceiveName());
			mMobileNum.setText(mAddressInfo.getMobileNo());
			mAddressCity.setText(mAddressInfo.getCityName());
			mAddressCountry.setText(mAddressInfo.getCountyName());
			mAddressDetai.setText(mAddressInfo.getDetailAddr());
			mRequestAddButton.setText("删除");
		}else{
			mRequestAddButton.setText("添加");
		}
		
		findViewById(R.id.add_canton_button).setOnClickListener(this);
		findViewById(R.id.add_city_button).setOnClickListener(this);
		
		mRequestAddButton.setOnClickListener(this);
		findViewById(R.id.add_address_setting_default).setOnClickListener(this);
		
	}
	
	/**
	 * 请求关键词
	 */
	private void requestAddressBuilding(){
		String token = ConfigManager.getInstance().getUserToken();
		LogUtil.e("countyId = " + mCountryId);
		LogUtil.e("token = " + token);
		HttpManager.getInstance(AddAddressActivity.this).volleyRequestByPost(
			HttpManager.URL + "account/address/listBuilding?token=" + token + "&&countyId=" + mCountryId , 
			new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mKeyWords = gson.fromJson(response, new TypeToken<List<KeyWord>>() { }.getType());
					LogUtil.e("keyWords = " + mKeyWords.toString());
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
			Intent intent = new Intent(AddAddressActivity.this, ChooseCantonActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
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
				CustomDialog dialog = 
						new CustomDialog(AddAddressActivity.this, R.layout.dialog_delete_notify);
				dialog.setDismissButtonId(R.id.cancel_button);
				dialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						handler.sendEmptyMessage(HAND_DELETE_OLD_ADDRESS);
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

	/**
	 * 请求添加地址
	 */
	private void requestAddAddress() {
		int userId = ConfigManager.getInstance().getUserId();
		String userToken = ConfigManager.getInstance().getUserToken();
		if(userId == -1 || userToken == null){
			ToastUtil.showToast(AddAddressActivity.this, "你的用户信息不正确，提交失败");
			return;
		}
		Map<String, String> params;
		String paramPath;
		params = new HashMap<String, String>();
		params.put("token", userToken);
		params.put("userId", String.valueOf(userId));
		params.put("receiveName", mReceiveName.getText().toString());
		params.put("mobileNo", mMobileNum.getText().toString());
		params.put("provinceId", "1");
		params.put("cityId", "2");
		params.put("countyId", String.valueOf(mCountryId));
		params.put("buildingid", "0");
		params.put("buildingName", mAddressCountry.getText().toString());
		params.put("detailAddr", mAddressDetai.getText().toString()
				+ mAddressDetaiNum.getText().toString());
		params.put("defaultFlag", String.valueOf(defaultFlag));
		paramPath = "account/address/insert" + HttpUtil.generatedAddress(params);
		HttpManager.getInstance(
			AddAddressActivity.this).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							String returnCode = response.getString("returnCode");
							if("1".equals(returnCode)){
								ToastUtil.showToast(AddAddressActivity.this, "成功处理");
								if(mAddressInfo != null){
									//请求删除旧的（这个相当于修改）
									//handler.sendEmptyMessage(HAND_DELETE_OLD_ADDRESS);
								}else{
									handler.sendEmptyMessage(HAND_FINISH_WINDOW);
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
	 * 删除旧的地址
	 */
	private void deleteOldAddress(){
		String token = ConfigManager.getInstance().getUserToken();
		if(token == null) return;
		HttpManager.getInstance(AddAddressActivity.this).volleyJsonRequestByPost(
			HttpManager.URL + "account/address/delete?token=" + token + "&&addressId=" + mAddressInfo.getAddressId(), 
			new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					//相当于修改，没有判断的必要
					//String returnCode = response.getString("returnCode");
					handler.sendEmptyMessage(HAND_FINISH_WINDOW);
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
		if(detailAddr.length() < 3){
			ToastUtil.showToast(AddAddressActivity.this, "详细地址不能少于3个字");
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
		String token = ConfigManager.getInstance().getUserToken();
		int userId = ConfigManager.getInstance().getUserId();
		if(token == null){
			ToastUtil.showToast(AddAddressActivity.this, "修改失败，你的登录过期了");
			return;
		}
		HttpManager.getInstance(AddAddressActivity.this).volleyJsonRequestByPost(
			HttpManager.URL + "account/address/default?token="
				+ token + "&&addressId=" + mAddressInfo.getAddressId()
				+ "&&userId=" + userId, new Listener<JSONObject>() {
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
