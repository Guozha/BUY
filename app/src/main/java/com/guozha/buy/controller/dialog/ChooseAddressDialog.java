package com.guozha.buy.controller.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.ChooseAddressListAdapter;
import com.guozha.buy.controller.mine.AddAddressActivity;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.UserModelResult;

/**
 * 选择地址对话框
 * @author PeggyTong
 *
 */
public class ChooseAddressDialog extends Activity{
	
	private static final int HAND_ADDRESS_COMPLTED = 0x0001;
	
	private ListView mChooseAddressList;
	private TextView mAddNewAddress;
	private List<AddressInfo> mAddressInfos = new ArrayList<AddressInfo>();
	private ChooseAddressListAdapter mAddressListAdapter;
	private UserModel mUserModel = new UserModel(new MyUserModelResult());

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_ADDRESS_COMPLTED:
				mAddressListAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_choose_address);
		//让Dialog全屏
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setResult(0);
		initView();
		initData();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mChooseAddressList = (ListView) findViewById(R.id.choose_address_list);
		mChooseAddressList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mAddressInfos != null){
					int addressId = mAddressInfos.get(position).getAddressId();
					ConfigManager.getInstance().setChoosedAddressId(addressId);
					ChooseAddressDialog.this.finish();
				}
			}
		});
		mAddNewAddress = (TextView) findViewById(R.id.add_new_address);
		mAddNewAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChooseAddressDialog.this, AddAddressActivity.class);
				startActivity(intent);
				ChooseAddressDialog.this.finish();
			}
		});
		
		findViewById(R.id.select_weight_free_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChooseAddressDialog.this.finish();
			}
		});
		int choosedAddressId = ConfigManager.getInstance().getChoosedAddressId();
		mAddressListAdapter = new ChooseAddressListAdapter(this, mAddressInfos, choosedAddressId);
		mChooseAddressList.setAdapter(mAddressListAdapter);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		mUserModel.requestListAddress(ChooseAddressDialog.this, ConfigManager.getInstance().getUserId());
	}
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestListAddressResult(List<AddressInfo> adressInfos) {
			if(adressInfos == null) return;
			mAddressInfos.addAll(adressInfos);
			mHandler.sendEmptyMessage(HAND_ADDRESS_COMPLTED);
		}
	}
}
