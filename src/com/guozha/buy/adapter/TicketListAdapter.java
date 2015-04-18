package com.guozha.buy.adapter;

import java.util.List;

import u.aly.cu;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mine.MarketTicket;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.util.ConstantUtil;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 我的菜票适配器
 * @author PeggyTong
 *
 */
public class TicketListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<MarketTicket> mMarketTicket;
	private long currentTime;
	
	public TicketListAdapter(Context context, List<MarketTicket> marketTicket){
		mInflater = LayoutInflater.from(context);
		mMarketTicket = marketTicket;
		currentTime = ConfigManager.getInstance().getTodayDate();
	}

	@Override
	public int getCount() {
		if(mMarketTicket == null) return 0;
		return mMarketTicket.size();
	}

	@Override
	public Object getItem(int position) {
		return mMarketTicket.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_ticket_item_cell, null);
			holder = new ViewHolder();
			holder.ticketType = (TextView) convertView.findViewById(R.id.ticket_type);
			holder.ticketForPrice = (TextView) convertView.findViewById(R.id.ticket_for_price);
			holder.ticketValidDate = (TextView) convertView.findViewById(R.id.ticket_valid_date);
			holder.ticketParValue = (TextView) convertView.findViewById(R.id.ticket_par_value);
			holder.ticketEffective = (ImageView) convertView.findViewById(R.id.ticket_effective);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		MarketTicket marketTicket = mMarketTicket.get(position);
		holder.ticketType.setText(ConstantUtil.getTicketType(marketTicket.getTicketType()));
		holder.ticketForPrice.setText("订单满" + UnitConvertUtil.getSwitchedMoney(marketTicket.getForPrice()) + "元使用");
		String validDate = DimenUtil.getStringFormatDate(marketTicket.getValidDate());
		if("9999年12月31日".equals(validDate)){
			holder.ticketValidDate.setText("菜票有效期 （长期有效）");
		}else{
			holder.ticketValidDate.setText("菜票有效期 " + validDate);
		}
		holder.ticketParValue.setText("￥" + UnitConvertUtil.getSwitchedMoney(marketTicket.getParValue()));
		if(currentTime < marketTicket.getValidDate().getTime()){
			holder.ticketEffective.setVisibility(View.GONE);
		}else{
			holder.ticketEffective.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder{
		private TextView ticketType;	   //菜票类型
		private TextView ticketForPrice;   //满多少可用
		private TextView ticketValidDate;  //使用期限
		private TextView ticketParValue;   //面值
		private ImageView ticketEffective; //是否过期
	}

}
