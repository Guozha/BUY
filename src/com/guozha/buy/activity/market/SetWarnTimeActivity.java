package com.guozha.buy.activity.market;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.WarnTimeListAdapter;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.view.Switch;
import com.guozha.buy.view.Switch.OnChangedListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 设置提醒
 * @author PeggyTong
 *
 */
public class SetWarnTimeActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SetWarnTimePage";
	
	private ListView mWarnList;
	private TextView mSettingWarnTime;
	private List<String> mWarnTimes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setwarntime);
		
		customActionBarStyle("提醒");
		
		initView();
		setWarnTimeData();
	}
	
	/**
	 * 初始化View
	 */
	private void initView(){
		mWarnList = (ListView) findViewById(R.id.set_warn_list);
		mSettingWarnTime = (TextView) findViewById(R.id.setting_warn_time);
		mWarnList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//TODO 发送给服务器
				String warnTime = mWarnTimes.get(position);
				ConfigManager.getInstance().setWarnTime(warnTime);
				setWarnTimeData();
			}
		});
		Switch switchWarn = (Switch) findViewById(R.id.switch_warn);
		switchWarn.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void onCheckedChanged(Switch checkSwitch, boolean CheckState) {
				//TODO 
				ToastUtil.showToast(SetWarnTimeActivity.this, "CheckeState = " + CheckState);
			}
		});
	}
	
	/**
	 * 设置提醒时间
	 */
	private void setWarnTimeData(){
		mWarnTimes = new ArrayList<String>();
		mWarnTimes.add("09:00");
		mWarnTimes.add("10:00");
		mWarnTimes.add("11:00");
		mWarnTimes.add("12:00");
		mWarnTimes.add("13:00");
		mWarnTimes.add("14:00");
		mWarnTimes.add("15:00");
		mWarnTimes.add("16:00");
		mWarnTimes.add("17:00");
		String choosedWarnTime = ConfigManager.getInstance().getWarnTime();
		mWarnList.setAdapter(new WarnTimeListAdapter(this, choosedWarnTime, mWarnTimes));
		mSettingWarnTime.setText(choosedWarnTime);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//友盟界面统计
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
	
}
