package com.guozha.buy.activity.mpage;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.DetailMaterialListAdapter;
import com.guozha.buy.adapter.DetailSeasonListAdapter;
import com.guozha.buy.adapter.DetailStepListAdapter;
import com.guozha.buy.entry.mpage.plan.CookBookDetail;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.ConstantUtil;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 菜谱详情
 * @author PeggyTong
 *
 */
public class CookBookDetailActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "CookBookDetailPage";
	
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private BitmapCache mBitmapCache = CustomApplication.getBitmapCache();
	
	private ListView mMaterialList;//食材
	private ListView mSeasonList;  //调料
	private ListView mStepList;    //做法步骤
	
	private TextView mCookBookName;
	private TextView mCookBookDifficu;
	private TextView mCookBookUsingTime;
	private TextView mCookBookDescrip;
	private ImageView mCookBookImage;
	
	private CookBookDetail mCookBookDetail;

	private int mMenuId = -1;
	
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
		setContentView(R.layout.activity_cookbook_detail);
		customActionBarStyle("菜谱详情");
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mMenuId = bundle.getInt("menuId");
			}
		}
		initView();
		initData();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mMaterialList = (ListView) findViewById(R.id.cookbook_detail_material);
		mSeasonList = (ListView) findViewById(R.id.cookbook_detail_seasoning);
		mStepList = (ListView) findViewById(R.id.cookbook_detail_step);
		
		//解决scrollview进入时位置不在最顶部问题
		/*
		mMaterialList.post(new Runnable() {
			@Override
			public void run() {
				findViewById(R.id.scrollview).scrollTo(0, 0);
			}
		});
		*/
		mCookBookName = (TextView) findViewById(R.id.cookbook_name);
		mCookBookDifficu = (TextView) findViewById(R.id.cookbook_difficulty);
		mCookBookUsingTime = (TextView) findViewById(R.id.cookbook_using_time);
		mCookBookDescrip = (TextView) findViewById(R.id.cookbook_description);
		mCookBookImage = (ImageView) findViewById(R.id.cookbook_imag);
		
		findViewById(R.id.cookbook_add_cart_button).setOnClickListener(this);
		findViewById(R.id.cookbook_add_collection_button).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.cookbook_add_cart_button:
			requestAddCartMenu();
			break;
		case R.id.cookbook_add_collection_button:
			requestCollectionMenu();
			break;
		}
	}
	
	/**
	 * 请求添加购物车
	 */
	private void requestAddCartMenu(){
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(CookBookDetailActivity.this);
		if(token == null) return;
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		
		RequestParam paramPath = new RequestParam("cart/insert")
		.setParams("userId", userId)
		.setParams("token", token)
		.setParams("id", mMenuId)
		.setParams("productType", "01")
		.setParams("amount", "1")
		.setParams("addressId", addressId);
		
		HttpManager.getInstance(this).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				String returnCode;
				try {
					returnCode = response.getString("returnCode");
					if("1".equals(returnCode)){
						ToastUtil.showToast(CookBookDetailActivity.this, "已添加到购物车");
						MainPageInitDataManager.mCartItemsUpdated = true;
					}else{
						ToastUtil.showToast(CookBookDetailActivity.this, "msg");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 请求添加收藏
	 */
	private void requestCollectionMenu() {
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(CookBookDetailActivity.this);
		if(token == null) return;
		RequestParam paramPath = new RequestParam("account/myfavo/insertMenuFavo")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("menuIds", mMenuId);
		HttpManager.getInstance(this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							ToastUtil.showToast(CookBookDetailActivity.this, "收藏成功");
						}else{
							ToastUtil.showToast(CookBookDetailActivity.this, "收藏失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		RequestParam paramPath = new RequestParam("menuplan/detail")
		.setParams("menuId", mMenuId);
		HttpManager.getInstance(CookBookDetailActivity.this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					mCookBookDetail = gson.fromJson(response, new TypeToken<CookBookDetail>() { }.getType());
					handler.sendEmptyMessage(HAND_DATA_COMPLETED);
				}
		});
	}
	
	private void updateView(){
		if(mCookBookDetail == null) return;
		mCookBookName.setText(mCookBookDetail.getMenuName());
		mCookBookDifficu.setText("难度:" + ConstantUtil.getCookHardType(mCookBookDetail.getHardType()));
		mCookBookUsingTime.setText("用时:" + mCookBookDetail.getCookieTime() + "分钟");
		mCookBookDescrip.setText(mCookBookDetail.getMenuDesc());
		HttpManager.getInstance(this).volleyImageRequest(
				HttpManager.URL + mCookBookDetail.getMenuImg(), new Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap response) {
				if(response != null){
					mCookBookImage.setImageBitmap(response);
				}
			}
		});
		mMaterialList.setAdapter(new DetailMaterialListAdapter(this, mCookBookDetail.getMenuGoods()));
		mSeasonList.setAdapter(new DetailSeasonListAdapter(this, mCookBookDetail.getSeasonings()));
		mStepList.setAdapter(new DetailStepListAdapter(this, mCookBookDetail.getMenuSteps(), mBitmapCache));
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
		mBitmapCache.fluchCache();
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
}
