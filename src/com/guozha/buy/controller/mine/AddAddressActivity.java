package com.guozha.buy.controller.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.UserModelResult;
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
	
	private static final String PAGE_NAME = "添加地址";
	
	private static final int HAND_KEYWORD_COMPLETED = 0x0002;  //关键字获取成功
	private static final int HAND_FINISH_WINDOW = 0x0004; //退出
	private static final int HAND_ADD_ADDR_SUCCESS = 0x0005;
	private static final int REQUEST_CODE = 0;
	
	private EditText mReceiveName;   //收货人
	private EditText mMobileNum;	 //电话
	private TextView mAddressCity;   //城市
	private TextView mAddressCountry; //区
	//private AutoCompleteTextView mAddressDetai;  
	private TextView  mAddressDetai;  //小区，楼宇
	private EditText mAddressDetaiNum; //门牌号
	
	private ImageView mAddressCountryIcon;
	private ImageView mAddressCityIcon;
	private ImageView mAddressDetailIcon;
	
	private int mCountryId = -1;
	
	private int defaultFlag = 0;  //是否默认地址
	
	private AddressInfo mAddressInfo = null;
	
	private Button mRequestAddButton;
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
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
		customActionBarStyle(PAGE_NAME);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mAddressInfo = 
					(AddressInfo) bundle.getSerializable("addressInfo");
			if(mAddressInfo != null){
				mCountryId = mAddressInfo.getCountyId();
			}
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
		mAddressDetai = (TextView) findViewById(R.id.add_address_detailaddr);
		mAddressDetaiNum = (EditText) findViewById(R.id.add_address_detail_number);
		mRequestAddButton = (Button) findViewById(R.id.add_address_request_button);
		
		mAddressCityIcon = (ImageView) findViewById(R.id.add_address_city_icon);
		mAddressCountryIcon = (ImageView) findViewById(R.id.add_address_country_icon);
		mAddressDetailIcon = (ImageView) findViewById(R.id.add_address_detailaddr_icon);
		
		if(mAddressInfo != null){
			mReceiveName.setText(mAddressInfo.getReceiveName());
			mMobileNum.setText(mAddressInfo.getMobileNo());
			mAddressCity.setText(mAddressInfo.getCityName());
			mAddressCountry.setText(mAddressInfo.getCountyName());
			mAddressDetai.setText(mAddressInfo.getBuildingName());
			mAddressDetaiNum.setText(mAddressInfo.getDetailAddr());
			mRequestAddButton.setText("修改");
		}else{
			String mobileNum = ConfigManager.getInstance().getMobileNum();
			if(mobileNum != null){
				mMobileNum.setText(mobileNum);
			}
			mRequestAddButton.setText("添加");
		}
		findViewById(R.id.add_canton_button).setOnClickListener(this);
		findViewById(R.id.add_city_button).setOnClickListener(this);
		mAddressDetai.setOnClickListener(this);
		mRequestAddButton.setOnClickListener(this);
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
		case R.id.add_address_detailaddr:  //请求小区、楼宇
			if(mCountryId == -1){
				ToastUtil.showToast(AddAddressActivity.this, "请先选择行政区");
				return;
			}
			Intent intent = new Intent(AddAddressActivity.this, ChooseDetailActivity.class);
			intent.putExtra("countryId", mCountryId);
			intent.putExtra("addrDetail", mAddressDetai.getText());
			startActivityForResult(intent, REQUEST_CODE);
			break;
		case R.id.add_address_request_button:
			if(mAddressInfo != null){//修改地址
				if(!valideRequestAddress()) return;
				requestModefyAddress();
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
	 * 请求修改地址
	 */
	private void requestModefyAddress(){
		if(mAddressInfo == null) return;
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(this);
		if(token == null) return;
		mUserModel.requestModefyAddress(this, token, mAddressInfo.getAddressId(), 
				userId, mCountryId, mReceiveName.getText().toString(), mMobileNum.getText().toString(), mAddressDetai.getText().toString(), mAddressDetaiNum.getText().toString());
	}
	/**
	 * 请求添加地址
	 */
	private void requestAddAddress() {
		int userId = ConfigManager.getInstance().getUserId();
		String userToken = ConfigManager.getInstance().getUserToken(this);
		if(userToken == null) return;
		mUserModel.requestAddAddress(this, userToken, userId, mReceiveName.getText().toString(), 
				mMobileNum.getText().toString(), mCountryId, mAddressDetai.getText().toString(), 
				mAddressDetaiNum.getText().toString(), String.valueOf(defaultFlag));
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
			ToastUtil.showToast(AddAddressActivity.this, "请选择行政区");
			return false;
		}
		String detailAddr = mAddressDetai.getText().toString();
		if(detailAddr.isEmpty()){
			ToastUtil.showToast(AddAddressActivity.this, "请选择小区/楼宇");
			return false;
		}
		String detailAddrNum = mAddressDetaiNum.getText().toString();
		if(detailAddrNum.isEmpty()){
			ToastUtil.showToast(AddAddressActivity.this, "请填写详细地址和门牌号");
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE && data != null){
			Bundle bundle = data.getExtras();
			if(resultCode == 0){
				mCountryId = bundle.getInt("areaId");
				String areaName = bundle.getString("areaName");
				mAddressCountry.setText(areaName);
				mAddressDetai.setText("");
				mAddressDetaiNum.setText("");
			}else if(resultCode == 1){
				LogUtil.e("onActivityResult");
				String detail = bundle.getString("addrDetail");
				mAddressDetai.setText(detail);
				mAddressDetaiNum.setText("");
				mAddressDetaiNum.requestFocus();
			}
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
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestAddAddressResult(String returnCode,
				String buildFlag, String msg, int addressId) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				if("0".equals(buildFlag)){//未覆盖
					showNotCoveredDialog();
				}else{
					ToastUtil.showToast(AddAddressActivity.this, "成功添加");
					ConfigManager.getInstance().setChoosedAddressId(addressId);
					handler.sendEmptyMessage(HAND_FINISH_WINDOW);
				}
			}else{
				ToastUtil.showToast(AddAddressActivity.this, msg);
			}
		}
		
		@Override
		public void requestModefyAddressResult(String returnCode, String msg,
				int addressId, String buildFlag) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
					if("0".equals(buildFlag)){//未覆盖
						showNotCoveredDialog();
					}else{
						ToastUtil.showToast(AddAddressActivity.this, "成功修改");
						ConfigManager.getInstance().setChoosedAddressId(addressId);
						handler.sendEmptyMessage(HAND_FINISH_WINDOW);
					}
			}else{
				ToastUtil.showToast(AddAddressActivity.this, msg);
			}
		}
	}
}