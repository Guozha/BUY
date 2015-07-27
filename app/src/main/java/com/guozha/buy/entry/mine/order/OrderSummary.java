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
	private String orderType;	//订单类型
	private String orderNo;		//订单编号
	private Date createTime;	//创建时间
	private int quantity;		//总件数
	private String totalPrice;	//总金额
	private String arrivalPayFlag;	//货到付款标识
	private String finishPayFlag;	//付款完成标识
	private String commentFlag;		//评价完成标识
	private Date aboutArrivalTime; 	//预计到货时间
	private String status;			//状态
	private String statusDesc;		//状态描述
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
	public String getArrivalPayFlag() {
		return arrivalPayFlag;
	}
	public void setArrivalPayFlag(String arrivalPayFlag) {
		this.arrivalPayFlag = arrivalPayFlag;
	}
	public String getFinishPayFlag() {
		return finishPayFlag;
	}
	public void setFinishPayFlag(String finishPayFlag) {
		this.finishPayFlag = finishPayFlag;
	}
	public String getCommentFlag() {
		return commentFlag;
	}
	public void setCommentFlag(String commentFlag) {
		this.commentFlag = commentFlag;
	}
	public Date getAboutArrivalTime() {
		return aboutArrivalTime;
	}
	public void setAboutArrivalTime(Date aboutArrivalTime) {
		this.aboutArrivalTime = aboutArrivalTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
}
