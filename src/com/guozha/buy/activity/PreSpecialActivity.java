package com.guozha.buy.activity;

import android.os.Bundle;
import android.widget.GridView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.PreSpecialGridAdapter;

/**
 * 预售/特供
 * @author PeggyTong
 *
 */
public class PreSpecialActivity extends BaseActivity{
	
	private GridView mGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_special);
		customActionBarStyle("特供");
		
		initView();
	}
	
	private void initView(){
		
		mGridView = (GridView) findViewById(R.id.pre_special_gridlist);
		
		mGridView.setAdapter(new PreSpecialGridAdapter(this));
	}
}
