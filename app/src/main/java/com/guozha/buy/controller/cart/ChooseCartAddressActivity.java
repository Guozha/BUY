package com.guozha.buy.controller.cart;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.ChooseAddressAdapter;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.UserModelResult;

public class ChooseCartAddressActivity extends BaseActivity{
	private static final int HAND_ADDRESS_COMPLETED = 0x0001;
	private ListView mAddressList;
	private List<AddressInfo> mAddressInfo = new ArrayList<AddressInfo>();
	private ChooseAddressAdapter mChooseAddressAdapter;
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_ADDRESS_COMPLETED:
				mChooseAddressAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_cartaddress);
		customActionBarStyle("更换地址");
		initView();
		initData();
	}
	
	private void initView(){
		mAddressList = (ListView) findViewById(R.id.address_list);
		mChooseAddressAdapter = new ChooseAddressAdapter(this, mAddressInfo);
		mAddressList.setAdapter(mChooseAddressAdapter);
		mAddressList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AddressInfo addressInfo = mAddressInfo.get(position);
				ConfigManager.getInstance().setChoosedAddressId(addressInfo.getAddressId());
				ChooseCartAddressActivity.this.finish();
			}
		});
	}
	
	private void initData(){
		mAddressInfo.clear();
		int userId = ConfigManager.getInstance().getUserId();
		mUserModel.requestListAddress(this, userId);
	}
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestListAddressResult(List<AddressInfo> addressInfos) {
			if(addressInfos == null) return;
			mAddressInfo.addAll(addressInfos);
			mHandler.sendEmptyMessage(HAND_ADDRESS_COMPLETED);
		}
	}
}
