 package com.guozha.buy.controller.best.fragment;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.MarketItemListAdapter;
import com.guozha.buy.adapter.MenuExpandListAapter;
import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.controller.dialog.ChooseAddressDialog;
import com.guozha.buy.controller.market.ListVegetableActivity;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.MarketHomeItem;
import com.guozha.buy.entry.market.MarketHomePage;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.GoodsModel;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.GoodsModelResult;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.view.AnimatedExpandableListView;
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
	private static final int HAND_GOODS_DATA_COMPLETED = 0x0002; 
	private static final int HAND_GOODS_TYPE_COMPLETED = 0x0003;
	private static final int HAND_ADRESS_COMPLETED = 0x0004;
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
	
	private ListView mItemList;
	
	private Animation mInAnimation;
	private Animation mOutAnimation;
	
	private ImageView mMenuArrowIcon;
	
	/**
	 * 分页相关
	 */
	private int mTotalPageSize;     //总的页数
	private int mLastVisibleIndex;  //可见的最后一条数据
	private int mMaxDateNum;		//最大数据数
	private static int currentPage = 0;
	private static final int PAGE_SIZE = 4;
	
	/**
	 * 分页加载底部view相关
	 */
	private View mBottomLoadingView;  //底部刷新视图
	private TextView mLoadText;
	private ProgressBar mLoadProgressBar;
	
	private List<AddressInfo> mAddressInfos; //地址列表
	private TextView mActionBarAddress; 
	private View mActionBarView;
	private RefreshableView mRefreshableView;
	private GoodsModel mGoodsModel = new GoodsModel(new MyGoodsModelResult());
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_GOODS_DATA_COMPLETED:
				updateItemList();
				break;
			case HAND_GOODS_TYPE_COMPLETED:
				setGoodsItemTypeData();
				break;
			case HAND_ADRESS_COMPLETED:
				setAddressInfoData();
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
		initActionBar("逛菜场");
		initView(mView);
		initData();
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
		
		//界面分类列表
		mItemList = (ListView) view.findViewById(R.id.market_itemlist);
		mItemList.setItemsCanFocus(true);
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
		
		//箭头
		mMenuArrowIcon = (ImageView) view.findViewById(R.id.market_menu_item_arrow_icon);
		
		/**
		 * 刷新相关
		 */
		mRefreshableView = (RefreshableView) view.findViewById(R.id.market_refreshable_view);
		mRefreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mRefreshableView.finishRefreshing();
			}
		}, 0);
	}
	
	private void initData(){
		mTotalPageSize = 0;     //总的页数
		mLastVisibleIndex = 0;  //可见的最后一条数据
		mMaxDateNum = 0;		//最大数据数
		currentPage = 0;
		mMarketHomeItems.clear();
		
		int userId = ConfigManager.getInstance().getUserId();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		
		mUserModel.requestListAddress(getActivity(), userId);
		mGoodsModel.requestGoodsTypes(getActivity());
		mGoodsModel.requestGoodsList(getActivity(), addressId, 1, 4);
	}
	
	/**
	 * 设置地址列表数据
	 */
	private void setAddressInfoData(){
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
		if(mGoodsItemTypes == null) return;
		mMenuExpandListAapter = new MenuExpandListAapter(getActivity(), mGoodsItemTypes);
		if(mMenuList == null) return;
		mMenuList.setAdapter(mMenuExpandListAapter);
	}
	
	private void loadNewDataAndUpdate(){
		mGoodsModel.requestGoodsList(getActivity(), -1, currentPage + 1, PAGE_SIZE);
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
		case R.id.actionbar_address: //点击ActionBar地址按钮
			intent = new Intent(getActivity(), ChooseAddressDialog.class);
			getActivity().startActivityForResult(intent, REQUEST_CODE_ADDRESS);
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
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
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
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;  
		//所有的条目已经和最大数相等，则移除底部的view
		if(totalItemCount >= mMaxDateNum + 2){	//加了viewHead和viewFooter
			//mItemList.removeFooterView(mBottomLoadingView);
			mLoadProgressBar.setVisibility(View.GONE);
			mLoadText.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
	}
	
	class MyGoodsModelResult extends GoodsModelResult{
		@Override
		public void requestGoodsListResult(MarketHomePage marketHomePage) {
			if(marketHomePage == null) return;
			mTotalPageSize = marketHomePage.getPageCount();
			mMaxDateNum = marketHomePage.getTotalCount();
			currentPage++;
			List<MarketHomeItem> marketHomeItems = marketHomePage.getFrontTypeList();
			if(marketHomeItems == null) return;
			mMarketHomeItems.addAll(marketHomeItems);
			handler.sendEmptyMessage(HAND_GOODS_DATA_COMPLETED);
		}
		
		@Override
		public void requestGoodsTypesResult(List<GoodsItemType> goodsItemTypes) {
			mGoodsItemTypes = goodsItemTypes;
			handler.sendEmptyMessage(HAND_GOODS_TYPE_COMPLETED);
		}
	}
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestListAddressResult(List<AddressInfo> addressInfos) {
			mAddressInfos = addressInfos;
			handler.sendEmptyMessage(HAND_ADRESS_COMPLETED);
		}
	}
}
