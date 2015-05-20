package com.guozha.buy.dialog;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.mine.AddAddressActivity;
import com.guozha.buy.adapter.ChooseAddressListAdapter;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.util.LogUtil;

/**
 * 选择地址对话框
 * @author PeggyTong
 *
 */
public class ChooseAddressDialog extends Activity{
	
	private ListView mChooseAddressList;
	private TextView mAddNewAddress;
	private List<AddressInfo> mAddressInfos;

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
					LogUtil.e("改变的地址 = " + addressId);
					ConfigManager.getInstance().setChoosedAddressId(addressId);
					LogUtil.e("设置后的地址 = " + addressId);
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
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		mAddressInfos = 
				MainPageInitDataManager.getInstance().getAddressInfos(null);
		mChooseAddressList.setAdapter(new ChooseAddressListAdapter(this, mAddressInfos));
	}
}
