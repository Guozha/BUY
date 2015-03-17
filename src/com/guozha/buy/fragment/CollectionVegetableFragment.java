package com.guozha.buy.fragment;

import com.guozha.buy.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 菜品收藏
 * @author PeggyTong
 *
 */
public class CollectionVegetableFragment extends Fragment{

	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_collection_vegetable, container, false);
		//initView(view);
		return view;
	}
	
}
