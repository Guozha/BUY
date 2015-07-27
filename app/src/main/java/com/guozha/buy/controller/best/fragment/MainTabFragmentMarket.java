package com.guozha.buy.controller.best.fragment;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.MarketItemListAdapter;
import com.guozha.buy.adapter.MenuExpandListAapter;
import com.guozha.buy.controller.dialog.ChooseAddressDialog;
import com.guozha.buy.controller.market.ListVegetableActivity;
import com.guozha.buy.entry.market.GoodsItemType;
import com.guozha.buy.entry.market.GoodsSecondItemType;
import com.guozha.buy.entry.market.MarketHomeItem;
import com.guozha.buy.entry.market.MarketHomePage;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.GoodsModel;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.GoodsModelResult;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.view.AnimatedExpandableListView;
import com.guozha.buy.view.RefreshableView;
import com.guozha.buy.view.RefreshableView.PullToRefreshListener;

/**
 * 逛菜场
 * @author PeggyTong
 *
 */
public class MainTabFragmentMarket extends MainTabBaseFragment implements OnClickListener,OnScrollListener{
	
	private static final String PAGE_NAME = "逛菜场";
	private static final int HAND_GOODS_DATA_COMPLETED = 0x0002; 
	private static final int HAND_GOODS_TYPE_COMPLETED = 0x0003;
	private static final int HAND_ADRESS_COMPLETED = 0x0004;
	private static final int HAND_REFRESH_DATA = 0x0005;
	public static final int REQUEST_CODE_ADDRESS = 10;
	public static final int REQUEST_CODE_CART = 11;
	
	private View mView;
	
	private BitmapCache mBitmapCache = BitmapCache.getInstance();
	
	private View mTopExpandMenuButton;
	private AnimatedExpandableListView mMenuList;
	private MenuExpandListAapter mMenuExpandListAapter;
	private MarketItemListAdapter mMarketItemListAdapter;
	
	private List<GoodsItemType> mGoodsItemTypes = new ArrayList<GoodsItemType>();  //菜品类目菜单数据
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
	private static int mCurrentPage = 0;
	
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
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_GOODS_DATA_COMPLETED:
				mMarketItemListAdapter.notifyDataSetChanged();
				break;
			case HAND_GOODS_TYPE_COMPLETED:
				mMenuExpandListAapter.notifyDataSetChanged();
				break;
			case HAND_ADRESS_COMPLETED:
				setAddressInfoData();
				break;
			case HAND_REFRESH_DATA:
				initData();
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
		//初始化ActionBar
		initActionBar(getActivity().getActionBar());
		return mView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		initData();
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
		actionbar.setCustomView(mActionBarView);
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
		mActionBarAddress.setOnClickListener(this);
		
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
		mLoadText =  (TextView) mBottomLoadingView.findViewById(R.id.list_paging_bottom_text);
		mLoadProgressBar = (ProgressBar) mBottomLoadingView.findViewById(R.id.list_paging_bottom_progressbar);
		mItemList.addFooterView(mBottomLoadingView);
		mItemList.setOnScrollListener(this);
		
		//箭头
		mMenuArrowIcon = (ImageView) view.findViewById(R.id.market_menu_item_arrow_icon);
		
