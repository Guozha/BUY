package com.guozha.buy.fragment;

import com.guozha.buy.R;
import com.guozha.buy.adapter.OrderListAdapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
	
	private void initView(View view){
		mOrderFinishList = (ListView) view.findViewById(R.id.order_finished_list);
		mOrderFinishList.setAdapter(new OrderListAdapter(getActivity()));
	}
	
}
