package com.guozha.buy.activity.mine;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 常见问题
 * @author PeggyTong
 *
 */
public class AnswerQuestionActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "AnswerQuestionPage";
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_question);
		customActionBarStyle("常见问题");
		
		initView();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mWebView = (WebView) findViewById(R.id.answer_question_page);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl("http://www.baidu.com");
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
