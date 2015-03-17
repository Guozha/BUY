package com.guozha.buy.fragment;

import com.guozha.buy.global.MainPageInitDataManager;

import android.support.v4.app.Fragment;

/**
 * MainActivity的Fragment抽象类
 * @author PeggyTong
 *
 */
public abstract class MainTabBaseFragment extends Fragment{
	
	protected MainPageInitDataManager mDataManager = null;
	
	/**
	 * 加载数据完成
	 */
	public abstract void loadDataCompleted(MainPageInitDataManager dataManager, int handerType);
}
