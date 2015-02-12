package com.guozha.buy.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;

public class ShopCarDownBar extends ViewGroup{

	public ShopCarDownBar(Context context) {
		super(context);
		
		addChild(context);
	}
	
	private void addChild(Context context){
		Button button = new Button(context);
		button.setText("°´Å¥");
		this.addView(button);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
}
