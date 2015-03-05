package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.MarketItemListAdapter;
import com.guozha.buy.adapter.MenuExpandListAapter;
import com.guozha.buy.entry.VegetableInfo;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.view.AnimatedExpandableListView;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMarket extends MainTabBaseFragment implements OnClickListener{
	
	private static final String PAGE_NAME = "MarketPage";
	
	private View mView;
	
	private View mTopExpandMenuButton;
	private AnimatedExpandableListView mMenuList;
	
	private String[] mGroupMenus;
	private List<String>[] mChildMenus;
	
	private ListView mItemList;
	
	private Animation mInAnimation;
	private Animation mOutAnimation;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_maintab_market, container, false);
		
		mInAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.market_menu_in_anim);
		mOutAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.market_menu_out_anim);
				
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
		
		mMenuList = (AnimatedExpandableListView) view.findViewById(R.id.market_item_menu_list);
		mMenuList.setAdapter(new MenuExpandListAapter(getActivity(), mGroupMenus, mChildMenus));
		
		mMenuList.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group 
                // expansion/collapse.
                if (mMenuList.isGroupExpanded(groupPosition)) {
                    mMenuList.collapseGroupWithAnimation(groupPosition);
                } else {
                    mMenuList.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
            
        });
		
		mItemList = (ListView) view.findViewById(R.id.market_itemlist);
		mItemList.setAdapter(new MarketItemListAdapter(this.getActivity(), 
				new ArrayList<String>(), new ArrayList<VegetableInfo[]>()));
		
		
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.market_expand_menu_button:
			String tag = (String) view.getTag();	
			if("unexpand".equals(tag)){
				mMenuList.setVisibility(View.VISIBLE);
				view.setTag("expand");
				mMenuList.startAnimation(mInAnimation);
			}else{
				mMenuList.setVisibility(View.GONE);
				view.setTag("unexpand");
				mMenuList.startAnimation(mOutAnimation);
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
