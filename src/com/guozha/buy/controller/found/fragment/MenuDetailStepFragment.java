package com.guozha.buy.controller.found.fragment;

import android.os.Bundle;
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
import com.guozha.buy.controller.BaseFragment;
import com.guozha.buy.util.DimenUtil;

/**
 * 菜谱详情 - 步骤
 * @author PeggyTong
 *
 */
public class MenuDetailStepFragment extends BaseFragment{
	
	private ListView mStepList;  //步骤列表
	private MenuDetailStepListAdapter mStepListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu_detail_step, container, false);
		initView(view); 
		return view;
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
		
		mStepListAdapter = new MenuDetailStepListAdapter(getActivity());
		mStepList.setAdapter(mStepListAdapter);
	}
}
