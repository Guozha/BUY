package com.guozha.buy.controller.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class MyInvateNumActivity extends BaseActivity{
	private static final String PAGE_NAME = "优惠码";
	private EditText mEditText;
	private Button mSubmitButton;
	private TextView mInvateResult;
	
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invate_num);
		customActionBarStyle(PAGE_NAME);
		mEditText = (EditText) findViewById(R.id.mine_invit_invitation);
		mSubmitButton = (Button) findViewById(R.id.mine_invite_commit_button);
		mInvateResult = (TextView) findViewById(R.id.invite_result_text);
		mEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!s.toString().isEmpty()){
					mSubmitButton.setEnabled(true);
				}else{
					mSubmitButton.setEnabled(false);
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
		
		mSubmitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String preferenNo = mEditText.getText().toString();
				if(preferenNo.isEmpty()) return;
				String token = ConfigManager.getInstance().getUserToken(MyInvateNumActivity.this);
				int userId = ConfigManager.getInstance().getUserId();
				mUserModel.requestInvateNumCommit(MyInvateNumActivity.this, token, userId, preferenNo);
			}
		});
	}
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestInvateNumCommitResult(String returnCode, String msg) {
			mInvateResult.setText(msg);
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				mEditText.setText("");
				mSubmitButton.setEnabled(false);
			}else{
				ToastUtil.showToast(MyInvateNumActivity.this, msg);
			}
		}
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
