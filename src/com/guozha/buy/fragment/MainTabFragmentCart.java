package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.CartItemListAdapter;
import com.guozha.buy.entry.cart.CartBaseItem;
import com.guozha.buy.entry.cart.CartBaseItem.CartItemType;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartCookMaterial;
import com.guozha.buy.entry.cart.CartMarketItem;
import com.guozha.buy.global.MainPageInitDataManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 购物车
 * @author PeggyTong
 *
 */
public class MainTabFragmentCart extends MainTabBaseFragment{
	
	private static final String PAGE_NAME = "CartPage";
	
	private ExpandableListView mCartList;
	
	private TextView mMesgTotal;
	private TextView mMesgServerMoney;
	private TextView mMesgFreeGap;
	private List<CartBaseItem> mCartItems;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_cart, container, false);
		initData();
		initView(view);
		return view;
	}
	
	private void initData(){
		mCartItems = new ArrayList<CartBaseItem>();
		
		//添加5个菜谱
		for(int i = 0; i < 5; i++){
			List<CartCookMaterial> cookMaterials = new ArrayList<CartCookMaterial>();
			for(int j = 0; j < 5; j++){
				CartCookMaterial cookMaterial = new CartCookMaterial(j + "", "西红柿", "3", "两");
				cookMaterials.add(cookMaterial);
			}
			CartCookItem cartCookItem = new CartCookItem(
					i + "", "西红柿炒鸡蛋", 1, "份", "5.7", cookMaterials);
			mCartItems.add(cartCookItem);
		}
		
		//添加标题
		mCartItems.add(new CartBaseItem(null, "逛菜场", -1, null, null, CartItemType.undefine));
		
		//添加10个逛菜场
		for(int i = 0; i < 10; i++){
			CartMarketItem cartMarketItem = new CartMarketItem(i + "", "新鲜猪肉", 14, "斤", "45.9");
			mCartItems.add(cartMarketItem);
		}
	}
	
	
	/**
	 * 初始化界面
	 * @param view
	 */
	private void initView(View view){
		if(view == null) return;
		mCartList = (ExpandableListView) view.findViewById(R.id.expandable_cart_list);
		mCartList.setAdapter(new CartItemListAdapter(getActivity(), mCartItems));
		//首次全部展开
		for (int i = 0; i < mCartItems.size(); i++) {
		    mCartList.expandGroup(i);
		}
		
		mMesgTotal = (TextView) view.findViewById(R.id.cart_total_message);
		mMesgServerMoney = (TextView) view.findViewById(R.id.cart_server_money);
		mMesgFreeGap = (TextView) view.findViewById(R.id.cart_free_money_gap);
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
