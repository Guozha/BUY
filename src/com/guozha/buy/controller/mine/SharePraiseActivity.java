package com.guozha.buy.controller.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.dialog.ActiveRuleActivity;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.SystemModel;
import com.guozha.buy.model.result.SystemModelResult;
import com.guozha.buy.server.ShareManager;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.media.UMImage;

/**
 * 推荐有奖
 * @author PeggyTong
 *
 */
public class SharePraiseActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SharePraisePage";
	private static final int HAND_DATA_COMPLETED = 0x0001;
	private static final int HAND_INVITEID_COMPLTED = 0x0002;
	private static final String SHARE_TITLE = "做了好人几十年，真心不愿吃独食，戳一下5块钱菜票拿走不谢~";
	private static final String SHARE_CONTENT = "我家买菜不要钱，所以你家买菜当然也不要钱~";
	
	private TextView mTotalAmountText;
	private TextView mUsedAmountText;
	private TextView mTotalMoneyText;
	
	private int mDrawAmount;
	private int mUsedAmount;
	private int mAwardPrice;
	
	private SystemModel mSystemModel = new SystemModel(new MySystemModelResult());
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				if(mTotalAmountText != null){
					mTotalAmountText.setText(String.valueOf(mDrawAmount));
				}
				if(mUsedAmountText != null){
					mUsedAmountText.setText(String.valueOf(mUsedAmount));
				}
				if(mTotalMoneyText != null){
					mTotalMoneyText.setText(String.valueOf(mAwardPrice));
				}
				break;
			case HAND_INVITEID_COMPLTED:
				//分享
				ShareManager shareManager = new ShareManager(SharePraiseActivity.this);
				String shareOpenUrl;
				if(mShareUrl == null || "".equals(mShareUrl)){
					shareOpenUrl = "http://download.wymc.com.cn/app/buyer_app.html";
				}else{
					shareOpenUrl = mShareUrl + mInviteId;
				}
				shareManager.shareToWeixinFriends(SharePraiseActivity.this,
						new UMImage(SharePraiseActivity.this, R.drawable.logo_share),
						SHARE_TITLE, SHARE_CONTENT, shareOpenUrl);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice_praise);
		customActionBarStyle("我的收益");
		initView();
		initData();
	}
	
	private int mInviteId = 0;	//邀请id
	private String mShareUrl = null;
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mTotalAmountText = (TextView) findViewById(R.id.total_amount);
		mUsedAmountText = (TextView) findViewById(R.id.used_amount);
		mTotalMoneyText = (TextView) findViewById(R.id.total_money);
		
		findViewById(R.id.advice_praise_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int userId = ConfigManager.getInstance().getUserId();
				String token = ConfigManager.getInstance().getUserToken();
				mSystemModel.requestInviteShare(SharePraiseActivity.this, userId, token);
			}
		});
		
		findViewById(R.id.activity_rule_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SharePraiseActivity.this, ActiveRuleActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		mSystemModel.requestInviteInfo(this, userId);
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
		public void requestInviteShareResult(String returnCode, int inviteId,
				String shareUrl, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				mInviteId = inviteId;
				mShareUrl = shareUrl;
				handler.sendEmptyMessage(HAND_INVITEID_COMPLTED);
			}else{
				ToastUtil.showToast(SharePraiseActivity.this, msg);
			}
		}
		
		@Override
		public void requestInviteInfoResult(int drawAmount, int usedAmount,
				int awardPrice) {
			mDrawAmount = drawAmount;
			mUsedAmount = usedAmount;
			mAwardPrice = awardPrice;
			handler.sendEmptyMessage(HAND_DATA_COMPLETED);
		}
	}
}
