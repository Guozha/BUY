package com.guozha.buy.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.MyAdressListAdapter;

/**
 * 我的地址
 * @author PeggyTong
 *
 */
public class MyAddressActivity extends BaseActivity{
	
	private ListView mAddressList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_address);
		customActionBarStyle("我的地址");
		
		initView();
	}
	
	private void initView(){
		mAddressList = (ListView) findViewById(R.id.my_address_list);
		mAddressList.setAdapter(new MyAdressListAdapter(this));
		
		mAddressList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MyAddressActivity.this, ModifyAddressActivity.class);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.myaddress_add_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyAddressActivity.this, AddAddressActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
