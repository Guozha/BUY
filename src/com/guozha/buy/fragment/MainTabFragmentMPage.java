package com.guozha.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.newfold.MPageListAdapter;
import com.guozha.buy.global.MainPageInitDataManager;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMPage extends MainTabBaseFragment implements OnClickListener{
	
	private static final String PAGE_NAME = "MainPage";
	private ListView mListView;
	private MPageListAdapter mMPageListAdpater;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_mpage, container, false);
		initActionBar("爱掌勺");
		initView(view);
		return view;
	}
	
	/**
	 * 初始化视图
	 * @param view
	 */
	private void initView(View view){
		if(view == null) return;
		mListView = (ListView) view.findViewById(R.id.mpage_list);
		mMPageListAdpater = new MPageListAdapter(getActivity());
		mListView.setAdapter(mMPageListAdpater);
	}
	
	@Override
	public void loadDataCompleted(MainPageInitDataManager dataManager, int handlerType) {
		
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
			//测试服提示
		}else{
			//View不可见
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
