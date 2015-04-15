package com.guozha.buy.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mine.order.OrderSummary;
import com.guozha.buy.util.ConstantUtil;
import com.guozha.buy.util.LogUtil;

/**
 * 订单列表适配器
 * @author PeggyTong
 *
 */
public class OrderListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<OrderSummary> mOrderSummary;
	private SimpleDateFormat mDateFormat;
	
	public OrderListAdapter(Context context, List<OrderSummary> orderSummary){
		mInflater = LayoutInflater.from(context);
		mOrderSummary = orderSummary;
		mDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	}

	@Override
	public int getCount() {
		if(mOrderSummary == null) return 0;
		return mOrderSummary.size();
	}

	@Override
	public Object getItem(int position) {
		return mOrderSummary.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_order_item_cell, null);
			holder = new ViewHolder();
			holder.orderTime = (TextView) convertView.findViewById(R.id.order_time);
			holder.orderCount = (TextView) convertView.findViewById(R.id.order_product_count);
			holder.orderStatus = (TextView) convertView.findViewById(R.id.order_status);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		OrderSummary orderSummary = mOrderSummary.get(position);
		Date date = orderSummary.getCreateTime();
		holder.orderTime.setText(mDateFormat.format(date));
		holder.orderCount.setText("共" + orderSummary.getQuantity() + "件商品，");
		holder.orderStatus.setText(ConstantUtil.getOrderStatusString(
				orderSummary.getStatus(),
				orderSummary.getArrivalPayFlag(), 
				orderSummary.getCommentFlag()));
		return convertView;
	}
	
	static class ViewHolder{
		private TextView orderTime;
		private TextView orderCount;
		private TextView orderStatus;
	}

}
