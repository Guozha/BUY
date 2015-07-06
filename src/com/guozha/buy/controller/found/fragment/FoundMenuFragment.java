package com.guozha.buy.controller.found.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.FoundMenuListAdapter;
import com.guozha.buy.controller.BaseFragment;
import com.guozha.buy.entry.found.MenuFirstType;
import com.guozha.buy.model.FoundModel;
import com.guozha.buy.model.result.FoundModelResult;
import com.umeng.analytics.MobclickAgent;

/**
 * 发现-菜谱
 * @author PeggyTong
 *
 */
public class FoundMenuFragment extends BaseFragment{
	
	private static final String PAGE_NAME = "菜谱分类";
	private static final int HAND_MENU_TYPES_COMPLETED = 0x0001;
	private ListView mFoundMenuList;
	private FoundMenuListAdapter mFoundMenuListAdapter;
	private List<MenuFirstType> mMenuFirstTypes = new ArrayList<MenuFirstType>();
	private FoundModel mFoundModel = new FoundModel(new MyFoundModelResult());
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_MENU_TYPES_COMPLETED:
				mFoundMenuListAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_found_menu, container, false);
		initView(view); 
		initData();
		return view;
	}
	
	private void initView(View view){
		mMenuFirstTypes.clear();
		mFoundMenuList = (ListView) view.findViewById(R.id.found_menu_list);
		mFoundMenuListAdapter = new FoundMenuListAdapter(getActivity(), mMenuFirstTypes);
		mFoundMenuList.setAdapter(mFoundMenuListAdapter);
	}
	
	private void initData(){
		mFoundModel.requestFoundMenuTypes(getActivity());
	}
	
	class MyFoundModelResult extends FoundModelResult{
		@Override
		public void requestFoundMenuTypesResult(
				List<MenuFirstType> menuFirstTypes) {
			if(menuFirstTypes == null) return;
			mMenuFirstTypes.addAll(menuFirstTypes);
			mHandler.sendEmptyMessage(HAND_MENU_TYPES_COMPLETED);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(PAGE_NAME); //统计页面
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(PAGE_NAME); 
	}
}
