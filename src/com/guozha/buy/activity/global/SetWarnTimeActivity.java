package com.guozha.buy.activity.global;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.adapter.WarnTimeListAdapter;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
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
	private List<WarnTime> mWarnTimes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setwarntime);
		
		customActionBarStyle("管家提醒");
		
		initView();
		setWarnTimeData(null);
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
				//TODO 将选择的时间发送给服务器
				WarnTime warnTime = mWarnTimes.get(position);
				ConfigManager.getInstance().setWarnTime(warnTime.getValueTime());
				setWarnTimeData(warnTime.getShowTime());
				if(ConfigManager.getInstance().getWarnTimeOpend()){
					requestMenuPlan(warnTime.getValueTime());
				}
				
			}
		});
		Switch switchWarn = (Switch) findViewById(R.id.switch_warn);
		switchWarn.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void onCheckedChanged(Switch checkSwitch, boolean CheckState) {
				//TODO 将开关状态发送到服务器（开关推送功能）
				if(CheckState){
					String valueTime = ConfigManager.getInstance().getWarnTime();
					requestMenuPlan(valueTime);
				}else{
					//传null表示关闭
					requestMenuPlan(null);
				}
				ConfigManager.getInstance().setWarnTimeOpend(CheckState);
			}
		});
		boolean opendStatus = ConfigManager.getInstance().getWarnTimeOpend();
		switchWarn.setChecked(opendStatus);
	}
	
	/**
	 * 请求设置提醒时间
	 * @param warnTime
	 */
	private void requestMenuPlan(String valueTime) {
		String token = ConfigManager.getInstance().getUserToken(SetWarnTimeActivity.this);
		int userId = ConfigManager.getInstance().getUserId();
		RequestParam paramPath = new RequestParam("menuplan/alert")
		.setParams("token", token)
		.setParams("userId", userId)
		.setParams("planNotiTime", valueTime);
		HttpManager.getInstance(SetWarnTimeActivity.this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if(!"1".equals(returnCode)){
							ToastUtil.showToast(SetWarnTimeActivity.this, response.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
		});
	}
	
	public class WarnTime{
		private String showTime;	//显示的时间
		private String valueTime;	//发送的时间
		public WarnTime(String showTime, String valueTime){
			this.showTime = showTime;
			this.valueTime = valueTime;
		}
		public String getShowTime() {
			return showTime;
		}
		public String getValueTime() {
			return valueTime;
		}
	}
	
	/**
	 * 设置提醒时间
	 */
	private void setWarnTimeData(String showTime){
		mWarnTimes = new ArrayList<WarnTime>();
		mWarnTimes.add(new WarnTime("09:00", "0900"));
		mWarnTimes.add(new WarnTime("10:00", "1000"));
		mWarnTimes.add(new WarnTime("11:00", "1100"));
		mWarnTimes.add(new WarnTime("12:00", "1200"));
		mWarnTimes.add(new WarnTime("13:00", "1300"));
		mWarnTimes.add(new WarnTime("14:00", "1400"));
		mWarnTimes.add(new WarnTime("15:00", "1500"));
		mWarnTimes.add(new WarnTime("16:00", "1600"));
		String choosedWarnTime = ConfigManager.getInstance().getWarnTime();
		mWarnList.setAdapter(new WarnTimeListAdapter(this, choosedWarnTime, mWarnTimes));
		if(showTime == null){
			for(int i = 0; i < mWarnTimes.size(); i++){
				if(mWarnTimes.get(i).getValueTime().equals(choosedWarnTime)){
					mSettingWarnTime.setText(mWarnTimes.get(i).getShowTime());
				}
			}
		}else{
			mSettingWarnTime.setText(showTime);
		}
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
