package com.guozha.buy.model.result;

import com.guozha.buy.entry.cart.TimeList;
import com.guozha.buy.entry.mine.order.OrderDetail;
import com.guozha.buy.entry.mine.order.OrderSummaryPage;
import com.guozha.buy.model.OrderModel.OrderModelInterface;

public class OrderModelResult implements OrderModelInterface{

	@Override
	public void requestOrderTimesResult(TimeList timeList) {
		
	}

	@Override
	public void requestSubmitOrderResult(String returnCode, String msg,
			int orderId) {
		
	}

	@Override
	public void requestListOrderResult(OrderSummaryPage orderSummaryPage) {
		
	}

	@Override
	public void requestOrderInforResult(int totalPrice, int serviceFee) {
		
	}

	@Override
	public void requestOrderDetailResult(OrderDetail orderDetail) {
		
	}

	@Override
	public void requestCancelOrderResult(String returnCode, String msg) {
		
	}

	@Override
	public void requestGradeProductResult(String returnCode, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestOrderMarkResult(String returnCode, String msg) {
		// TODO Auto-generated method stub
		
	}

}
