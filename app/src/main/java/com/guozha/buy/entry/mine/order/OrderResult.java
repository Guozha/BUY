package com.guozha.buy.entry.mine.order;

public class OrderResult {

	private String returnCode;
	private String msg;
	private String needPayFlag;
	private Order order;
	
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
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
