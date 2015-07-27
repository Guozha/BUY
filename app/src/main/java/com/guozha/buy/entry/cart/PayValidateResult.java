package com.guozha.buy.entry.cart;

import java.io.Serializable;

/**
 * 支付验证结果
 * @author PeggyTong
 *
 */
public class PayValidateResult implements Serializable{

	private static final long serialVersionUID = 7132685067642084459L;
	
	private String returnCode;
	private String msg;
	private String needPayFlag; //是否需要调用第三方支付
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
	public PayOrderMesg getOrder() {
		return order;
	}
	public void setOrder(PayOrderMesg order) {
		this.order = order;
	}
	public String getNeedPayFlag() {
		return needPayFlag;
	}
	public void setNeedPayFlag(String needPayFlag) {
		this.needPayFlag = needPayFlag;
	}
}
