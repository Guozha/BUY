package com.guozha.buy.activity.global;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import com.guozha.buy.R;
import com.umeng.analytics.MobclickAgent;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.webkit.WebView;


/**
 * 服务协议
 * @author PeggyTong
 *
 */
public class LicenceActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "LicencePage";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_licence);
		customActionBarStyle("服务协议");
		initWebView();
	}
	
	private boolean initWebView() {
		WebView webView = (WebView)findViewById(R.id.webview);
		AssetManager am = getResources().getAssets();
		if (am == null) {
			return false;
		}
		InputStream input = null;
		InputStreamReader reader = null;
		try {
			//TODO 这个用户协议要替换哦
			input = am.open("licence.html");
			if (input == null) {
				return false;
			}
			reader = new InputStreamReader(input);
			input = null;
			CharBuffer buffer = CharBuffer.allocate(12 * 1024);
			reader.read(buffer);
			String content = new String(buffer.array(), 0, buffer.position());
			webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
			return true;
		} catch (IOException e) {

		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
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
