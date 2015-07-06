package com.guozha.buy.controller.market;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.CookBookListAdapter;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.controller.found.MenuDetailActivity;
import com.guozha.buy.entry.market.GoodsDetail;
import com.guozha.buy.entry.market.RelationRecipe;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.CollectionModel;
import com.guozha.buy.model.GoodsModel;
import com.guozha.buy.model.MenuModel;
import com.guozha.buy.model.result.CollectionModelResult;
import com.guozha.buy.model.result.GoodsModelResult;
import com.guozha.buy.model.result.MenuModelResult;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 菜品详情
 * @author PeggyTong
 *
 */
public class VegetableDetailActivity extends BaseActivity implements OnClickListener{
	
	private static final String PAGE_NAME = "菜品详情";
	private static final int HAND_DATA_COMPLTED = 0x0001;  
	private static final int HAND_RELATION_RECIPE_COMPLETED = 0x0002;
	
	private ListView mConnCookBookList;
	private GoodsDetail mGoodsDetails = null;
	private List<RelationRecipe> mRelationRecipes = null;
	
	private ImageView mDetailImage;
	private TextView mDetailName;
	private TextView mDetailPrice;
	private TextView mDetailDescript;
	private BitmapCache mBitmapCache = BitmapCache.getInstance();
	
	private String mGoodsId = null;
	private GoodsModel mGoodsModel = new GoodsModel(new MyGoodsModelResult());
	private CollectionModel mCollectionModel = new CollectionModel(new MyCollectionModelResult());
	private MenuModel mMenuModel = new MenuModel(new MyMenuModelResult());
	
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
		customActionBarStyle(PAGE_NAME);
		setContentView(R.layout.activity_vegetable_detail);
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mGoodsId = bundle.getString("goodsId");
				LogUtil.e("mGoodsId == " + mGoodsId);
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
		mConnCookBookList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position <= 0) return;
				RelationRecipe recipe = mRelationRecipes.get(position - 1);
				Intent intent = new Intent(VegetableDetailActivity.this, MenuDetailActivity.class);
				if(recipe != null){
					intent.putExtra("menuId", recipe.getMenuId());
				}
				startActivity(intent);
			}
		});
		setTextColor();
	}
	
	private void initHeader(View header){
		mDetailImage = (ImageView) header.findViewById(R.id.detail_image);
		mDetailName = (TextView) header.findViewById(R.id.detail_name);
		mDetailPrice = (TextView) header.findViewById(R.id.vegetable_detail_item_price);
		mDetailDescript = (TextView) header.findViewById(R.id.vegetable_description);
		//header.findViewById(R.id.cookbook_add_cart_button).setOnClickListener(this);
		//header.findViewById(R.id.cookbook_collection_button).setOnClickListener(this);
	}
	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		/*
		case R.id.cookbook_add_cart_button:
			Intent intent = new Intent(VegetableDetailActivity.this, WeightSelectDialog.class);
			intent.putExtra("goodsId", mGoodsId);
			if(mGoodsDetails != null){
				intent.putExtra("unitPrice", String.valueOf(mGoodsDetails.getUnitPrice()));
				intent.putExtra("unit", mGoodsDetails.getUnit());
			}
			startActivity(intent);
			break;
		case R.id.cookbook_collection_button:
			requestCollectionVegetable();
			break;
			*/
		default:
			
			break;
		}
	}

	/**
	 * 请求收藏食材
	 */
	//private void requestCollectionVegetable() {
	//	String token = ConfigManager.getInstance().getUserToken();
	//	int userId = ConfigManager.getInstance().getUserId();
	//	mCollectionModel.requestCollectionGoods(this, token, userId, Integer.parseInt(mGoodsId));
	//}
	
	private void initData(){
		//2.1.4接口
		if(mGoodsId == null) return;
		int addressId = ConfigManager.getInstance().getChoosedAddressId(this);
		if(addressId == -1) return;
		mGoodsModel.requestGoodsDetail(this, Integer.parseInt(mGoodsId), addressId);
		//7.4接口
		mMenuModel.requestMenusByGoods(this, Integer.parseInt(mGoodsId));
	}
	
	/**
	 * 更新上面的信息
	 */
	private void updateHeadView(){
		if(mGoodsDetails == null) return;
		//HttpManager.getInstance(this).volleyImageRequest(
		//		HttpManager.URL + mGoodsDetails.getGoodsImg(), 
		//		mDetailImage, R.drawable.default_icon, R.drawable.default_icon);
		mBitmapCache.loadBitmaps(mDetailImage, mGoodsDetails.getGoodsImg());
		mDetailName.setText(mGoodsDetails.getGoodsName());
		mDetailPrice.setText("￥" + UnitConvertUtil.getSwitchedMoney(mGoodsDetails.getUnitPrice()) + "元/" 
				+ UnitConvertUtil.getSwichedUnit(1000, mGoodsDetails.getUnit()));
		mDetailDescript.setText(mGoodsDetails.getMemo());
		setTextColor();
	}
	
	/**
	 * 更新底下相关菜谱列表
	 */
	private void updateRelationView(){
		mConnCookBookList.setAdapter(new CookBookListAdapter(this, mRelationRecipes, mBitmapCache));
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
		builder.setSpan(redSpan, 0, totalSpanSart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
		mBitmapCache.fluchCache();
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}

	class MyGoodsModelResult extends GoodsModelResult{
		@Override
		public void requestGoodsDetailResult(GoodsDetail goodsDetail) {
			mGoodsDetails = goodsDetail;
			handler.sendEmptyMessage(HAND_DATA_COMPLTED);
		}
	}
	
	class MyCollectionModelResult extends CollectionModelResult{
		@Override
		public void requestCollectionGoodsResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				ToastUtil.showToast(VegetableDetailActivity.this, "收藏成功");
			}else{
				ToastUtil.showToast(VegetableDetailActivity.this, msg);
			}
		}
	}
	
	class MyMenuModelResult extends MenuModelResult{
		@Override
		public void requestMenuByGoodsResult(
				List<RelationRecipe> relationRecipes) {
			mRelationRecipes = relationRecipes;
			handler.sendEmptyMessage(HAND_RELATION_RECIPE_COMPLETED);
		}
	}
}
