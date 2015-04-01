package com.guozha.buy.activity.cart;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.ChooseTicketListAdapter;

/**
 * 选择有效菜票
 * @author PeggyTong
 *
 */
public class ChooseTicketActivity extends BaseActivity{
	
	private ListView mChooseTicketList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_ticket);
		customActionBarStyle("选择菜票");
		
		initView();
	}
	
	private void initView(){
		mChooseTicketList = (ListView) findViewById(R.id.choose_ticket_list);
		mChooseTicketList.setAdapter(new ChooseTicketListAdapter(this, null));
		
		mChooseTicketList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				ChooseTicketActivity.this.finish();
			}
		});
	}
}
