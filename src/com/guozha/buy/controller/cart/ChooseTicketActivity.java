package com.guozha.buy.controller.cart;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.ChooseTicketListAdapter;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.entry.mine.MarketTicket;
import com.guozha.buy.entry.mine.UsefulTicket;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.PayModel;
import com.guozha.buy.model.result.PayModelResult;
import com.guozha.buy.util.ToastUtil;

/**
 * 选择有效菜票
 * @author PeggyTong
 *
 */
public class ChooseTicketActivity extends BaseActivity{
	private static final int HAND_DATA_COMPLETED = 0x0001;
	private ListView mChooseTicketList;
	private List<MarketTicket> mMarketTickets;
	
	private int mMoney;
	
	private PayModel mPayModel = new PayModel(new MyPayModelResult());
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateView();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_ticket);
		customActionBarStyle("选择菜票");
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mMoney = bundle.getInt("money");
			}
		}
		setResult(0);
		initView();
		initData();
	}
	
	private void initView(){
		mChooseTicketList = (ListView) findViewById(R.id.choose_ticket_list);
		mChooseTicketList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = getIntent();
				if(intent != null && mMarketTickets != null){
					intent.putExtra("ticktId", mMarketTickets.get(position).getMyTicketId());
					intent.putExtra("ticketPrice", mMarketTickets.get(position).getParValue());
					setResult(0, intent);
				}
				ChooseTicketActivity.this.finish();
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(this);
		if(token == null) return;
		mPayModel.requestValidTicket(this, token, userId, mMoney);
	}
	
	
	private void updateView(){
		mChooseTicketList.setAdapter(new ChooseTicketListAdapter(this, mMarketTickets));
	}
	
	class MyPayModelResult extends PayModelResult{
		@Override
		public void requestValidTicketResult(UsefulTicket userfulTicket) {
			if(userfulTicket == null) {
				ToastUtil.showToast(ChooseTicketActivity.this, "获取数据失败");
				return;
			}
			if(BaseModel.REQUEST_SUCCESS.equals(userfulTicket.getReturnCode())){
				mMarketTickets = userfulTicket.getTickets();
				handler.sendEmptyMessage(HAND_DATA_COMPLETED);
			}else{
				ToastUtil.showToast(ChooseTicketActivity.this, userfulTicket.getMsg());
			}
		}
	}
}
