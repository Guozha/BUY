package com.guozha.buy.model.result;

import java.util.List;

import com.guozha.buy.entry.cart.PayValidateResult;
import com.guozha.buy.entry.cart.PayWayEntry;
import com.guozha.buy.entry.mine.UsefulTicket;
import com.guozha.buy.model.PayModel.PayModelInterface;

public class PayModelResult implements PayModelInterface{

	@Override
	public void requestPayWaysResult(List<PayWayEntry> payWayList) {
		
	}

	@Override
	public void requestPreparePayResult(PayValidateResult payValidateResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestValidTicketResult(UsefulTicket userfulTicket) {
		// TODO Auto-generated method stub
		
	}

}
