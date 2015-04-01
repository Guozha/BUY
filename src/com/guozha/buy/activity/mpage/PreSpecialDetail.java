package com.guozha.buy.activity.mpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.cart.PreSpecialPayActivity;
import com.guozha.buy.activity.global.BaseActivity;

public class PreSpecialDetail extends BaseActivity{
	
	private TextView mPreSpecialTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prespecial_detail);
		customActionBarStyle("商品详情");
		
		initView();
	}
	
	private void initView(){
		findViewById(R.id.prespecial_buy_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PreSpecialDetail.this, PreSpecialPayActivity.class);
				startActivity(intent);
			}
		});
		
		mPreSpecialTitle = (TextView) findViewById(R.id.prespecial_title);
		
		SpannableStringBuilder builder = new SpannableStringBuilder(mPreSpecialTitle.getText());
		builder.setSpan(  
	            new ImageSpan(this, R.drawable.sale_tag_02, DynamicDrawableSpan.ALIGN_BOTTOM), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mPreSpecialTitle.setText(builder);
	}
}
