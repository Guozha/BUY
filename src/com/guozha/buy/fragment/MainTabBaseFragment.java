package com.guozha.buy.fragment;

import com.guozha.buy.R;
import com.guozha.buy.debug.DebugActivity;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.widget.TextView;

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
	
	/**
	 * 初始化ActionBar
	 * @param actionbar
	 */
	protected void initActionBar(String barTitle) {
		ActionBar actionbar = getActivity().getActionBar();
		if(actionbar == null) return;
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.actionbar_base_view);
		TextView title = (TextView) actionbar.getCustomView().findViewById(R.id.title);
		title.setText(barTitle);
	}

}
