 package com.guozha.buy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.ChooseMenuActivity;
import com.guozha.buy.activity.market.ListVegetableActivity;
import com.guozha.buy.adapter.MarketItemListAdapter;
import com.guozha.buy.adapter.MenuExpandListAapter;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.MarketHomeItem;
import com.guozha.buy.entry.market.MarketHomePage;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
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
	private static final int HAND_DATA_COMPLETED = 0x0002; 
	
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
	
	/**
	 * 分页加载底部view相关
	 */
	private View mBottomLoadingView;  //底部刷新视图
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateItemList();
				break;
			}
		};
	};

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
		mBottomLoadingView = getActivity().getLayoutInflater().inflate(R.layout.list_paging_bottom, null);
		mLoadText = (TextView) mBottomLoadingView.findViewById(R.id.list_paging_bottom_text);
		mLoadProgressBar = (ProgressBar) mBottomLoadingView.findViewById(R.id.list_paging_bottom_progressbar);
		mItemList.addFooterView(mBottomLoadingView);
		mItemList.setOnScrollListener(this);
		mMarketItemListAdapter = new MarketItemListAdapter(getActivity(), mMarketHomeItems);
		mItemList.setAdapter(mMarketItemListAdapter);
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
	 * 设置逛菜场首页数据（第一页数据）
	 */
	private void setMarketHomeData(){
		if(mDataManager == null){
			return;
		}
		if(currentPage == 1) return;
		MarketHomePage marketHomePage = mDataManager.getMarketHomePage(null, 1, 4);
		if(marketHomePage == null) return;
		mTotalPageSize = marketHomePage.getPageCount();
		currentPage = 1;
		mMaxDateNum = marketHomePage.getTotalCount();
		List<MarketHomeItem> marketHomeItems = marketHomePage.getFrontTypeList();
		mMarketHomeItems.addAll(marketHomeItems);
		handler.sendEmptyMessage(HAND_DATA_COMPLETED);
	}
	
	private int currentPage = 0;
	private static final int PAGE_SIZE = 4;
	private void loadNewDataAndUpdate(){
		String addressId = "";
		RequestParam paramPath = new RequestParam("goods/general/list")
		.setParams("addressId", addressId)
		.setParams("pageNum", currentPage + 1)
		.setParams("pageSize", PAGE_SIZE);
		HttpManager.getInstance(getActivity()).volleyRequestByPost(HttpManager.URL + paramPath, 
			new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					MarketHomePage marketHomePage = gson.fromJson(response, new TypeToken<MarketHomePage>() { }.getType());
					if(marketHomePage == null) return;
					mTotalPageSize = marketHomePage.getPageCount();
					mMaxDateNum = marketHomePage.getTotalCount();
					currentPage++;
					List<MarketHomeItem> marketHomeItems = marketHomePage.getFrontTypeList();
					if(marketHomeItems == null) return;
					mMarketHomeItems.addAll(marketHomeItems);
					handler.sendEmptyMessage(HAND_DATA_COMPLETED);
				}
			});
	}
	
	/**
	 * 更新当前列表
	 */
	private void updateItemList(){
		if(mItemList == null) return;
		mMarketItemListAdapter.notifyDataSetChanged();
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

	
	private int mLastVisibleIndex;  //可见的最后一条数据
	private int mMaxDateNum;		//最大数据数
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//当滑动到最顶部后显示快捷菜单
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mFirstVisibleItem == 0){
			mQuickInView.setVisibility(View.VISIBLE);
		}
		
		LogUtil.e("currentPage = " + currentPage);
		LogUtil.e("totalPage = " + mTotalPageSize);
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex == mMarketItemListAdapter.getCount()
				&& currentPage < mTotalPageSize){
			loadNewDataAndUpdate();
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
		
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		//所有的条目已经和最大数相等，则移除底部的view
		LogUtil.e("totalItemCount = " + totalItemCount);
		LogUtil.e("mMaxDataNum = " + mMaxDateNum);
		if(totalItemCount >= mMaxDateNum + 2){
			mItemList.removeFooterView(mBottomLoadingView);
			ToastUtil.showToast(getActivity(), "数据全部加载完毕，没有更多数据");
		}
	}
}
