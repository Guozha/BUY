package com.guozha.buy.activity.mine;

import android.os.Bundle;

import com.guozha.buy.activity.global.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 常见问题
 * @author PeggyTong
 *
 */
public class AnswerQuestionActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "AnswerQuestionPage";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		customActionBarStyle("常见问题");
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
