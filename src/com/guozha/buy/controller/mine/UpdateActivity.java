package com.guozha.buy.controller.mine;

import com.guozha.buy.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class UpdateActivity extends Activity{

	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		mWebView = (WebView) findViewById(R.id.webview_yingyongbao);
		mWebView.loadUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.guozha.buy#opened");
		
	}
}
