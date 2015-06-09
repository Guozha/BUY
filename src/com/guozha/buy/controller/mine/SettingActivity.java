package com.guozha.buy.controller.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.SettingListAdapter;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.LicenceActivity;
import com.guozha.buy.controller.LoginActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 设置界面
 * @author PeggyTong
 *
 */
public class SettingActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SettingPage";
	
	private ListView mSettingList;
	
	private List<String> mSettingItems;
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		customActionBarStyle("设置");
		initData();
		initView();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		mSettingItems = new ArrayList<String>();
		mSettingItems.add("意见反馈");
		//mSettingItems.add("常见问题");
		mSettingItems.add("在线客服");
		//mSettingItems.add("系统更新");
		mSettingItems.add("关于我们");
		mSettingItems.add("服务协议");
	}
	
	private void initView(){
		findViewById(R.id.setting_login_out_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				requestLoginOut();
			}
		});
		
		mSettingList = (ListView) findViewById(R.id.setting_list);
		mSettingList.setAdapter(new SettingListAdapter(this, mSettingItems));
		
		mSettingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				case 0:     //意见反馈
					intent = new Intent(SettingActivity.this, FeadbackActivity.class);
					startActivity(intent);
					break;
				/*
				case 1:		//常见问题
					intent = new Intent(SettingActivity.this, AnswerQuestionActivity.class);
					startActivity(intent);
					break;
				*/
				case 1:     //在线客服
					dialServerTelephone();
					break;
			    /*
				case 3:     //系统更新
					//TODO 从服务器获取版本号进行判断，
					
					//如果没有新版本
					//ToastUtil.showToast(SettingActivity.this, "您当前使用的已经是最新版本");
					
					final CustomDialog versionDialog = 
							new CustomDialog(SettingActivity.this, R.layout.dialog_version_update);
					versionDialog.setDismissButtonId(R.id.cancel_button);
					versionDialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO 开始更新
							versionDialog.dismiss();
						}
					});
					
					break;
					*/
				case 2:     //关于我们
					intent = new Intent(SettingActivity.this, AboutOurActivity.class);
					startActivity(intent);
					break;
				case 3:		//服务协议
					intent = new Intent(SettingActivity.this, LicenceActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
		});
	}
	
	/**
	 * 请求退出登录
	 */
	private void requestLoginOut() {
		String token = ConfigManager.getInstance().getUserToken(SettingActivity.this);
		if(token == null){
			return;
		}
		mUserModel.requestLoginOut(this, token);
	}
	
	/**
	 * 拨打客服电话
	 */
	private void dialServerTelephone() {
		final CustomDialog dialDialog = 
			new CustomDialog(SettingActivity.this, R.layout.dialog_dial_telephone);
		dialDialog.setDismissButtonId(R.id.cancel_button);
		dialDialog.getViewById(R.id.dial_tel_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialDialog.dismiss();
				String phoneNum = "0571-86021150";
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
				SettingActivity.this.startActivity(intent);
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
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestLoginOutResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				//清空用户相关信息
				ConfigManager.getInstance().clearUserInfor();
				ToastUtil.showToast(SettingActivity.this, "退出成功");
				//跳到登录界面
				Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
				startActivity(intent);
				SettingActivity.this.finish();
			}else{
				ToastUtil.showToast(SettingActivity.this, msg);
			}
		}
	}
}
