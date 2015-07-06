package com.guozha.buy.controller.mine;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.MyAdressListAdapter;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的地址
 * @author PeggyTong
 *
 */
public class MyAddressActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "我的地址";
	
	private static final int REQUEST_CODE = 0;
	
	private static final int HAND_REQUEST_ADDRESS_COMPLETED = 0x0001;  	//请求地址数据完成
	private static final int HAND_REFRESH_LIST_DATA = 0x0002; 			//刷新数据列表
	private static final int HAND_DELETE_COMPLTED = 0x0003;				//删除成功
	
	private ListView mAddressList;
	
	private List<AddressInfo> mAdressInfos = null;
	private View mEmptyView;
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_REQUEST_ADDRESS_COMPLETED:
				updateAddressList();
				break;
			case HAND_REFRESH_LIST_DATA:
				initData();
				break;
			case HAND_DELETE_COMPLTED:
				initData();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_address);
		customActionBarStyle(PAGE_NAME);
		
		initView();
		initData();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mEmptyView = findViewById(R.id.empty_view);
		mAddressList = (ListView) findViewById(R.id.my_address_list);
		int currentAddressId = ConfigManager.getInstance().getChoosedAddressId();
		mAddressList.setAdapter(new MyAdressListAdapter(this, mAdressInfos, currentAddressId));
		
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
		
		mAddressList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final AddressInfo addressInfo = mAdressInfos.get(position);
				if(mAdressInfos.size() <= 1){
					ToastUtil.showToast(MyAddressActivity.this, "不允许地址全部删除");
					return true;
				}
				final CustomDialog dialog = 
						new CustomDialog(MyAddressActivity.this, R.layout.dialog_delete_notify);
				dialog.setDismissButtonId(R.id.cancel_button);
				dialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						deleteOldAddress(addressInfo.getAddressId());
						dialog.dismiss();
					}
				});
				return true;
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
	 * 删除旧的地址
	 */
	private void deleteOldAddress(int addressId){
		String token = ConfigManager.getInstance().getUserToken(this);
		int userId = ConfigManager.getInstance().getUserId();
		if(token == null || userId == -1) return; //TODO 去登录
		mUserModel.requestDeleteAddress(this, userId, token, addressId);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		if(userId == -1){
			ToastUtil.showToast(MyAddressActivity.this, "您的身份验证存在问题");
		}
		mUserModel.requestListAddress(this, userId);
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
		int currentAddressId = ConfigManager.getInstance().getChoosedAddressId();
		mAddressList.setAdapter(new MyAdressListAdapter(this, mAdressInfos, currentAddressId));
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
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestListAddressResult(List<AddressInfo> adressInfos) {
			mAdressInfos = adressInfos;
			mHandler.sendEmptyMessage(HAND_REQUEST_ADDRESS_COMPLETED);
		}
		
		@Override
		public void requestDeleteAddressResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				ConfigManager.getInstance().setChoosedAddressId(mAdressInfos.get(1).getAddressId());
				mHandler.sendEmptyMessage(HAND_DELETE_COMPLTED);
			}else{
				ToastUtil.showToast(MyAddressActivity.this, msg);
			}
		}
	}
}
