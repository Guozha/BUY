 package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.ChooseMenuActivity;
import com.guozha.buy.activity.market.ListVegetableActivity;
import com.guozha.buy.adapter.MarketItemListAdapter;
import com.guozha.buy.adapter.MenuExpandListAapter;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.MarketHomeItem;
import com.guozha.buy.entry.market.MarketHomePage;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.view.AnimatedExpandableListView;
import com.guozha.buy.view.CustomListView;
import com.umeng.analytics.MobclickAgent;

/**
 * 逛菜场
 * @author PeggyTong
 *
 */
public class MainTabFragmentMarket extends MainTabBaseFragment implements OnClickListener,OnScrollListener{
	
	private static final String PAGE_NAME = "MarketPage";
	private static final int HANDLER_MENU_ITEM_MSG_WHAT = 0x0001;
	
	private View mView;
	
	private View mTopExpandMenuButton;
	private AnimatedExpandableListView mMenuList;
	private MenuExpandListAapter mMenuExpandListAapter;
	private MarketItemListAdapter mMarketItemListAdapter;
	
	private List<GoodsItemType> mGoodsItemTypes;  //菜品类目菜单数据
	private List<MarketHomeItem> mMarketHomeItems = new ArrayList<MarketHomeItem>();	//逛菜场主页的列表
	
	private CustomListView mItemList;
	private int mFirstVisibleItem;  //屏幕上可见的第一条
	
	private Animation mInAnimation;
	private Animation mOutAnimation;
	
	private ImageView mMenuArrowIcon;
	private View mQuickInView;
	
	private int mTotalPageSize;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_maintab_market, container, false);
		
		//菜单出入动画
		mInAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.market_menu_in_anim);
		mOutAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.market_menu_out_anim);
			
		initView(mView);
		
		return mView;
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
		//mMenuExpandListAapter = new MenuExpandListAapter(getActivity(), mGoodsItemTypes);
		//mMenuList.setAdapter(mMenuExpandListAapter);
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
		
		mMenuList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//TODO 暂时这样写着，为了测试接口
				Intent intent = new Intent(getActivity(), ListVegetableActivity.class);
				startActivity(intent);
				
				return false;
			}
		});
		
		
		setGoodsItemTypeData();
		
		View header = LayoutInflater.from(this.getActivity())
				.inflate(R.layout.market_list_item_header, null);
		//界面分类列表
		mItemList = (CustomListView) view.findViewById(R.id.market_itemlist);
		mItemList.setItemsCanFocus(true);
		mItemList.addHeaderView(header);
		
		mItemList.setOnScrollListener(this);
		
		setMarketHomeData();
		
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
	public void loadDataCompleted(MainPageInitDataManager dataManager, int handlerType) {
		mDataManager = dataManager;
		switch (handlerType) {
		case MainPageInitDataManager.HAND_INITDATA_MSG_ITEMTYPE:  //菜单
			setGoodsItemTypeData();
			break;
		case MainPageInitDataManager.HAND_INITDATA_MSG_MARKETHOME: //列表
			setMarketHomeData();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 获取菜单条目列表数据
	 */
	private void setGoodsItemTypeData(){
		if(mDataManager == null) {
			return;
		}
		mGoodsItemTypes = mDataManager.getGoodsItemType(null);
		if(mGoodsItemTypes == null) return;
		mMenuExpandListAapter = new MenuExpandListAapter(getActivity(), mGoodsItemTypes);
		if(mMenuList == null) return;
		mMenuList.setAdapter(mMenuExpandListAapter);
	}
	
	/**
	 * 设置逛菜场首页数据
	 */
	private void setMarketHomeData(){
		if(mDataManager == null){
			return;
		}
		MarketHomePage marketHomePage = mDataManager.getMarketHomePage(null, 1, 6);
		if(marketHomePage == null) return;
		mTotalPageSize = marketHomePage.getPageCount();
		List<MarketHomeItem> marketHomeItems = marketHomePage.getFrontTypeList();
		mMarketHomeItems.addAll(marketHomeItems);
		mMarketItemListAdapter = new MarketItemListAdapter(getActivity(), mMarketHomeItems);
		if(mItemList == null) return;
		mItemList.setAdapter(mMarketItemListAdapter);
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
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//当滑动到最顶部后显示快捷菜单
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mFirstVisibleItem == 0){
			mQuickInView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
	}
}
