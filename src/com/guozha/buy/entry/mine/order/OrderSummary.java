package com.guozha.buy.entry.mine.order;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单简要
 * @author PeggyTong
 *
 */
public class OrderSummary implements Serializable{

	private static final long serialVersionUID = 1557375615752897419L;
	
	private int orderId;  		//订单id
	private String orderNo;  	//订单编号
	private Date createTime;  	//创建时间
	private int quantity;		//总件数
	private String totalPrice;  //总金额
	private String status;		//状态
	
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
