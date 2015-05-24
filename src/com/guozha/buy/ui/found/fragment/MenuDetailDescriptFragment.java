package com.guozha.buy.ui.found.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.ui.BaseFragment;
import com.guozha.buy.util.DimenUtil;

public class MenuDetailDescriptFragment extends BaseFragment{
	
	private ImageView mHeadImg;
	private TextView mHeadTitle;
	private TextView mCookWay;  //方式
	private TextView mCookTime; //用时
	private TextView mCookCal;	//卡路里
	private TextView mDescript;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu_detail_descript, container, false);
		initView(view); 
		return view;
	}
	
	private void initView(View view){
		if(view == null) return;
		
		mHeadImg = (ImageView) view.findViewById(R.id.menu_detail_head_img);
		mHeadTitle = (TextView) view.findViewById(R.id.menu_detail_head_title);
		int screenWidth = DimenUtil.getScreenWidth(getActivity());
		mHeadImg.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenWidth / 2));
		
		mCookWay = (TextView) view.findViewById(R.id.menu_detail_cookway);
		mCookTime = (TextView) view.findViewById(R.id.menu_detail_cooktime);
		mCookCal = (TextView) view.findViewById(R.id.menu_detail_cal);
		mDescript = (TextView) view.findViewById(R.id.menu_detail_descript);
		
		mHeadImg.setImageResource(R.drawable.temp_subject_item_imag);
		mHeadTitle.setText("麻辣香锅");
		mCookWay.setText("煮、炒");
		mCookTime.setText("20min");
		mCookCal.setText("300卡路里");
		mDescript.setText("麻辣香锅里，这是一种让人留恋的美食，它给了人们太多的味蕾上的快感和心底大块的温暖。菜里的每一样菜都有一种滋味，在四川独有的调料之间，经过幽微细致的搭配，衬出了每一种不同菜料的特质，复杂而多感。");
	}
}
