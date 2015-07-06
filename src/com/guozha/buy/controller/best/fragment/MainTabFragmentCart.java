package com.guozha.buy.controller.best.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.guozha.buy.adapter.CartItemListAdapter;
import com.guozha.buy.adapter.CartItemListAdapter.CartItemChanged;
import com.guozha.buy.controller.MainActivity;
import com.guozha.buy.controller.cart.ChooseCartAddressActivity;
import com.guozha.buy.controller.cart.PlanceOrderActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.entry.cart.CartBaseItem;
import com.guozha.buy.entry.cart.CartBaseItem.CartItemType;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartMarketItem;
import com.guozha.buy.entry.cart.CartTotalData;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.OrderModel;
import com.guozha.buy.model.ShopCartModel;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.OrderModelResult;
import com.guozha.buy.model.result.ShopCartModelResult;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 购物车
 * @author PeggyTong
 *
 */
public class MainTabFragmentCart extends MainTabBaseFragment{
	private static final String PAGE_NAME = "购物车";
	private static final int HAND_DATA_COMPLETED = 0x0001;
	private static final int HAND_REFRESH_DATA = 0x0002;
	private static final int HAND_TO_DELETE_ITEM = 0x0003;
	private static final int HAND_CHECKED_PRICE = 0x0004;
	private static final int HAND_ADDRESS_COMPLETED = 0x0005;
	
