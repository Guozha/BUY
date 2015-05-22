 package com.guozha.buy.ui.best.fragment;

import java.util.ArrayList;
import java.util.List;

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
import com.guozha.buy.adapter.MarketItemListAdapter;
import com.guozha.buy.adapter.MenuExpandListAapter;
import com.guozha.buy.entry.global.QuickMenu;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.MarketHomeItem;
import com.guozha.buy.entry.market.MarketHomePage;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.ui.CustomApplication;
import com.guozha.buy.ui.dialog.ChooseAddressDialog;
import com.guozha.buy.ui.market.ChooseMenuActivity;
import com.guozha.buy.ui.market.ListVegetableActivity;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.view.AnimatedExpandableListView;
import com.guozha.buy.view.CustomListView;
import com.guozha.buy.view.RefreshableView;
import com.guozha.buy.view.RefreshableView.PullToRefreshListener;
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
	private static final int HAND_BEGIN_REFRESH = 0x00010;	//开始刷新
	private static final int HAND_END_REFRESH = 0x0004;		//刷新完成
	public static final int REQUEST_CODE_ADDRESS = 10;
	public static final int REQUEST_CODE_CART = 11;
	
	private View mView;
	
	private BitmapCache mBitmapCache = CustomApplication.getBitmapCache();
	
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
	
	private List<TextView> mQuickMenus; //快捷菜单列表
	private List<AddressInfo> mAddressInfos; //地址列表
	private TextView mActionBarAddress; 
	private View mActionBarView;
	
	private View mItemHeaderView;
	private RefreshableView mRefreshableView;
	private int mCurrentAddressId = 
			ConfigManager.getInstance().getChoosedAddressId();   //当前地址id(用来判断地址是否发生了改变）
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateItemList();
				break;
			case HAND_BEGIN_REFRESH:
				if(mQuickInView == null) return;
				mQuickInView.setVisibility(View.GONE);
				mItemList.removeHeaderView(mItemHeaderView);
				break;
			case HAND_END_REFRESH:
				if(mQuickInView == null) return;
				mQuickInView.setVisibility(View.VISIBLE);
				mItemList.addHeaderView(mItemHeaderView);
				break;
			case MainPageInitDataManager.HAND_INITDATA_MSG_MARKETHOME:
				setMarketHomeData();
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		
		LogUtil.e("第一次进来的地址id = " + mCurrentAddressId);
		mView = inflater.inflate(R.layout.fragment_maintab_market, container, false);
		
		//菜单出入动画
		mInAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.market_menu_in_anim);
		mOutAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.market_menu_out_anim);
		
		initActionBar("逛菜场");
		initView(mView);
		initQuickMenusData();
		
		return mView;
	}
	
	/**
	 * 初始化View
	 * @param view
	 */
	private void initView(View view){
		//地址相关
		mActionBarView = LayoutInflater.from(getActivity())
				.inflate(R.layout.actionbar_market_custom_view, null);
		mActionBarAddress = (TextView) mActionBarView.findViewById(R.id.actionbar_address);
		mActionBarAddress.setVisibility(View.GONE);
		mActionBarAddress.setOnClickListener(this);
		setAddressInfoData();
		
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
			public boolean onChildClick(ExpandableListView parent, View view,
					int groupPosition, int childPosition, long id) {
				//TODO 暂时这样写着，为了测试接口
				View tagView = view.findViewById(R.id.menu_list_child_cell_text);
				String tag = String.valueOf(tagView.getTag());
				String[] itemType = tag.split(":");
				Intent intent = new Intent(getActivity(), ListVegetableActivity.class);
				//将商品类别传给列表
				if(itemType.length == 2){
					intent.putExtra("frontTypeId", itemType[0]);
					intent.putExtra("frontTypeName", itemType[1]);
				}
				startActivity(intent);
				return false;
			}
		});
		
		
		setGoodsItemTypeData();
		
		mItemHeaderView = LayoutInflater.from(this.getActivity())
				.inflate(R.layout.market_list_item_header, null);
		//界面分类列表
		mItemList = (CustomListView) view.findViewById(R.id.market_itemlist);
		mItemList.setItemsCanFocus(true);
		mItemList.addHeaderView(mItemHeaderView);
		mBottomLoadingView = getActivity().getLayoutInflater().inflate(R.layout.list_paging_bottom, null);
		mLoadText = (TextView) mBottomLoadingView.findViewById(R.id.list_paging_bottom_text);
		mLoadProgressBar = (ProgressBar) mBottomLoadingView.findViewById(R.id.list_paging_bottom_progressbar);
		mItemList.addFooterView(mBottomLoadingView);
		mItemList.setOnScrollListener(this);
		if(mMarketItemListAdapter == null){
			mMarketItemListAdapter = new MarketItemListAdapter(this.getActivity(), mMarketHomeItems, mBitmapCache);
		}else{
			mMarketItemListAdapter.notifyDataSetChanged();
		}
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
		
		
		//快捷菜单设置
		mQuickMenus = new ArrayList<TextView>();
		mQuickMenus.add((TextView)view.findViewById(R.id.market_quick_menu_1));
		mQuickMenus.add((TextView)view.findViewById(R.id.market_quick_menu_2));
		mQuickMenus.add((TextView)view.findViewById(R.id.market_quick_menu_3));
		mQuickMenus.add((TextView)view.findViewById(R.id.market_quick_menu_4));
		mQuickMenus.add((TextView)view.findViewById(R.id.market_quick_menu_5));
		
		for(int i = 0; i < mQuickMenus.size(); i++){
			mQuickMenus.get(i).setOnClickListener(this);
		}
		
		/**
		 * 刷新相关
		 */
		mRefreshableView = (RefreshableView) view.findViewById(R.id.market_refreshable_view);
		mRefreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				handler.sendEmptyMessage(HAND_BEGIN_REFRESH);
				if(mDataManager == null){
					mDataManager = MainPageInitDataManager.getInstance();
				}
				//让全部更新
				MainPageInitDataManager.mMarketItemUpdated = true;
				mDataManager.getMarketHomePage(handler, 1, 4);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mRefreshableView.finishRefreshing();
				handler.sendEmptyMessage(HAND_END_REFRESH);
			}
		}, 0);
	}
	
	/**
	 * 初始化快捷菜单数据
	 */
	private void initQuickMenusData(){
		if(mQuickMenus == null) return;
		List<QuickMenu> quickMenus = ConfigManager.getInstance().getQuickMenus();
		
		if(quickMenus == null) {
			for(int i = 0; i < 5; i++){
				mQuickMenus.get(i).setBackgroundResource(R.drawable.main_tag_edit);
				mQuickMenus.get(i).setTag("-1");
				mQuickMenus.get(i).setText("");
			}
			return;
		}
		for(int i = 0; i < quickMenus.size(); i++){
			mQuickMenus.get(i).setText(quickMenus.get(i).getName());
			mQuickMenus.get(i).setTag(quickMenus.get(i).getMenuId() + ":" + quickMenus.get(i).getName());
			mQuickMenus.get(i).setBackgroundResource(R.drawable.market_tag_background);
		}
		
		for(int i = 0; i < 5 - quickMenus.size(); i++){
			mQuickMenus.get(i).setBackgroundResource(R.drawable.main_tag_edit_round);
			mQuickMenus.get(i).setTag("-1");
			mQuickMenus.get(i).setText("");
		}
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
		case MainPageInitDataManager.HAND_INTTDATA_MSG_ADDRESS_LIST: //地址列表
			setAddressInfoData();
			break;
		}
	}
	
	/**
	 * 设置地址列表数据
	 */
	private void setAddressInfoData(){
		if(mDataManager == null){
			mDataManager = MainPageInitDataManager.getInstance();
		}
		mAddressInfos = mDataManager.getAddressInfos(null);
		//TODO 设置ActionBar上面的显示
		int choosedId = ConfigManager.getInstance().getChoosedAddressId();
		String addressName = "";
		if(mActionBarAddress != null){
			if(mAddressInfos != null){
				for(int i = 0; i < mAddressInfos.size(); i++){
					AddressInfo addressInfo = mAddressInfos.get(i);
					if(addressInfo.getAddressId() == choosedId){
						addressName = addressInfo.getBuildingName();
					}
				}
				mActionBarAddress.setVisibility(View.VISIBLE);
				mActionBarAddress.setText(addressName);
			}else{
				mActionBarAddress.setVisibility(View.GONE);
			}
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
		MarketHomePage marketHomePage = mDataManager.getMarketHomePage(null, 1, 4);
		if(marketHomePage == null) return;
		mTotalPageSize = marketHomePage.getPageCount();
		currentPage = 1;
		mMarketHomeItems.clear();
		LogUtil.e("clear");
		mMaxDateNum = marketHomePage.getTotalCount();
		List<MarketHomeItem> marketHomeItems = marketHomePage.getFrontTypeList();
		mMarketHomeItems.addAll(marketHomeItems);
		handler.sendEmptyMessage(HAND_DATA_COMPLETED);
	}
	
	private static int currentPage = 0;
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
		Intent intent;
		switch (view.getId()) {
		case R.id.market_expand_menu_button:
			expandMenuAction(view);
			break;
		case R.id.choose_menu_custom:
			intent = new Intent(getActivity(), ChooseMenuActivity.class);
			startActivity(intent);
			break;
		case R.id.market_quick_menu_1:
		case R.id.market_quick_menu_2:
		case R.id.market_quick_menu_3:
		case R.id.market_quick_menu_4:
		case R.id.market_quick_menu_5:
			clickQuickMneuEvent(view);
			break;
		case R.id.actionbar_address: //点击ActionBar地址按钮
			intent = new Intent(getActivity(), ChooseAddressDialog.class);
			getActivity().startActivityForResult(intent, REQUEST_CODE_ADDRESS);
			break;
		}
	}
	
	/**
	 * 点击快捷菜单
	 * @param view
	 */
	private void clickQuickMneuEvent(View view) {
		String tag = String.valueOf(view.getTag());
		Intent intent;
		if("-1".equals(tag)){
			intent = new Intent(MainTabFragmentMarket.this.getActivity(), ChooseMenuActivity.class);
			startActivity(intent);
		}else{
			if(view == null) return;
			String[] itemType = tag.split(":");
			intent = new Intent(getActivity(), ListVegetableActivity.class);
			//将商品类别传给列表
			if(itemType.length == 2){
				intent.putExtra("frontTypeId", itemType[0]);
				intent.putExtra("frontTypeName", itemType[1]);
			}
			startActivity(intent);
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
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		//如果地址发生变化则改变数据
		if(mCurrentAddressId != addressId){
			MainPageInitDataManager.mMarketItemUpdated = true;
			MainPageInitDataManager.mCartItemsUpdated = true;
			mDataManager.getMarketHomePage(handler, 1, 4);
			mCurrentAddressId = addressId;
		}
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
		
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex == mMarketItemListAdapter.getCount() + 1 //加了viewHead
				&& currentPage < mTotalPageSize){
			mLoadText.setVisibility(View.GONE);
			mLoadProgressBar.setVisibility(View.VISIBLE);
			loadNewDataAndUpdate();
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
		
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;  
		//所有的条目已经和最大数相等，则移除底部的view
		if(totalItemCount >= mMaxDateNum + 2){	//加了viewHead和viewFooter
			//mItemList.removeFooterView(mBottomLoadingView);
			mLoadProgressBar.setVisibility(View.GONE);
			mLoadText.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//更新快捷菜单
		initQuickMenusData();
		if(mMarketHomeItems == null || mMarketHomeItems.size() == 0){
			setMarketHomeData();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
	}
}
