package com.guozha.buy.entry.mine.order;

import java.util.Date;

public class Order {
	
	private int orderId;
	private String orderNo;
	private int quantity;
	private String memo;
	private Date createTime;
	private int totalPrice;
	private int balanceDecPrice;
	private int beanDecPrice;
	private int serviceFee;
	private int payPrice;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getBalanceDecPrice() {
		return balanceDecPrice;
	}
	public void setBalanceDecPrice(int balanceDecPrice) {
		this.balanceDecPrice = balanceDecPrice;
	}
	public int getBeanDecPrice() {
		return beanDecPrice;
	}
	public void setBeanDecPrice(int beanDecPrice) {
		this.beanDecPrice = beanDecPrice;
	}
	public int getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(int serviceFee) {
		this.serviceFee = serviceFee;
	}
	public int getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(int payPrice) {
		this.payPrice = payPrice;
	}
}
