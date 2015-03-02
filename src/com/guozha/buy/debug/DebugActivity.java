package com.guozha.buy.debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.guozha.buy.R;
import com.guozha.buy.alarm.PlanMenuAlarm;
import com.guozha.buy.dialog.WeightSelectDialog;
import com.guozha.buy.share.ShareManager;
import com.guozha.buy.util.LogUtil;

/**
 * 用于测试的UI界面
 * @author PeggyTong
 *
 */
public class DebugActivity extends Activity implements OnClickListener{
	
	private EditText mAlarmHour;
	private EditText mAlarmMinute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);
		
		findViewById(R.id.youmeng_phoneid_test).setOnClickListener(this);
		
		//分享相关
		findViewById(R.id.share_to_qq).setOnClickListener(this);
		
		//设置闹钟
		findViewById(R.id.set_alarm_button).setOnClickListener(this);
		
		findViewById(R.id.weight_selected_dialog_test).setOnClickListener(this);
		mAlarmHour = (EditText) findViewById(R.id.set_alarm_hour);
		mAlarmMinute = (EditText) findViewById(R.id.set_alarm_minute);
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
		case R.id.share_to_qq:
			ShareManager shareManager = new ShareManager(DebugActivity.this);
			shareManager.shareToQQ(DebugActivity.this);
			break;
		case R.id.set_alarm_button:
			int hour = Integer.parseInt(mAlarmHour.getText().toString());
			int minute = Integer.parseInt(mAlarmMinute.getText().toString());
			if(hour == 0 && minute == 0) return;
			PlanMenuAlarm alarm = new PlanMenuAlarm(DebugActivity.this);
			alarm.setAlarm(DebugActivity.this, hour, minute);
			break;
		case R.id.weight_selected_dialog_test:
			Intent intent = new Intent(DebugActivity.this, WeightSelectDialog.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}


}
