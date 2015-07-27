package com.guozha.buy.entry.cart;

import java.io.Serializable;

public class PayWayEntry implements Serializable{
	private static final long serialVersionUID = 690806151082376084L;
	int payWayId;
	String payWayName;
	public int getPayWayId() {
		return payWayId;
	}
	public void setPayWayId(int payWayId) {
		this.payWayId = payWayId;
	}
	public String getPayWayName() {
		return payWayName;
	}
	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}
	
	
}
