package com.guozha.buy.controller.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.SystemModel;
import com.guozha.buy.model.result.SystemModelResult;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 意见反馈
 * @author PeggyTong
 *
 */
public class FeadbackActivity extends BaseActivity{
	
	private TextView mFeadBackText;
	private Button mButton;
	
	private static final String PAGE_NAME = "FeadbackPage";
	
	private SystemModel mSystemModel = new SystemModel(new MySystemModelResult());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feadback);
		customActionBarStyle("意见反馈");
		
		initView();
	}
	
	private void initView(){
		mFeadBackText = (TextView) findViewById(R.id.feadback_text);
		mButton = (Button) findViewById(R.id.feadback_button);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String feadback = mFeadBackText.getText().toString();
				if(feadback.length() < 5){
					ToastUtil.showToast(FeadbackActivity.this, "不能少于5个字");
				}else{
					requestFeadback(feadback);
				}
			}
		});
		
		mFeadBackText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!s.toString().isEmpty()){
					mButton.setEnabled(true);
				}else{
					mButton.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	private void requestFeadback(String feadback){
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(this);
		if(token == null) return; //TODO 先登录
		mSystemModel.requestFeadback(this, token, userId, feadback);
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
	
	class MySystemModelResult extends SystemModelResult{
		@Override
		public void requestFeadbackResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				ToastUtil.showToast(FeadbackActivity.this, "反馈成功");
			}
		}
	}
}
