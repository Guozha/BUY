package com.guozha.buy.controller.found.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.MenuDetailFoodListAdapter;
import com.guozha.buy.adapter.MenuDetailSoupListAdapter;
import com.guozha.buy.controller.BaseFragment;
import com.guozha.buy.util.DimenUtil;

/**
 * 菜谱详情 - 食材
 * @author PeggyTong
 *
 */
public class MenuDetailFoodFragment extends BaseFragment{
	
	private ListView mFoodList;
	private ListView mSoupList;
	private MenuDetailFoodListAdapter mFoudListAdapter;
	private MenuDetailSoupListAdapter mSoupListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu_detail_food, container, false);
		initView(view); 
		return view;
	}
	
	private void initView(View view){
		if(view == null) return;
		mFoodList = (ListView) view.findViewById(R.id.menu_detail_food_list);
		mSoupList = (ListView) view.findViewById(R.id.menu_detail_soup_list);
		
		//添加头部
		TextView foodListHead = new TextView(getActivity());
		TextView soupListHead = new TextView(getActivity());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 
				DimenUtil.dp2px(getActivity(), 56));
		foodListHead.setGravity(Gravity.CENTER);
		foodListHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		foodListHead.setTextColor(getResources().getColor(R.color.color_app_base_25));
		foodListHead.setLayoutParams(params);
		foodListHead.setText("食材");
		soupListHead.setGravity(Gravity.CENTER);
		soupListHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		soupListHead.setTextColor(getResources().getColor(R.color.color_app_base_25));
		soupListHead.setLayoutParams(params);
		soupListHead.setText("辅料");
		mFoodList.addHeaderView(foodListHead);
		mSoupList.addHeaderView(soupListHead);
		
		mFoudListAdapter = new MenuDetailFoodListAdapter(getActivity());
		mSoupListAdapter = new MenuDetailSoupListAdapter(getActivity());
		mFoodList.setAdapter(mFoudListAdapter);
		mSoupList.setAdapter(mSoupListAdapter);
	}
}
