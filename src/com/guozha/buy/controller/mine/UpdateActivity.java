package com.guozha.buy.controller.mine;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;

/**
 * 系统更新
 * @author PeggyTong
 *
 */
public class UpdateActivity extends BaseActivity{

	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		customActionBarStyle("系统更新");
		mWebView = (WebView) findViewById(R.id.webview_yingyongbao);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl("http://download.wymc.com.cn/app/buyer_app.html");
	}
}
