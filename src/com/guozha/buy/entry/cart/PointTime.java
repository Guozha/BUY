package com.guozha.buy.entry.cart;

import java.io.Serializable;

/**
 * 时间点选择
 * @author PeggyTong
 *
 */
public class PointTime implements Serializable{
	
	private static final long serialVersionUID = -5000184442522308321L;
	
	private String fromTime;	   //上限时间
	private String toTime;   //下限时间
	
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
}
