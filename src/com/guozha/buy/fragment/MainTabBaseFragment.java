package com.guozha.buy.fragment;

import com.guozha.buy.debug.DebugActivity;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.support.v4.app.Fragment;

/**
 * MainActivity的Fragment抽象类
 * @author PeggyTong
 *
 */
public abstract class MainTabBaseFragment extends BaseFragment{
	
	protected MainPageInitDataManager mDataManager = null;
	
	/**
	 * 加载数据完成(注意：该方法是在主线程中调用的）
	 */
	public abstract void loadDataCompleted(MainPageInitDataManager dataManager, int handerType);
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			//测试服提示
			if(HttpManager.URL.equals(DebugActivity.TEST_URL)){
				ToastUtil.showToast(this.getActivity(), "当前是测试服");
			}
		}else{
			//View不可见
			
		}
	}
}
