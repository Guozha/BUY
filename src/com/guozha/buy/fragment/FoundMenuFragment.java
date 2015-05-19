package com.guozha.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.newfold.FoundMenuGridAdapter;

/**
 * 发现-菜谱
 * @author PeggyTong
 *
 */
public class FoundMenuFragment extends BaseFragment{
	
	private static final String PAGE_NAE = "FoundMenuPage";
	private GridView mFoundMenuList;
	private FoundMenuGridAdapter mMenuGridAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_found_menu, container, false);
		initView(view); 
		return view;
	}
	
	private void initView(View view){
		mFoundMenuList = (GridView) view.findViewById(R.id.found_menu_list);
		mMenuGridAdapter = new FoundMenuGridAdapter(getActivity());
	}
}
