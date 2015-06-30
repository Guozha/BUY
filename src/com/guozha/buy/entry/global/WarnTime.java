package com.guozha.buy.entry.global;

public class WarnTime {
	private String showTime;	//显示的时间
	private String valueTime;	//发送的时间
	public WarnTime(String showTime, String valueTime){
		this.showTime = showTime;
		this.valueTime = valueTime;
	}
	public String getShowTime() {
		return showTime;
	}
	public String getValueTime() {
		return valueTime;
	}
}
