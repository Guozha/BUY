package com.guozha.buy.activity.market;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.CookBookListAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 菜品详情
 * @author PeggyTong
 *
 */
public class VegetableDetailActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "VegetableDetailPage";
	
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
