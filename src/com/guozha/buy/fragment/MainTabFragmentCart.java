package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.adapter.CartItemListAdapter;
import com.guozha.buy.entry.cart.CartBaseItem;
import com.guozha.buy.entry.cart.CartBaseItem.CartItemType;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartMarketItem;
import com.guozha.buy.entry.cart.CartTotalData;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.umeng.analytics.MobclickAgent;

/**
 * 购物车
 * @author PeggyTong
 *
 */
public class MainTabFragmentCart extends MainTabBaseFragment{
	
	private static final String PAGE_NAME = "CartPage";
	private static final int HAND_DATA_COMPLETED = 0x0001;
	
	private ExpandableListView mCartList;
	private TextView mMesgTotal;
	private TextView mMesgServerMoney;
	private TextView mMesgFreeGap;
	private List<CartBaseItem> mCartItems;
	
	private int mQuantity;		//总商品个数
	private int mTotalPrice;	//总额
	private int mServiceFree;	//服务费
	private int mFreeGap;		//还差多少免服务费
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateViewData();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_cart, container, false);
		initView(view);
		initData();
		return view;
	}
	
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("cart/list")
		.setParams("userId", userId);
		HttpManager.getInstance(getActivity()).volleyRequestByPost(
				HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				CartTotalData cartTotalData = gson.fromJson(response, new TypeToken<CartTotalData>() { }.getType());
				exchangeDataFormat(cartTotalData);
			}
		});
	}

	/**
	 * 改变数据格式
	 * @param cartTotalData
	 */
	private void exchangeDataFormat(CartTotalData cartTotalData) {
		//数量
		mQuantity = cartTotalData.getQuantity();
		//总额
		mServiceFree = cartTotalData.getTotalPrice();
		//服务费
		mServiceFree = cartTotalData.getServiceFee();
		//距离免服务费还剩多少
		mFreeGap = cartTotalData.getServiceFee() - cartTotalData.getTotalPrice();
		
		mCartItems = new ArrayList<CartBaseItem>();
		List<CartCookItem> cartCookItem = cartTotalData.getMenuList();
		List<CartMarketItem> cartMarketItem = cartTotalData.getGoodsList();
		if(cartCookItem != null && !cartCookItem.isEmpty()){
			mCartItems.add(new CartBaseItem(-1, "菜谱", -1, 
					null, -1, -1, null, CartItemType.undefine));
		}
		mCartItems.addAll(cartCookItem);
		//添加标题
		if(cartMarketItem != null && !cartMarketItem.isEmpty()){
			mCartItems.add(new CartBaseItem(-1, "逛菜场", -1, 
					null, -1, -1, null, CartItemType.undefine));
		}
		mCartItems.addAll(cartMarketItem);
		handler.sendEmptyMessage(HAND_DATA_COMPLETED);
	}
	
	/**
	 * 初始化界面
	 * @param view
	 */
	private void initView(View view){
		if(view == null) return;
		mCartList = (ExpandableListView) view.findViewById(R.id.expandable_cart_list);	
		mMesgTotal = (TextView) view.findViewById(R.id.cart_total_message);
		mMesgServerMoney = (TextView) view.findViewById(R.id.cart_server_money);
		mMesgFreeGap = (TextView) view.findViewById(R.id.cart_free_money_gap);
	}
	
	/**
	 * 给视图添加数据
	 */
	private void updateViewData(){
		if(mCartList == null || mCartItems == null) return;
		mCartList.setAdapter(new CartItemListAdapter(getActivity(), mCartItems));
		//首次全部展开
		for (int i = 0; i < mCartItems.size(); i++) {
		    mCartList.expandGroup(i);
		}
		mMesgTotal.setText("共计" + mQuantity + "件 合计￥" + mTotalPrice);
		mMesgServerMoney.setText("预计服务费" + mServiceFree);
		mMesgFreeGap.setText("离免服务费还差" + mFreeGap + "元");
		setTextColor();
	}
	
	@Override
	public void loadDataCompleted(MainPageInitDataManager dataManager, int handlerType) {
		
	}

	/**
	 * 设置文字颜色
	 */
	private void setTextColor() {
		String msgTotal = mMesgTotal.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(msgTotal);
		
		ForegroundColorSpan redSpan = new ForegroundColorSpan(
				getResources().getColor(R.color.color_app_base_1));
		int totalSpanSart = msgTotal.indexOf("￥");
		builder.setSpan(redSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(redSpan, totalSpanSart, msgTotal.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mMesgTotal.setText(builder);
		
		String msgServerMoney = mMesgServerMoney.getText().toString();
		builder.clear();
		builder.append(msgServerMoney);
		builder.setSpan(redSpan, 5, msgServerMoney.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mMesgServerMoney.setText(builder);
		
		String msgFreeGap = mMesgFreeGap.getText().toString();
		builder.clear();
		builder.append(msgFreeGap);
		builder.setSpan(redSpan, 7,msgFreeGap.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mMesgFreeGap.setText(builder);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
		    //初始化ActionBar	
			initActionBar(getActivity().getActionBar());
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
	
	/**
	 * 初始化ActionBar
	 * @param actionbar
	 */
	private void initActionBar(ActionBar actionbar) {
		if(actionbar == null) return;
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.actionbar_base_view);
		TextView title = (TextView) actionbar.getCustomView().findViewById(R.id.title);
		title.setText("购物车");
	}
}
