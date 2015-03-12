 package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;

import com.guozha.buy.R;
import com.guozha.buy.activity.ChooseMenuActivity;
import com.guozha.buy.adapter.MarketItemListAdapter;
import com.guozha.buy.adapter.MenuExpandListAapter;
import com.guozha.buy.entry.VegetableInfo;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.view.AnimatedExpandableListView;
import com.guozha.buy.view.CustomListView;
import com.umeng.analytics.MobclickAgent;

/**
 * 逛菜场
 * @author PeggyTong
 *
 */
public class MainTabFragmentMarket extends MainTabBaseFragment implements OnClickListener,OnTouchListener{
	
	private static final String PAGE_NAME = "MarketPage";
	
	private View mView;
	
	private View mTopExpandMenuButton;
	private AnimatedExpandableListView mMenuList;
	
	private String[] mGroupMenus;
	private List<String>[] mChildMenus;
	
	private CustomListView mItemList;
	
	private Animation mInAnimation;
	private Animation mOutAnimation;
	
	private ImageView mMenuArrowIcon;
	
	private View mQuickInView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_maintab_market, container, false);
		
		//菜单出入动画
		mInAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.market_menu_in_anim);
		mOutAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.market_menu_out_anim);
			
		initMenuData();
		
		initView(mView);
		
		return mView;
	}
	
	/**
	 * 获取菜单数据
	 */
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
		if(view == null) return;
		//顶部按钮
		mTopExpandMenuButton = view.findViewById(R.id.market_expand_menu_button);
		mTopExpandMenuButton.setOnClickListener(this);
		
		//菜单类目可展开列表
		mMenuList = (AnimatedExpandableListView) view.findViewById(R.id.market_item_menu_list);
		mMenuList.setAdapter(new MenuExpandListAapter(getActivity(), mGroupMenus, mChildMenus));
		
		mMenuList.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (mMenuList.isGroupExpanded(groupPosition)) {
                    mMenuList.collapseGroupWithAnimation(groupPosition);
                } else {
                    mMenuList.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
            
        });
		
		View header = LayoutInflater.from(this.getActivity())
				.inflate(R.layout.market_list_item_header, null);
		//界面分类列表
		mItemList = (CustomListView) view.findViewById(R.id.market_itemlist);
		mItemList.setItemsCanFocus(true);
		mItemList.addHeaderView(header);
		mItemList.setAdapter(new MarketItemListAdapter(this.getActivity(), 
				new ArrayList<String>(), new ArrayList<VegetableInfo[]>()));
		
		//箭头
		mMenuArrowIcon = (ImageView) view.findViewById(R.id.market_menu_item_arrow_icon);
		mQuickInView = view.findViewById(R.id.market_quick_in_view);
		
		mItemList.setOnListViewEventListener(new CustomListView.OnListViewEventListener() {
			
			@Override
			public void upDownEvent(boolean isUp) {
				if(isUp){
					mQuickInView.setVisibility(View.INVISIBLE);
				}else{
					mQuickInView.setVisibility(View.VISIBLE);
				}
			}
		});
		
		view.findViewById(R.id.choose_menu_custom).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.market_expand_menu_button:
			expandMenuAction(view);
			break;
		case R.id.choose_menu_custom:
			Intent intent = new Intent(getActivity(), ChooseMenuActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 展开和收起菜单
	 * @param view
	 */
	private void expandMenuAction(View view) {
		String tag = (String) view.getTag();	
		if("unexpand".equals(tag)){
			mMenuList.setVisibility(View.VISIBLE);
			mMenuArrowIcon.setImageResource(R.drawable.main_menu_up);
			view.setTag("expand");
			mMenuList.startAnimation(mInAnimation);
		}else{
			mMenuList.setVisibility(View.GONE);
			view.setTag("unexpand");
			mMenuArrowIcon.setImageResource(R.drawable.main_menu_down);
			mMenuList.startAnimation(mOutAnimation);
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			//初始化ActionBar
			initActionBar(getActivity().getActionBar());
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			LogUtil.e("Goods_inVisible");
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
	
	/**
	 * 初始化ActionBar
	 * @param actionbar
	 */
	private void initActionBar(ActionBar actionbar){
		if(actionbar == null) return;
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.actionbar_market_custom_view);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		LogUtil.e("OnTouch。。。");
		return false;
	}
}
