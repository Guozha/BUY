package com.guozha.buy.activity.mine;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.umeng.analytics.MobclickAgent;

/**
 * 推荐有奖
 * @author PeggyTong
 *
 */
public class SharePraiseActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SharePraisePage";
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private TextView mTotalAmountText;
	private TextView mUsedAmountText;
	private TextView mTotalMoneyText;
	
	private int mDrawAmount;
	private int mUsedAmount;
	private int mAwardPrice;
	
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

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice_praise);
		customActionBarStyle("推荐有奖");
		initView();
		initData();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mTotalAmountText = (TextView) findViewById(R.id.total_amount);
		mUsedAmountText = (TextView) findViewById(R.id.used_amount);
		mTotalMoneyText = (TextView) findViewById(R.id.total_money);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("account/invite/info")
		.setParams("userId", userId);
		HttpManager.getInstance(this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						mDrawAmount = response.getInt("drawAmount");
						mUsedAmount = response.getInt("usedAmount");
						mAwardPrice = response.getInt("awardPrice");
						handler.sendEmptyMessage(HAND_DATA_COMPLETED);
					} catch (JSONException e) {
						e.printStackTrace();
					}
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
}
