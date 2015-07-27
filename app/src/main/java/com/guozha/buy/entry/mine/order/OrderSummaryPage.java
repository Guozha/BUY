package com.guozha.buy.entry.mine.order;

import java.io.Serializable;
import java.util.List;

/**
 * 订单摘要分页
 * @author PeggyTong
 *
 */
public class OrderSummaryPage implements Serializable{
	
	private static final long serialVersionUID = -9157229217790661634L;
	
	private int totalCount;		//总条数
	private int pageCount;    	//页数
	private List<OrderSummary> orderList; //该页数据
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public List<OrderSummary> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<OrderSummary> orderList) {
		this.orderList = orderList;
	}
}
