package com.guozha.buy.entry.cart;

import java.io.Serializable;

/**
 * 时间点选择
 * @author PeggyTong
 *
 */
public class PointTime implements Serializable{
	
	private static final long serialVersionUID = -5000184442522308321L;
	
	private String upTime;	   //上限时间
	private String downTime;   //下限时间
	private String dateTime;   //日期
	private String timeTitle;  //时间描述
	
	public PointTime(String upTime, String downTime, String dateTime, String timeTitle) {
		super();
		this.upTime = upTime;
		this.downTime = downTime;
		this.dateTime = dateTime;
		this.timeTitle = timeTitle;
	}
	public String getUpTime() {
		return upTime;
	}
	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}
	public String getDownTime() {
		return downTime;
	}
	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getTimeTitle() {
		return timeTitle;
	}
	public void setTimeTitle(String timeTitle) {
		this.timeTitle = timeTitle;
	}
}