		/**
		 * 刷新相关
		 */
		mRefreshableView = (RefreshableView) view.findViewById(R.id.market_refreshable_view);
		mRefreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				mHandler.sendEmptyMessage(HAND_REFRESH_DATA);
				mRefreshableView.finishRefreshing();
			}
		}, 0);
		
		mMenuExpandListAapter = new MenuExpandListAapter(getActivity(), mGoodsItemTypes);
		mMenuList.setAdapter(mMenuExpandListAapter);
		
		mMarketItemListAdapter = new MarketItemListAdapter(this.getActivity(), mMarketHomeItems, mBitmapCache);
		mItemList.setAdapter(mMarketItemListAdapter);
	}
	
	@Override
	public boolean onKeyDownBack() {
		if("expand".equals(mTopExpandMenuButton.getTag())){
			expandMenuAction(mTopExpandMenuButton);
			return true;
		}
		return false;
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		mTotalPageSize = 0;     //总的页数
		mLastVisibleIndex = 0;  //可见的最后一条数据
		mMaxDateNum = 0;		//最大数据数
		mCurrentPage = 0;
		mMarketHomeItems.clear();
		isLocked = false;
		int userId = ConfigManager.getInstance().getUserId();
		mUserModel.requestListAddress(getActivity(), userId);
		mGoodsModel.requestGoodsTypes(getActivity());
		requestGoodsList();
	}
	
	private boolean isLocked = false;
	private void requestGoodsList(){
		if(!isLocked){
			int addressId = ConfigManager.getInstance().getChoosedAddressId();
			mGoodsModel.requestGoodsList(getActivity(), addressId, mCurrentPage + 1);
			isLocked = true;
		}
	}
	
	/**
	 * 设置地址列表数据
	 */
	private void setAddressInfoData(){
		String addressName = "";
		if(mActionBarAddress != null){
			if(mAddressInfos != null && !mAddressInfos.isEmpty()){
				int choosedId = ConfigManager.getInstance().getChoosedAddressId();
				boolean isMatched = false;
				if(choosedId != -1){
					for(int i = 0; i < mAddressInfos.size(); i++){
						AddressInfo addressInfo = mAddressInfos.get(i);
						if(addressInfo.getAddressId() == choosedId){
							isMatched = true;
							addressName = addressInfo.getBuildingName();
						}
					}
				}
				if(!isMatched){
					AddressInfo addressInfo = mAddressInfos.get(0);
					ConfigManager.getInstance().setChoosedAddressId(addressInfo.getAddressId());
					addressName = addressInfo.getBuildingName();
				}
				mActionBarAddress.setVisibility(View.VISIBLE);
				mActionBarAddress.setText(addressName);
			}else{
				mActionBarAddress.setVisibility(View.GONE);
			}
		}
	}
	
	private void loadNewDataAndUpdate(){
		requestGoodsList();
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
			this.startActivityForResult(intent, REQUEST_CODE_ADDRESS);
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE_ADDRESS){
			initData();
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
			mMenuArrowIcon.setImageResource(R.drawable.arrow_up);
			view.setTag("expand");
			mMenuList.startAnimation(mInAnimation);
		}else{
			mMenuList.setVisibility(View.GONE);
			view.setTag("unexpand");
			mMenuArrowIcon.setImageResource(R.drawable.arrow_down);
			mMenuList.startAnimation(mOutAnimation);
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex >= mMarketItemListAdapter.getCount()
				&& mCurrentPage < mTotalPageSize){
			mLoadText.setVisibility(View.GONE);
			mLoadProgressBar.setVisibility(View.VISIBLE);
			loadNewDataAndUpdate();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mLastVisibleIndex = firstVisibleItem + visibleItemCount;  
		//所有的条目已经和最大数相等，则移除底部的view
		if(totalItemCount >= mMaxDateNum + 1){	//加了viewHead和viewFooter
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
	
	/**
	 * 转换格式
	 */
	private void formatGoodsItemTypes(){
		for(int i = 0; i < mGoodsItemTypes.size(); i++){
			GoodsItemType itemType = mGoodsItemTypes.get(i);
			GoodsSecondItemType secondItemType = new GoodsSecondItemType(
					itemType.getFrontTypeId(), itemType.getShortName(), "查看全部...");
			List<GoodsSecondItemType> secondItemList = itemType.getFrontTypeList();
			secondItemList.add(0, secondItemType);
		}
	}
	
	class MyGoodsModelResult extends GoodsModelResult{
		@Override
		public void requestGoodsListResult(MarketHomePage marketHomePage) {
			isLocked = false;
			if(marketHomePage == null) return;
			mTotalPageSize = marketHomePage.getPageCount();
			mMaxDateNum = marketHomePage.getTotalCount();
			mCurrentPage++;
			List<MarketHomeItem> marketHomeItems = marketHomePage.getFrontTypeList();
			if(marketHomeItems == null) return;
			mMarketHomeItems.addAll(marketHomeItems);
			mHandler.sendEmptyMessage(HAND_GOODS_DATA_COMPLETED);
		}
		
		@Override
		public void requestGoodsTypesResult(List<GoodsItemType> goodsItemTypes) {
			if(goodsItemTypes == null) return;
			mGoodsItemTypes.clear();
			mGoodsItemTypes.addAll(goodsItemTypes);
			formatGoodsItemTypes();
			mHandler.sendEmptyMessage(HAND_GOODS_TYPE_COMPLETED);
		}
	}
	
	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestListAddressResult(List<AddressInfo> addressInfos) {
			mAddressInfos = addressInfos;
			mHandler.sendEmptyMessage(HAND_ADRESS_COMPLETED);
		}
	}

	@Override
	protected String getPageName() {
		return PAGE_NAME;
	}
}
