package com.guozha.buy.controller.mine;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.DebugActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于我们
 * @author PeggyTong
 *
 */
public class AboutOurActivity extends BaseActivity implements OnClickListener, OnLongClickListener{
	
	private static final String PAGE_NAME = "关于我们";
	private TextView mVersionNameText;
	private TextView mBottomVersionText;
	private TextView mWeixinGZH;
	private TextView mWeixinKF;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_our);
		customActionBarStyle(PAGE_NAME);
		
		initView();
	}
	
	private void initView(){
		mVersionNameText = (TextView) findViewById(R.id.about_our_version_name);
		mBottomVersionText = (TextView) findViewById(R.id.bottom_verson_message);
		String versionName = ConfigManager.getInstance().getVersionName();
		mVersionNameText.setText("爱掌勺" + versionName);
		mBottomVersionText.setText("版本信息：" + versionName);
		
		findViewById(R.id.logo_icon).setOnClickListener(this);
		
		mWeixinGZH = (TextView) findViewById(R.id.about_our_weixin);
		mWeixinGZH.setOnClickListener(this);
		mWeixinGZH.setOnLongClickListener(this);
		findViewById(R.id.about_our_weibo).setOnClickListener(this);
		mWeixinKF = (TextView) findViewById(R.id.about_our_qq_group);
		mWeixinKF.setText("客服微信号：" + ConfigManager.getInstance().getWeixinNum());
		mWeixinKF.setOnClickListener(this);
		mWeixinKF.setOnLongClickListener(this);
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
	public boolean onLongClick(View view) {
		switch (view.getId()) {
		case R.id.about_our_weixin:
			copyText("aizhangshao");
			break;
		case R.id.about_our_qq_group:
			copyText("aizhangshaohz");
			break;
		default:
			break;
		}
		return true;
	}
	
	private void copyText(String text) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);  
		ClipData clip =ClipData.newPlainText("orderMessage", text);
		clipboard.setPrimaryClip(clip);
		ToastUtil.showToast(AboutOurActivity.this, "已经复制到剪贴板");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.about_our_weixin:
			ImageView gongzhonghao = (ImageView) new CustomDialog(
					this, R.layout.dialog_custom_erweima).getViewById(R.id.erweima_img);
			gongzhonghao.setImageResource(R.drawable.weixin_erweima);
			break;
		case R.id.about_our_weibo:
			openWeiboUserInfo("5571667895");
			break;
		case R.id.about_our_qq_group:
			//openQQgroup();
			ImageView kefuhao = (ImageView) new CustomDialog(
					this, R.layout.dialog_custom_erweima).getViewById(R.id.erweima_img);
			kefuhao.setImageResource(R.drawable.kefuhao);
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
    	if(Util.isAppInstalled(AboutOurActivity.this, "com.sina.weibo")){
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
