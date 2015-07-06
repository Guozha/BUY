package com.guozha.buy.controller.found.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.found.menu.MenuDetail;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.DimenUtil;

public class MenuDetailDescriptFragment extends BaseMenuDetailFragment{
	private static final int HAND_DETAIL_DATA_COMPLETED = 0x0001;
	
	private ImageView mHeadImg;
	private TextView mHeadTitle;
	private TextView mCookWay;  //方式
	private TextView mCookTime; //用时
	private TextView mCookCal;	//卡路里
	private TextView mDescript;
	private MenuDetail mMenuDetail;
	private BitmapCache mBitmapCache = BitmapCache.getInstance();
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DETAIL_DATA_COMPLETED:
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
		View view = inflater.inflate(R.layout.fragment_menu_detail_descript, container, false);
		initView(view);
		updateView();
		return view;
	}
	
	@Override
	public void sendMenuDetailData(MenuDetail menuDetail) {
		super.sendMenuDetailData(menuDetail);
		if(menuDetail == null) return;
		mMenuDetail = menuDetail;
		mHandler.sendEmptyMessage(HAND_DETAIL_DATA_COMPLETED);
	}
	
	private void initView(View view){
		if(view == null) return;
		mHeadImg = (ImageView) view.findViewById(R.id.menu_detail_head_img);
		mHeadTitle = (TextView) view.findViewById(R.id.menu_detail_head_title);
		int screenWidth = DimenUtil.getScreenWidth(getActivity());
		mHeadImg.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenWidth * 2 / 3));
		mCookWay = (TextView) view.findViewById(R.id.menu_detail_cookway);
		mCookTime = (TextView) view.findViewById(R.id.menu_detail_cooktime);
		mCookCal = (TextView) view.findViewById(R.id.menu_detail_cal);
		mDescript = (TextView) view.findViewById(R.id.menu_detail_descript);
	}
	
	private void updateView(){
		if(mMenuDetail == null) return;
		if(mHeadImg == null 
				|| mBitmapCache == null 
				|| mHeadTitle == null 
				|| mCookWay == null
				|| mCookCal == null 
				|| mDescript == null
		) return;
		mHeadImg.setImageResource(R.drawable.default_icon_large);
		mBitmapCache.loadBitmaps(mHeadImg, mMenuDetail.getMenuImg());
		mHeadTitle.setText(mMenuDetail.getMenuName());
		mCookWay.setText(mMenuDetail.getCookieWay());
		mCookTime.setText(mMenuDetail.getCookieTime() + "Min");
		mCookCal.setText(mMenuDetail.getCalories() + "大卡/100g");
		mDescript.setText(mMenuDetail.getMenuDesc());
	}	
	
	@Override
	public void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
	}
}
