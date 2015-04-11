package com.guozha.buy.activity.market;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.CookBookListAdapter;
import com.guozha.buy.entry.market.GoodsDetail;
import com.guozha.buy.entry.market.RelationRecipe;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.umeng.analytics.MobclickAgent;

/**
 * 菜品详情
 * @author PeggyTong
 *
 */
public class VegetableDetailActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "VegetableDetailPage";
	private static final int HAND_DATA_COMPLTED = 0x0001;  
	private static final int HAND_RELATION_RECIPE_COMPLETED = 0x0002;
	
	private ListView mConnCookBookList;
	private GoodsDetail mGoodsDetails = null;
	private List<RelationRecipe> mRelationRecipes = null;
	
	private ImageView mDetailImage;
	private TextView mDetailName;
	private TextView mDetailPrice;
	private TextView mDetailDescript;
	
	private String mGoodsId = null;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLTED:
				updateHeadView();
				break;
			case HAND_RELATION_RECIPE_COMPLETED:
				updateRelationView();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		customActionBarStyle("菜品详情");
		setContentView(R.layout.activity_vegetable_detail);
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mGoodsId = bundle.getString("goodsId");
			}
		}
		initView();
		initData();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mConnCookBookList = (ListView) 
				findViewById(R.id.vegetable_connect_cookbook_list);
		View head = getLayoutInflater().inflate(R.layout.vegetable_detail_list_head, null);
		initHeader(head);
		mConnCookBookList.addHeaderView(head);
		setTextColor();
	}
	
	private void initHeader(View header){
		mDetailImage = (ImageView) header.findViewById(R.id.detail_image);
		mDetailName = (TextView) header.findViewById(R.id.detail_name);
		mDetailPrice = (TextView) header.findViewById(R.id.vegetable_detail_item_price);
		mDetailDescript = (TextView) header.findViewById(R.id.vegetable_description);
		header.findViewById(R.id.cookbook_add_cart_button).setOnClickListener(this);
		header.findViewById(R.id.cookbook_collection_button).setOnClickListener(this);
	}
	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.cookbook_add_cart_button:
			
			break;
		case R.id.cookbook_collection_button:
			
			break;
		}
	}
	
	private void initData(){
		//2.1.4接口
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("goods/general/detail")
		.setParams("goodsId", mGoodsId)
		.setParams("addressId", addressId);
		HttpManager.getInstance(this).volleyRequestByPost(HttpManager.URL + paramPath, 
				new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				mGoodsDetails = gson.fromJson(response, new TypeToken<GoodsDetail>() { }.getType());
				handler.sendEmptyMessage(HAND_DATA_COMPLTED);
			}
		});
		//7.4接口
		paramPath = new RequestParam("menuplan/goodsMenuList")
		.setParams("goodsId", mGoodsId);
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				mRelationRecipes = gson.fromJson(response, new TypeToken<List<RelationRecipe>>() { }.getType());
				handler.sendEmptyMessage(HAND_RELATION_RECIPE_COMPLETED);
			}
		});
		
		
	}
	
	/**
	 * 更新上面的信息
	 */
	private void updateHeadView(){
		if(mGoodsDetails == null) return;
		HttpManager.getInstance(this).volleyImageRequest(
				HttpManager.IMG_URL + mGoodsDetails.getGoodsImg(), 
				mDetailImage, R.drawable.default_icon, R.drawable.default_icon);
		mDetailName.setText(mGoodsDetails.getGoodsName());
		mDetailPrice.setText("￥" + mGoodsDetails.getUnitPrice() + "/斤");
		mDetailDescript.setText(mGoodsDetails.getMemo());
		setTextColor();
	}
	
	/**
	 * 更新底下相关菜谱列表
	 */
	private void updateRelationView(){
		mConnCookBookList.setAdapter(new CookBookListAdapter(this, mRelationRecipes));
	}
	
	/**
	 * 给字体设置红色
	 */
	private void setTextColor(){
		String msgTotal = mDetailPrice.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(msgTotal);
		
		ForegroundColorSpan redSpan = new ForegroundColorSpan(
				getResources().getColor(R.color.color_app_base_1));
		int totalSpanSart = msgTotal.indexOf("/");
		builder.setSpan(redSpan, 0, totalSpanSart - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mDetailPrice.setText(builder);
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
