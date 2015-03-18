package com.guozha.buy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.guozha.buy.R;
import com.guozha.buy.activity.mine.OrderDetailActivity;
import com.guozha.buy.adapter.OrderListAdapter;

/**
 * 未完成订单列表
 * @author PeggyTong
 *
 */
public class OrderUnFinishFragment extends Fragment{
	
	private ListView mOrderUnFinishList;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_order_unfinished, container, false);
		initView(view);
		return view;
	}
	
	private void initView(View view){
		mOrderUnFinishList = (ListView) view.findViewById(R.id.order_unfinished_list);
		mOrderUnFinishList.setAdapter(new OrderListAdapter(getActivity()));
		
		mOrderUnFinishList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(
						OrderUnFinishFragment.this.getActivity(), OrderDetailActivity.class);
				startActivity(intent);
			}
			
		});
	}
}
