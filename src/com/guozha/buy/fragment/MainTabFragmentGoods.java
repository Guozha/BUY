package com.guozha.buy.fragment;

import com.guozha.buy.R;
import com.guozha.buy.view.GoodsUpDownViewGroup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainTabFragmentGoods extends Fragment{
	
	private int mMaxDataNum;
	
	private View mView;
	private GoodsUpDownViewGroup mGoodsUpDownView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_maintab_goods, container, false);
		mGoodsUpDownView = (GoodsUpDownViewGroup) 
				mView.findViewById(R.id.goods_page_updown_view);
		return mView;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
		}else{
			//View不可见
			
		}
	}
}
