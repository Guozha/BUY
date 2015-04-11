package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mine.MarketTicket;

/**
 * 选择有效菜票列表适配器
 * @author PeggyTong
 *
 */
public class ChooseTicketListAdapter extends BaseAdapter{
	
	private List<MarketTicket> mMarketTickets;
	private LayoutInflater mInflater;
	public ChooseTicketListAdapter(Context context, List<MarketTicket> marketTickets){
		mInflater = LayoutInflater.from(context);
		mMarketTickets = marketTickets;
	}

	@Override
	public int getCount() {
		if(mMarketTickets == null) return 0;
		return mMarketTickets.size();
	}

	@Override
	public Object getItem(int position) {
		return mMarketTickets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_ticket_item_cell, null);
		}
		TextView ticketType = (TextView) convertView.findViewById(R.id.ticket_type);
		TextView ticketPrice = (TextView) convertView.findViewById(R.id.ticket_for_price);
		TextView ticketValidDate = (TextView) convertView.findViewById(R.id.ticket_valid_date);
		
		MarketTicket marketTicket = mMarketTickets.get(position);
		ticketType.setText(marketTicket.getTicketType());
		ticketPrice.setText(marketTicket.getForPrice());
		ticketValidDate.setText(marketTicket.getValidDate());
		
		return convertView;
	}

}
