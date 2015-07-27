package com.guozha.buy.controller.found.fragment;

import java.util.Set;

import android.os.Bundle;
import android.os.Handler;
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
import com.guozha.buy.entry.found.menu.MenuDetail;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.LogUtil;

/**
 * 菜谱详情 - 食材
 * @author PeggyTong
 *
 */
public class MenuDetailFoodFragment extends BaseMenuDetailFragment{
	
	private static final int HAND_MENU_DETAIL_COMPLETED = 0x0001;
	private ListView mFoodList;
	private ListView mSoupList;
	private MenuDetailFoodListAdapter mFoudListAdapter;
	private MenuDetailSoupListAdapter mSoupListAdapter;
	private MenuDetail mMenuDetail;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_MENU_DETAIL_COMPLETED:
				updateView();
				break;
			default:
				break;
			}
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu_detail_food, container, false);
		initView(view);
		updateView();
		return view;
	}
	
	@Override
	public void sendMenuDetailData(MenuDetail menuDetail) {
		super.sendMenuDetailData(menuDetail);
		if(menuDetail == null) return;
		mMenuDetail = menuDetail;
		mHandler.sendEmptyMessage(HAND_MENU_DETAIL_COMPLETED);
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
	}
	
	private void updateView(){
		if(mMenuDetail == null) return;
		if(mFoodList == null || mSoupList == null) return;
		LogUtil.e("canChoose == " + mMenuDetail.getPackingFlag());
		boolean canChoose = true;
		if("1".equals(mMenuDetail.getPackingFlag())) canChoose = false;
		mFoudListAdapter = new MenuDetailFoodListAdapter(getActivity(), canChoose, mMenuDetail.getMenuGoods());
		mSoupListAdapter = new MenuDetailSoupListAdapter(getActivity(), mMenuDetail.getSeasonings());
		mFoodList.setAdapter(mFoudListAdapter);
		mSoupList.setAdapter(mSoupListAdapter);
	}
	
	@Override
	public Set<String> getCheckedIds() {
		if(mFoudListAdapter == null) return null;
		return mFoudListAdapter.getCheckedIds();
	}
}
