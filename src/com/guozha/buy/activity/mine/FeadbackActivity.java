package com.guozha.buy.activity.mine;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 意见反馈
 * @author PeggyTong
 *
 */
public class FeadbackActivity extends BaseActivity{
	
	private TextView mFeadBackText;
	
	private static final String PAGE_NAME = "FeadbackPage";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feadback);
		customActionBarStyle("意见反馈");
		
		initView();
	}
	
	private void initView(){
		mFeadBackText = (TextView) findViewById(R.id.feadback_text);
		findViewById(R.id.feadback_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String feadback = mFeadBackText.getText().toString();
				if(feadback.length() < 10){
					ToastUtil.showToast(FeadbackActivity.this, "不能少于10个字");
				}else{
					requestFeadback(feadback);
				}
			}
		});
	}
	
	private void requestFeadback(String feadback){
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken();
		RequestParam paramPath = new RequestParam("account/opinion/insert")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("opinion", feadback);
		HttpManager.getInstance(FeadbackActivity.this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							ToastUtil.showToast(FeadbackActivity.this, "反馈成功");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
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
