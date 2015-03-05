package com.guozha.buy.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.ListVegetableActivity;
import com.guozha.buy.adapter.MenuExpandListAapter;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMarket extends MainTabBaseFragment implements OnClickListener{
	
	private static final String PAGE_NAME = "MarketPage";
	
	private View mView;
	
	private View mTopExpandMenuButton;
	private ExpandableListView mMenuList;
	
	private String[] mGroupMenus;
	private List<String>[] mChildMenus;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_maintab_market, container, false);
		
		initMenuData();
		
		initView(mView);
		
		return mView;
	}
	
	private void initMenuData(){
		mGroupMenus = new String[]{"Group1", "Group2", "Group3", "Group4"};
		
		mChildMenus = new ArrayList[mGroupMenus.length];
		List<String> menus;
		for(int i = 0; i < mGroupMenus.length; i++){
			menus = new ArrayList<String>();
			menus.add("Group" + i + "Child1");
			menus.add("Group" + i + "Child2");
			menus.add("Group" + i + "Child3");
			menus.add("Group" + i + "Child4");
			menus.add("Group" + i + "Child5");
			
			mChildMenus[i] = menus;
		}
	}
	
	/**
	 * 初始化View
	 * @param view
	 */
	private void initView(View view){
		mTopExpandMenuButton = view.findViewById(R.id.market_expand_menu_button);
		mTopExpandMenuButton.setOnClickListener(this);
		
		mMenuList = (ExpandableListView) view.findViewById(R.id.market_item_menu_list);
		mMenuList.setAdapter(new MenuExpandListAapter(getActivity(), mGroupMenus, mChildMenus));
		
		
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.market_expand_menu_button:
			String tag = (String) view.getTag();	
			if("unexpand".equals(tag)){
				mMenuList.setVisibility(View.VISIBLE);
				view.setTag("expand");
			}else{
				mMenuList.setVisibility(View.GONE);
				view.setTag("unexpand");
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			LogUtil.e("Goods_inVisible");
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
}