	private ExpandableListView mCartList;
	private TextView mMesgTotal;
	private TextView mMesgServerMoney;
	private TextView mMesgFreeGap;
	private List<CartBaseItem> mCartItems;
	private CartItemListAdapter mCartItemListAdapter;
	private View mCartEmptyBg;
	private View mCartAddressArea;
	private TextView mCartAddress;
	//private int mQuantity = 0;		//总商品个数
	private int mTotalPrice = 0;	//总额
	private int mServiceFree = 0;	//服务费
	private int mFreeGap = 0;		//还差多少免服务费
	private AddressInfo mAddressInfo;
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	private OrderModel mOrderModel = new OrderModel(new MyOrderModelResult()); 
	private ShopCartModel mShopCartModel = new ShopCartModel(new MyShopCartModelResult());
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateViewData();
				break;
			case HAND_REFRESH_DATA:
				initData();
				break;
			case HAND_TO_DELETE_ITEM:
				clickDeleteButton(msg.arg1);
				break;
			case HAND_CHECKED_PRICE:
				Intent intent = new Intent(MainTabFragmentCart.this.getActivity(), PlanceOrderActivity.class);
				intent.putExtra("totalPrice", mTotalPrice);
				intent.putExtra("serverPrice", mServiceFree);
				intent.putExtra("addressInfo", mAddressInfo);
				startActivity(intent);
				break;
			case HAND_ADDRESS_COMPLETED:
				if(mAddressInfo == null){
					mCartAddressArea.setVisibility(View.GONE);
				}else{
					mCartAddressArea.setVisibility(View.VISIBLE);
					mCartAddress.setText(mAddressInfo.getBuildingName() + mAddressInfo.getDetailAddr());
				}
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
		initActionBar(PAGE_NAME);
		initView(view);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		initData();
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
			private long beginTimeMillis;
			@Override
			public void onClick(View v) {
				if(System.currentTimeMillis() - beginTimeMillis > 3000){
					beginTimeMillis = System.currentTimeMillis();
					requestTurnChooseTime();
				}else{
					ToastUtil.showToast(MainTabFragmentCart.this.getActivity(), "请不要重复提交");
				}
			}
		});
		
		mCartEmptyBg = view.findViewById(R.id.cart_empty_bg);
		mCartItems = new ArrayList<CartBaseItem>();
		mCartItemListAdapter = new CartItemListAdapter(getActivity(), mCartItems);
		mCartList.setAdapter(mCartItemListAdapter);
		mCartItemListAdapter.setCartItemChangedListener(new CartItemChanged() {
			@Override
			public void changed() {
				handler.sendEmptyMessage(HAND_REFRESH_DATA);
			}

			@Override
			public void delete(int cartId) {
				Message message = new Message();
				message.arg1 = cartId;
				message.what = HAND_TO_DELETE_ITEM;
				handler.sendMessage(message);
			}
		});
		
		mCartAddressArea = view.findViewById(R.id.cart_address_area);
		mCartAddress = (TextView) view.findViewById(R.id.cart_address);
		mCartAddressArea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ChooseCartAddressActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 点击删除按钮
	 * @param view
	 */
	private void clickDeleteButton(final int cartId) {
		final CustomDialog deleteDialog = new CustomDialog(getActivity(), R.layout.dialog_delete_notify);
		deleteDialog.setDismissButtonId(R.id.cancel_button);
		deleteDialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				deleteDialog.dismiss();
				requestDeleteCartItem(cartId);
			}
		});
	}
	
	/**
	 * 请求删除数据
	 * @param view
	 */
	private void requestDeleteCartItem(int cartId) {
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(getActivity());
		if(token == null) return;
		mShopCartModel.requestDeleteCart(getActivity(), cartId, userId, token);
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
				public void onClick(View view) {
					//TODO
					if(mOnRequestTurnItem != null){
						mOnRequestTurnItem.turnItem(MainActivity.FRAGMENT_MARKET_INDEX);
					}
					emptyNotify.dismiss();
				}
			});
			return;
		}
		String token = ConfigManager.getInstance().getUserToken(getActivity());
		if(token == null) return;
		int addressId = ConfigManager.getInstance().getChoosedAddressId(getActivity());
		if(addressId == -1) return;
		int userId = ConfigManager.getInstance().getUserId();
		mOrderModel.requestOrderConfirm(getActivity(), token, userId, addressId);
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
			setBottomMessageText(0, 0, 0);
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
		setBottomMessageText(mTotalPrice, mServiceFree, mFreeGap);
	}

	private void setBottomMessageText(int totalPrice, int serverFee, int gapFreeFee) {
		mMesgTotal.setText("合计￥" + UnitConvertUtil.getSwitchedMoney(totalPrice));
		mMesgServerMoney.setText("预计服务费￥" + UnitConvertUtil.getSwitchedMoney(serverFee));
		gapFreeFee = gapFreeFee < 0 ? 0 : gapFreeFee;
		if(gapFreeFee == 0){
			mMesgFreeGap.setVisibility(View.INVISIBLE);
		}else{
			mMesgFreeGap.setVisibility(View.VISIBLE);
			mMesgFreeGap.setText(",离免服务费还差￥" + UnitConvertUtil.getSwitchedMoney(gapFreeFee));
		}
		setTextColor();
	}
	
	private void initData(){
		mCartItems.clear();
		int userId = ConfigManager.getInstance().getUserId();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		if(addressId == -1) return;
		mShopCartModel.requestListCartItem(getActivity(), userId, addressId);
		mUserModel.requestListAddress(getActivity(), userId);
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
	
	class MyShopCartModelResult extends ShopCartModelResult{
		@Override
		public void requestListCartItemResult(CartTotalData cartTotalData) {
			if(cartTotalData == null) return;
			int totalPrice = cartTotalData.getTotalPrice();
			int freePrice = cartTotalData.getServiceFeePrice();
			//数量
			//mQuantity = cartTotalData.getQuantity();
			mTotalPrice = cartTotalData.getTotalPrice();
			mServiceFree = cartTotalData.getCurrServiceFee();
			mFreeGap = freePrice > totalPrice ? freePrice - totalPrice : 0;
			exchangeDataFormat(cartTotalData);
		}
		
		@Override
		public void requestDeleteCartResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				handler.sendEmptyMessage(HAND_REFRESH_DATA);
			}else{
				ToastUtil.showToast(getActivity(), msg);
			}
		}
	}
	
	class MyOrderModelResult extends OrderModelResult{
		@Override
		public void requestOrderConfirmResult(String returnCode, String msg,
				int totalPrice, int serviceFee) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				mServiceFree = serviceFee;
				mTotalPrice = totalPrice;
				handler.sendEmptyMessage(HAND_CHECKED_PRICE);
			}else{
				ToastUtil.showToast(getActivity(), msg);
				initData();
			}
		}
	}
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestListAddressResult(List<AddressInfo> addressInfos) {
			//设置地址信息
			if(addressInfos != null){
				for(int i = 0; i < addressInfos.size(); i++){
					AddressInfo addressInfo = addressInfos.get(i);
					if(addressInfo.getAddressId() == ConfigManager.getInstance().getChoosedAddressId()){
						mAddressInfo = addressInfo;
						handler.sendEmptyMessage(HAND_ADDRESS_COMPLETED);
					}
				}
			}
		}
	}

	@Override
	protected String getPageName() {
		return PAGE_NAME;
	}
}
