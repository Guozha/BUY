package com.guozha.buy.controller.found.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.MenuDetailStepListAdapter;
import com.guozha.buy.entry.found.menu.MenuDetail;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.DimenUtil;

/**
 * 菜谱详情 - 步骤
 * @author PeggyTong
 *
 */
public class MenuDetailStepFragment extends BaseMenuDetailFragment{
	
	private static final int HAND_MENU_DETAIL_COMPLETED = 0x0001;
	
	private ListView mStepList;  //步骤列表
	private MenuDetailStepListAdapter mStepListAdapter;
	private MenuDetail mMenuDetail;
	private BitmapCache mBitmapCache = BitmapCache.getInstance();

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
		View view = inflater.inflate(R.layout.fragment_menu_detail_step, container, false);
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
		mStepList = (ListView) view.findViewById(R.id.menu_detail_step_list);
		TextView headText = new TextView(getActivity());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, DimenUtil.dp2px(getActivity(), 56));
		headText.setText("步骤");
		headText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		headText.setTextColor(getResources().getColor(R.color.color_app_base_25));
		headText.setGravity(Gravity.CENTER);
		headText.setLayoutParams(params);
		mStepList.addHeaderView(headText);
	}
	
	private void updateView(){
		if(mMenuDetail == null) return;
		if(mStepList == null) return;
		mStepListAdapter = new MenuDetailStepListAdapter(
				getActivity(), mMenuDetail.getMenuSteps(), mBitmapCache);
		mStepList.setAdapter(mStepListAdapter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
	}
}
