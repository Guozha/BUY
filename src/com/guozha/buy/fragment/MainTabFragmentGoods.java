package com.guozha.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guozha.buy.R;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentGoods extends Fragment{
	
	private static final String PAGE_NAME = "GoodsPage";
	
	private int mMaxDataNum;
	
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_maintab_goods, container, false);
		initView(mView);
		
		return mView;
	}
	
	/**
	 * 初始化View
	 * @param view
	 */
	private void initView(View view){
		
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
			LogUtil.e("Goods_inVisible");
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
}
