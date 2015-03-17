package com.guozha.buy.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.SettingListAdapter;

/**
 * 设置界面
 * @author PeggyTong
 *
 */
public class SettingActivity extends BaseActivity{
	
	private ListView mSettingList;
	
	private List<String> mSettingItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		customActionBarStyle("设置");
		initData();
		initView();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		mSettingItems = new ArrayList<String>();
		mSettingItems.add("意见反馈");
		mSettingItems.add("常见问题");
		mSettingItems.add("在线客服");
		mSettingItems.add("系统更新");
		mSettingItems.add("关于我们");
	}
	
	private void initView(){
		mSettingList = (ListView) findViewById(R.id.setting_list);
		mSettingList.setAdapter(new SettingListAdapter(this, mSettingItems));
		
		mSettingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:     //意见反馈
					
					break;
				case 1:		//常见问题
					
					break;
				case 2:     //在线客服
					
					break;
				case 3:     //系统更新
					
					break;
				case 4:     //关于我们
					
					break;

				default:
					break;
				}
			}
		});
	}
	
}
