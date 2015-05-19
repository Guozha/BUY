package com.guozha.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guozha.buy.R;
import com.guozha.buy.global.MainPageInitDataManager;

/**
 * 发现
 * @author PeggyTong
 *
 */
public class MainTabFragmentFound extends MainTabBaseFragment{
	
	private static final String PAGE_NAE = "FoundPage";
	

	@Override
	public void loadDataCompleted(MainPageInitDataManager dataManager,
			int handerType) {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_found, container, false);
		initView(view); 
		return view;
	}
	
	private void initView(View view){
		
	}
}
