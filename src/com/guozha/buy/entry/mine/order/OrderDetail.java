package com.guozha.buy.entry.mine.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单详情
 * @author PeggyTong
 *
 */
public class OrderDetail implements Serializable{

	private static final long serialVersionUID = -6285559783997107505L;
	
	private int orderId;
	private String orderType;
	private String orderNo;
	private Date createTime;
	private Date arrivalTime;
	private String wantArrivalTimeScope;
	private String receiveMen;
	private String receiveMobile;
	private String receiveAddr;
	private int serviceFee;
	private int quantity;
	private int totalPrice;
	private String status;
	private List<OrderDetailGoods> goodsInfoList;
	private List<OrderDetailMenus> menuInfoList;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getReceiveMen() {
		return receiveMen;
	}
	public void setReceiveMen(String receiveMen) {
		this.receiveMen = receiveMen;
	}
	public String getReceiveMobile() {
		return receiveMobile;
	}
	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}
	public String getReceiveAddr() {
		return receiveAddr;
	}
	public void setReceiveAddr(String receiveAddr) {
		this.receiveAddr = receiveAddr;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<OrderDetailGoods> getGoodsInfoList() {
		return goodsInfoList;
	}
	public void setGoodsInfoList(List<OrderDetailGoods> goodsInfoList) {
		this.goodsInfoList = goodsInfoList;
	}
	public List<OrderDetailMenus> getMenuInfoList() {
		return menuInfoList;
	}
	public void setMenuInfoList(List<OrderDetailMenus> menuInfoList) {
		this.menuInfoList = menuInfoList;
	}
	public int getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(int serviceFee) {
		this.serviceFee = serviceFee;
	}
	public String getWantArrivalTimeScope() {
		return wantArrivalTimeScope;
	}
	public void setWantArrivalTimeScope(String wantArrivalTimeScope) {
		this.wantArrivalTimeScope = wantArrivalTimeScope;
	}
}
