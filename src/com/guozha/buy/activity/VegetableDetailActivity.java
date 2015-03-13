package com.guozha.buy.activity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.CookBookListAdapter;

/**
 * 菜品详情
 * @author PeggyTong
 *
 */
public class VegetableDetailActivity extends BaseActivity{
	
	private ListView mConnCookBookList;
	private TextView mItemPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		customActionBarStyle("菜品详情");
		setContentView(R.layout.activity_vegetable_detail);
		
		initView();
	}
	
	private void initView(){
		mConnCookBookList = (ListView) 
				findViewById(R.id.vegetable_connect_cookbook_list);
		View head = getLayoutInflater().inflate(R.layout.vegetable_detail_list_head, null);
		mConnCookBookList.addHeaderView(head);
		mConnCookBookList.setAdapter(new CookBookListAdapter(this));
		
		mItemPrice = (TextView) findViewById(R.id.vegetable_detail_item_price);
		
		setTextColor();
	}
	
	/**
	 * 给字体设置红色
	 */
	private void setTextColor(){
		String msgTotal = mItemPrice.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(msgTotal);
		
		ForegroundColorSpan redSpan = new ForegroundColorSpan(
				getResources().getColor(R.color.color_app_base_1));
		int totalSpanSart = msgTotal.indexOf("/");
		builder.setSpan(redSpan, 0, totalSpanSart - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mItemPrice.setText(builder);
	}
}
