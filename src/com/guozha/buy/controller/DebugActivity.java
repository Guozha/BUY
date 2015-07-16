package com.guozha.buy.controller;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.dialog.WeightSelectDialog;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.SystemModel;
import com.guozha.buy.model.result.SystemModelResult;
import com.guozha.buy.server.AlipayManager;
import com.guozha.buy.server.ShareManager;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.YoumengDeviceManager;

/**
 * 用于测试的UI界面
 * @author PeggyTong
 *
 */
public class DebugActivity extends BaseActivity implements OnClickListener{
	
	private TextView mCurrentHttpPath;
	
	//测试服
	public static final String TEST_URL = "http://120.24.220.86:8080/BUY_SERVER/";
	public static final String TEST_ALI_PAY_URL = "http://120.24.220.86:9999/PAY_ALI/notify_url.jsp";
	//正式服
	public static final String REAL_URL = "http://120.24.211.45:8080/BUY_SERVER/";
	public static final String REAL_ALI_PAY_URL = "http://120.24.211.45:9999/PAY_ALI/notify_url.jsp";

	private SystemModel mSystemModel = new SystemModel(new MySystemModelResult());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);
		customActionBarStyle("Debug");
		findViewById(R.id.youmeng_phoneid_test).setOnClickListener(this);
		
		//分享相关
		findViewById(R.id.share_to_qq).setOnClickListener(this);
		
		//重量选择框
		findViewById(R.id.weight_selected_dialog_test).setOnClickListener(this);
		
		//当前服务器路径
		mCurrentHttpPath = (TextView) findViewById(R.id.current_http_path_text);
		switchHttpPathText();
		
		//转换测试服和正式服
		findViewById(R.id.switch_http_path).setOnClickListener(this);
	}

	private void switchHttpPathText() {
		if(mCurrentHttpPath == null) return;
		if(REAL_URL.equals(RequestParam.URL)){
			mCurrentHttpPath.setText("当前是正式服");
		}else{
			mCurrentHttpPath.setText("当前是测试服");
		}
		mSystemModel.requestImagePath(DebugActivity.this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.youmeng_phoneid_test: //友盟获取测试手机ID
			String deviceId = YoumengDeviceManager.getDeviceInfo(DebugActivity.this);
			
			ClipboardManager clipboard =  
				      (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);  
				 
			ClipData clip =ClipData.newPlainText("deviceId", deviceId);
			clipboard.setPrimaryClip(clip);
			ToastUtil.showToast(DebugActivity.this, "已复制到剪切板 " + deviceId);
			break;
		case R.id.share_to_qq:			//分享到qq
			ShareManager shareManager = new ShareManager(DebugActivity.this);
			shareManager.shareToQQ(DebugActivity.this);
			break;
		case R.id.weight_selected_dialog_test:  //重量选择框
			Intent intent = new Intent(DebugActivity.this, WeightSelectDialog.class);
			startActivity(intent);
			break;
		case R.id.switch_http_path:
			if(REAL_URL.equals(RequestParam.URL)){
				RequestParam.URL = TEST_URL;
				AlipayManager.NOTIFY_URL = TEST_ALI_PAY_URL;
			}else{
				RequestParam.URL = REAL_URL;
				AlipayManager.NOTIFY_URL = REAL_ALI_PAY_URL;
			}
			ConfigManager.getInstance().clearUserInfor();
			switchHttpPathText();
			break;
		default:
			break;
		}
	}

	class MySystemModelResult extends SystemModelResult{
		@Override
		public void requestImagePathResult(String imgPath) {
			if(imgPath == null) return;
			ConfigManager.getInstance().setImagePath(imgPath);
		}
	}

}
