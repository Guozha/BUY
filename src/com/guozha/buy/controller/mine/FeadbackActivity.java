package com.guozha.buy.controller.mine;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
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
	
	private static final String PAGE_NAME = "意见反馈";
	
	private SystemModel mSystemModel = new SystemModel(new MySystemModelResult());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feadback);
		customActionBarStyle(PAGE_NAME);
		
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
				dialogWeixinKefu();
			}else{
				ToastUtil.showToast(FeadbackActivity.this, msg);
			}
		}
	}
	
	private void dialogWeixinKefu(){
		final CustomDialog dialDialog = 
			new CustomDialog(this, R.layout.dialog_weixin_kefu);
		TextView weixinnum = (TextView) dialDialog.getViewById(R.id.weixinnum);
		TextView title = (TextView) dialDialog.getViewById(R.id.weixin_kefu_title);
		title.setText("感谢您的意见，我们会及时处理");
		weixinnum.setText("微信客服：" + ConfigManager.getInstance().getWeixinNum());
		dialDialog.setDismissButtonId(R.id.cancel_button);
		dialDialog.getViewById(R.id.copy_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				copyText(ConfigManager.getInstance().getWeixinNum());
				dialDialog.dismiss();
			}
		});
	}
	
	private void copyText(String text) {
		ClipboardManager clipboard = (ClipboardManager) 
				this.getSystemService(android.content.Context.CLIPBOARD_SERVICE);  
		ClipData clip =ClipData.newPlainText("orderMessage", text);
		clipboard.setPrimaryClip(clip);
		ToastUtil.showToast(this, "已经复制到剪贴板");
	}
}
