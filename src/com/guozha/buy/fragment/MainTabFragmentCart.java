package com.guozha.buy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.guozha.buy.R;
import com.guozha.buy.debug.DebugActivity;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentCart extends Fragment{
	
	private static final String PAGE_NAME = "CartPage";
	
	private Button turnToDebugpageButton;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_cart, container, false);
		initView(view);
		return view;
	}
	
	/**
	 * 初始化界面
	 * @param view
	 */
	private void initView(View view){
		if(view == null) return;
		turnToDebugpageButton = (Button) view.findViewById(R.id.turn_to_debugpage);
		turnToDebugpageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainTabFragmentCart.this.getActivity(), DebugActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
}
