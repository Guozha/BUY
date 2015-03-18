package com.guozha.buy.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.CartItemListAdapter;
import com.guozha.buy.entry.cart.CartBaseItem;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartCookMaterial;
import com.guozha.buy.entry.cart.CartMarketItem;
import com.guozha.buy.entry.cart.CartBaseItem.CartItemType;

/**
 * 订单详情
 * @author PeggyTong
 *
 */
public class OrderDetailActivity extends BaseActivity{
	
	private ExpandableListView mOrderDetailList;
	
	private List<CartBaseItem> mOrderItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		customActionBarStyle("订单详情");
		initData();
		initView();
	}
	
	private void initView(){
		mOrderDetailList = (ExpandableListView) findViewById(R.id.expandable_order_detail_list);
		mOrderDetailList.setAdapter(new CartItemListAdapter(this, mOrderItems));
		//首次全部展开
		for (int i = 0; i < mOrderItems.size(); i++) {
		    mOrderDetailList.expandGroup(i);
		}
	}
	
	private void initData(){
		mOrderItems = new ArrayList<CartBaseItem>();
		
		//添加5个菜谱
		for(int i = 0; i < 5; i++){
			List<CartCookMaterial> cookMaterials = new ArrayList<CartCookMaterial>();
			for(int j = 0; j < 5; j++){
				CartCookMaterial cookMaterial = new CartCookMaterial(j + "", "西红柿", "3", "两");
				cookMaterials.add(cookMaterial);
			}
			CartCookItem cartCookItem = new CartCookItem(
					i + "", "西红柿炒鸡蛋", 1, "份", "5.7", cookMaterials);
			mOrderItems.add(cartCookItem);
		}
		
		//添加标题
		mOrderItems.add(new CartBaseItem(null, "逛菜场", -1, null, null, CartItemType.undefine));
		
		//添加10个逛菜场
		for(int i = 0; i < 10; i++){
			CartMarketItem cartMarketItem = new CartMarketItem(i + "", "新鲜猪肉", 14, "斤", "45.9");
			mOrderItems.add(cartMarketItem);
		}
	}
}
