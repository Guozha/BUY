package com.guozha.buy.activity.mpage;

import android.os.Bundle;
import android.widget.GridView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.PreSpecialGridAdapter;
import com.guozha.buy.global.net.HttpManager;

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
		initData();
		initView();
	}
	
	private void initData(){
		String paramPath = "goods/special?frontTypeId=";
		HttpManager.getInstance(this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				
			}
		});
		
	}
	
	private void initView(){
		
		mGridView = (GridView) findViewById(R.id.pre_special_gridlist);
		
		mGridView.setAdapter(new PreSpecialGridAdapter(this));
	}
}
