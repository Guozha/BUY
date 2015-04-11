package com.guozha.buy.activity.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.global.ConfigManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于我们
 * @author PeggyTong
 *
 */
public class AboutOurActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "AboutOurPage";
	private TextView mVersionNameText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_our);
		customActionBarStyle("关于我们");
		
		initView();
	}
	
	private void initView(){
		mVersionNameText = (TextView) findViewById(R.id.about_our_version_name);
		mVersionNameText.setText("爱掌勺" + ConfigManager.getInstance().getVersionName());
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
