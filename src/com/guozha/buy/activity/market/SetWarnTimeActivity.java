package com.guozha.buy.activity.market;

import android.os.Bundle;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.WarnTimeListAdapter;

/**
 * 设置提醒
 * @author PeggyTong
 *
 */
public class SetWarnTimeActivity extends BaseActivity{
	
	private ListView mWarnList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setwarntime);
		
		customActionBarStyle("提醒");
		
		initView();
	}
	
	private void initView(){
		mWarnList = (ListView) findViewById(R.id.set_warn_list);
		mWarnList.setAdapter(new WarnTimeListAdapter(this));
	}

}
