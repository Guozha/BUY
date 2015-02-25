package com.guozha.buy.debug;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.guozha.buy.R;
import com.guozha.buy.util.LogUtil;

/**
 * 用于测试的UI界面
 * @author PeggyTong
 *
 */
public class DebugActivity extends Activity implements OnClickListener{
	
	private Button mYoumengPhoneidTestButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);
		
		mYoumengPhoneidTestButton = (Button) findViewById(R.id.youmeng_phoneid_test);
		mYoumengPhoneidTestButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.youmeng_phoneid_test: //友盟获取测试手机ID
			String deviceId = YoumengDeviceManager.getDeviceInfo(DebugActivity.this);
			LogUtil.e(deviceId);
			Toast.makeText(DebugActivity.this, 
					"deviceId = " + deviceId, Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}


}
