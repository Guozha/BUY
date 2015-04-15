package com.guozha.buy.entry.cart;

import java.io.Serializable;

/**
 * 特供预售订单信息（4.3 4.4接口）
 * @author PeggyTong
 *
 */
public class PreSpecialOrder implements Serializable{

	private static final long serialVersionUID = -4587623121147451824L;
	
	private String returnCode;
	private String msg;
	private String needPayFlag;
	private PayOrderMesg order;
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getNeedPayFlag() {
		return needPayFlag;
	}
	public void setNeedPayFlag(String needPayFlag) {
		this.needPayFlag = needPayFlag;
	}
	public PayOrderMesg getOrder() {
		return order;
	}
	public void setOrder(PayOrderMesg order) {
		this.order = order;
	}
}
