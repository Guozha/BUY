package com.guozha.buy.activity.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.debug.DebugActivity;
import com.guozha.buy.dialog.CustomDialog;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.util.Misc;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于我们
 * @author PeggyTong
 *
 */
public class AboutOurActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "AboutOurPage";
	private TextView mVersionNameText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_our);
		customActionBarStyle("关于我们");
		
		initView();
	}
	
	private void initView(){
		mVersionNameText = (TextView) findViewById(R.id.about_our_version_name);
		mVersionNameText.setText("爱掌勺" + ConfigManager.getInstance().getVersionName());
		
		findViewById(R.id.logo_icon).setOnClickListener(this);
		
		findViewById(R.id.about_our_weixin).setOnClickListener(this);
		findViewById(R.id.about_our_weibo).setOnClickListener(this);
		findViewById(R.id.about_our_qq_group).setOnClickListener(this);
		findViewById(R.id.about_our_website).setOnClickListener(this);
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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.about_our_weixin:
			new CustomDialog(this, R.layout.dialog_custom_erweima).getWindow();
			break;
		case R.id.about_our_weibo:
			openWeiboUserInfo("5571667895");
			break;
		case R.id.about_our_qq_group:
			openQQgroup();
			break;
		case R.id.about_our_website:
			
			break;
		case R.id.logo_icon:
			turnToDebug();
			break;
		}
	}
	
	private int mClickIconTimes = 0;
	private long mBeginTimeMillis;
	private void turnToDebug(){
		if(System.currentTimeMillis() - mBeginTimeMillis > 4000){
			mClickIconTimes = 0;
		}
		if(mClickIconTimes == 0){
			mBeginTimeMillis = System.currentTimeMillis();
		}
		if(mClickIconTimes <= 10){
			mClickIconTimes++;
		}else{
			mClickIconTimes = 0;
			Intent intent = new Intent(AboutOurActivity.this, DebugActivity.class);
			startActivity(intent);
		}
	}
	
	/**
     * 通过uid打开个人资料界面。
     * 
     * @param uid   用户ID
     */
    public void openWeiboUserInfo(String uid){
    	if(Misc.isAppInstalled(AboutOurActivity.this, "com.sina.weibo")){
	        Intent intent=new Intent();
	        intent.setAction(Intent.ACTION_VIEW);
	        intent.addCategory("android.intent.category.DEFAULT");
	        intent.setData(Uri.parse("sinaweibo://userinfo?uid="+uid));
	        startActivity(intent);
    	}
    }
    
	/**
	 * 打开QQ群对话
	 */
	private boolean openQQgroup() {
		Intent intent = new Intent();
		String key = "x_5sY0dqNTtdUH1yD_TZEUG2F3gV8d4K";
	    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
	    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	     try {
	         startActivity(intent);
	         return true;
	     } catch (Exception e) {
	         // 未安装手Q或安装的版本不支持
	         return false;
	     }
	}
}
