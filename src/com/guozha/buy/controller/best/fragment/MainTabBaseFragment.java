package com.guozha.buy.controller.best.fragment;

import android.app.ActionBar;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.BaseFragment;
import com.guozha.buy.controller.DebugActivity;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.ToastUtil;

/**
 * MainActivity的Fragment抽象类
 * @author PeggyTong
 *
 */
public abstract class MainTabBaseFragment extends BaseFragment{
	
	protected MainPageInitDataManager mDataManager = null;
	
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
