package com.guozha.buy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.mine.OrderDetailActivity;
import com.guozha.buy.adapter.OrderListAdapter;

/**
 * 已完成订单列表
 * @author PeggyTong
 *
 */
public class OrderFinishedFragment extends Fragment{
	
	private ListView mOrderFinishList;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_order_finished, container, false);
		initView(view);
		return view;
	}
	
	/**
	 * 初始化View
	 * @param view
	 */
	private void initView(View view){
		mOrderFinishList = (ListView) view.findViewById(R.id.order_finished_list);
		mOrderFinishList.setAdapter(new OrderListAdapter(getActivity()));
		
		mOrderFinishList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(
						OrderFinishedFragment.this.getActivity(), OrderDetailActivity.class);
				startActivity(intent);
			}
			
		});
	}
	
}
