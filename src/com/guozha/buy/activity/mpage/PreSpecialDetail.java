package com.guozha.buy.activity.mpage;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.activity.cart.PreSpecialPayActivity;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.umeng.analytics.MobclickAgent;

public class PreSpecialDetail extends BaseActivity{
	
	private static final String PAGE_NAME = "PreSpecialDetailPage";
	
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private ImageView mHeadImage;
	private TextView mPreSpecialTitle;
	private TextView mPriceText;
	private TextView mEndDayText;
	private TextView mArrivalDayText;
	private WebView mWebView;
	
	private int mGoodsId = -1;
	private int mUnitPrice;		//商品单价
	
	private String mGoodsName;
	private String mGoodsPrice;
	private String mImageUrl;
	//private String mGoodsDescript;
	private int mArrivalDays;
	private String mEndDate;
	private String mGoodsType;	//01 普通商品 02 特供 03 预售
	private String mWebUrl;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateView();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prespecial_detail);
		customActionBarStyle("商品详情");
		
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mGoodsId = bundle.getInt("goodsId");
			}
		}
		initView();
		initData();
	}
	
	private void initView(){
		findViewById(R.id.prespecial_buy_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO 先判断是否登录，再判断一下是否添加地址了
				String token = ConfigManager.getInstance().getUserToken(PreSpecialDetail.this);
				if(token == null) return;
				//TODO 再判断当前选择的地址是否为NULL
				if(ConfigManager.getInstance().getChoosedAddressId(PreSpecialDetail.this) == -1) return;
				Intent intent = new Intent(PreSpecialDetail.this, PreSpecialPayActivity.class);
				intent.putExtra("goodsId", mGoodsId);
				intent.putExtra("unitPrice", mUnitPrice);
				intent.putExtra("goodsName", mGoodsName);
				intent.putExtra("goodsType", mGoodsType);
				startActivity(intent);
			}
		});
		
		mPreSpecialTitle = (TextView) findViewById(R.id.prespecial_detial_descript);
		
		//SpannableStringBuilder builder = new SpannableStringBuilder(mPreSpecialTitle.getText());
		//builder.setSpan(  
	    //       new ImageSpan(this, R.drawable.sale_tag_02, DynamicDrawableSpan.ALIGN_BOTTOM), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//mPreSpecialTitle.setText(builder);
		
		mWebView = (WebView) findViewById(R.id.prespecail_detail_webview);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		
		mHeadImage = (ImageView) findViewById(R.id.prespecial_detail_img);
		mPriceText = (TextView) findViewById(R.id.prespecial_detail_price);
		mEndDayText = (TextView) findViewById(R.id.prespecial_detail_end_day);
		mArrivalDayText = (TextView) findViewById(R.id.prespecial_detail_arrival_day);
		
	}
	
	
	private void initData(){
		if(mGoodsId == -1) {
			return;
		}
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("goods/special/detail")
		.setParams("addressId", addressId)
		.setParams("goodsId", mGoodsId);
		HttpManager.getInstance(PreSpecialDetail.this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						mImageUrl = response.getString("goodsImg");
						mGoodsName = response.getString("goodsName");
						mGoodsType = response.getString("goodsProp"); //商品性质
						//mGoodsDescript = response.getString("memo");			   	//商品备注
						mUnitPrice = response.getInt("unitPrice");		//单价
						String unit = response.getString("unit");				//计量单位
						mEndDate = response.getString("prepareEndDate");	//预售截止日期
						mArrivalDays = response.getInt("arrivalDays");		//送达天数
						mWebUrl = response.getString("picDesc");			//图文介绍地址
						mGoodsPrice = UnitConvertUtil.getSwitchedMoney(mUnitPrice) + "元/" + UnitConvertUtil.getSwichedUnit(1000, unit);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(HAND_DATA_COMPLETED);
				}
			});
	}
	
	
	private void updateView(){
		HttpManager.getInstance(PreSpecialDetail.this).volleyImageRequest(
				HttpManager.URL + mImageUrl, mHeadImage, R.drawable.default_icon_large, R.drawable.default_icon_large);
		mPreSpecialTitle.setText(mGoodsName);
		mPriceText.setText(mGoodsPrice);
		mEndDayText.setText("截止时间：" + mEndDate);
		mArrivalDayText.setText("预计送达日期:" + mArrivalDays + "天");
		mWebView.loadUrl(HttpManager.URL + mWebUrl);
		
		int iconId = -1;
		if("02".equals(mGoodsType)){
			iconId = R.drawable.sale_tag_01;
		}else if("03".equals(mGoodsType)){
			iconId = R.drawable.sale_tag_02;
		}
		if(iconId != -1){
			Drawable drawable = this.getResources().getDrawable(iconId);
			mPreSpecialTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		}else{
			mPreSpecialTitle.setCompoundDrawables(null, null, null, null);
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
