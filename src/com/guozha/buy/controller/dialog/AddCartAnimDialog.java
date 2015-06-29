package com.guozha.buy.controller.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.guozha.buy.R;
import com.guozha.buy.view.AddCartAnimWindow;
import com.guozha.buy.view.AddCartAnimWindow.OnAnimEndListener;

public class AddCartAnimDialog extends Activity{
	
	private LinearLayout mOutLayout;
	private String mShowText = "+1";
	private boolean mIsTop;
	private AddCartAnimWindow addCartAnimWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_addcart_anim);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			mShowText = bundle.getString("showText");
			mIsTop = bundle.getBoolean("isTop");
		}
		mOutLayout = (LinearLayout) findViewById(R.id.add_cart_anim_linearlayout);
		addCartAnimWindow = new AddCartAnimWindow(mShowText, mIsTop, this);
		mOutLayout.addView(addCartAnimWindow);
		addCartAnimWindow.setOnAnimEndListener(new OnAnimEndListener() {
			@Override
			public void animEnd() {
				addCartAnimWindow.setVisibility(View.GONE);
				AddCartAnimDialog.this.finish();
			}
		});
	}
	
}
