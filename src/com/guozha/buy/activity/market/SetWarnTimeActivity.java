package com.guozha.buy.activity.market;

import android.os.Bundle;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.WarnTimeListAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 设置提醒
 * @author PeggyTong
 *
 */
public class SetWarnTimeActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SetWarnTimePage";
	
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
