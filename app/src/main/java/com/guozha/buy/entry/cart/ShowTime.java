package com.guozha.buy.entry.cart;

public class ShowTime {
	
	private String dayname;
	private String fromTime;
	private String toTime;
	
	public ShowTime(String dayname, String fromTime, String toTime) {
		super();
		this.dayname = dayname;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}
	
	public String getDayname() {
		return dayname;
	}
	public void setDayname(String dayname) {
		this.dayname = dayname;
	}
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
