package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.CustomApplication;
import com.guozha.buy.activity.cart.PlanceOrderActivity;
import com.guozha.buy.adapter.CartItemListAdapter;
import com.guozha.buy.dialog.CustomDialog;
import com.guozha.buy.entry.cart.CartBaseItem;
import com.guozha.buy.entry.cart.CartBaseItem.CartItemType;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartMarketItem;
import com.guozha.buy.entry.cart.CartTotalData;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.util.UnitConvertUtil;
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
	private CartItemListAdapter mCartItemListAdapter;
	private View mCartEmptyBg;
	
	private int mQuantity = 0;		//总商品个数
	private int mTotalPrice = 0;	//总额
	private int mServiceFree = 0;	//服务费
	private int mFreeGap = 0;		//还差多少免服务费
	private int mCurrentAddressId = 
			ConfigManager.getInstance().getChoosedAddressId();   //当前地址id(用来判断地址是否发生了改变）
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateViewData();
				break;
			case MainPageInitDataManager.HAND_INITDATA_MSG_CART_ITEM:
				setCartItemsData();
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
		initActionBar("购物车");
		initView(view);
		setCartItemsData();
		return view;
	}

	/**
	 * 改变数据格式
	 * @param cartTotalData
	 */
	private void exchangeDataFormat(CartTotalData cartTotalData) {
		if(mCartItems == null){
			mCartItems = new ArrayList<CartBaseItem>();
		}else{
			mCartItems.clear();
		}
		
		List<CartCookItem> cartCookItem = cartTotalData.getMenuList();
		List<CartMarketItem> cartMarketItem = cartTotalData.getGoodsList();
		if(cartCookItem != null && !cartCookItem.isEmpty()){
			mCartItems.add(new CartBaseItem(-1, "菜谱", -1, 
					null, -1, -1, null, CartItemType.undefine));
			mCartItems.addAll(cartCookItem);
		}
		//添加标题
		if(cartMarketItem != null && !cartMarketItem.isEmpty()){
			mCartItems.add(new CartBaseItem(-1, "逛菜场", -1, 
					null, -1, -1, null, CartItemType.undefine));
			mCartItems.addAll(cartMarketItem);
		}
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
		view.findViewById(R.id.cart_to_order_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				requestTurnChooseTime();
			}
		});
		
		mCartEmptyBg = view.findViewById(R.id.cart_empty_bg);
		mCartItems = new ArrayList<CartBaseItem>();
		mCartItemListAdapter = new CartItemListAdapter(getActivity(), mCartItems);
		mCartList.setAdapter(mCartItemListAdapter);
	}

	/**
	 * 请求提交订单
	 */
	private void requestTurnChooseTime() {
		//TODO 判断
		if(mCartList == null || mCartItems.isEmpty()){
			final CustomDialog emptyNotify = new CustomDialog(MainTabFragmentCart.this.getActivity(), R.layout.dialog_cart_empty);
			emptyNotify.setDismissButtonId(R.id.cancel_button);
			emptyNotify.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//TODO
				}
			});
			return;
		}
		Intent intent = new Intent(MainTabFragmentCart.this.getActivity(), PlanceOrderActivity.class);
		intent.putExtra("totalPrice", mTotalPrice);
		intent.putExtra("serverPrice", mServiceFree);
		startActivity(intent);
	}
	
	/**
	 * 给视图添加数据
	 */
	private void updateViewData(){
		if(mCartEmptyBg == null || mCartList == null || mMesgTotal == null 
				|| mMesgServerMoney == null || mMesgFreeGap == null) return;
		if(mCartList == null || mCartItems == null || mCartItems.isEmpty()) {
			mCartEmptyBg.setVisibility(View.VISIBLE);
			mCartList.setVisibility(View.GONE);
			setBottomMessageText(0, 0, 0, 0);
			return;
		}
		mCartEmptyBg.setVisibility(View.GONE);
		mCartList.setVisibility(View.VISIBLE);
		
		//TODO
		if(mCartItemListAdapter == null){
			mCartItemListAdapter = new CartItemListAdapter(getActivity(), mCartItems);
			mCartList.setAdapter(mCartItemListAdapter);
		}else{
			mCartItemListAdapter.notifyDataSetChanged();
		}
		//首次全部展开
		for (int i = 0; i < mCartItems.size(); i++) {
		    mCartList.expandGroup(i);
		}
		setBottomMessageText(mQuantity, mTotalPrice, mServiceFree, mFreeGap);
	}

	private void setBottomMessageText(int quantity, int totalPrice, int serverFee, int gapFreeFee) {
		mMesgTotal.setText("共计" + mQuantity + "件 合计￥" + UnitConvertUtil.getSwitchedMoney(totalPrice));
		mMesgServerMoney.setText("预计服务费￥" + UnitConvertUtil.getSwitchedMoney(serverFee));
		gapFreeFee = gapFreeFee < 0 ? 0 : gapFreeFee;
		mMesgFreeGap.setText("离免服务费还差￥" + UnitConvertUtil.getSwitchedMoney(gapFreeFee));
		setTextColor();
	}
	
	@Override
	public void loadDataCompleted(MainPageInitDataManager dataManager, int handlerType) {
		mDataManager = dataManager;
		switch (handlerType) {
		case MainPageInitDataManager.HAND_INITDATA_MSG_CART_ITEM:  //购物车数据
			setCartItemsData();
			break;
		}
	}
	
	/**
	 * 设置购物车列表数据
	 */
	private void setCartItemsData(){
		if(mDataManager == null) {
			mDataManager = MainPageInitDataManager.getInstance(CustomApplication.getContext());
		}
		CartTotalData cartTotalData = mDataManager.getCartItems(null);
		if(cartTotalData == null) return;
		int totalPrice = cartTotalData.getTotalPrice();
		int freePrice = cartTotalData.getServiceFeePrice();
		//数量
		mQuantity = cartTotalData.getQuantity();
		mTotalPrice = cartTotalData.getTotalPrice();
		mServiceFree = cartTotalData.getCurrServiceFee();
		mFreeGap = freePrice > totalPrice ? freePrice - totalPrice : 0;
		exchangeDataFormat(cartTotalData);
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
		builder.setSpan(redSpan, 5, msgServerMoney.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mMesgServerMoney.setText(builder);
		
		String msgFreeGap = mMesgFreeGap.getText().toString();
		builder.clear();
		builder.append(msgFreeGap);
		builder.setSpan(redSpan, 7,msgFreeGap.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mMesgFreeGap.setText(builder);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			int addressId =ConfigManager.getInstance().getChoosedAddressId();
			if(mDataManager == null){
				mDataManager = MainPageInitDataManager.getInstance(CustomApplication.getContext());
			}
			if(mCurrentAddressId != addressId){
				mDataManager = MainPageInitDataManager.getInstance(CustomApplication.getContext());
				mDataManager.getCartItems(handler);
				mCurrentAddressId = addressId;
			}
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
