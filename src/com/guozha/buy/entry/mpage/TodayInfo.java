package com.guozha.buy.entry.mpage;

/**
 * 首页的当日信息
 * @author PeggyTong
 *
 */
public class TodayInfo {
	private String calendarSolar;
	private String calendarLunar;
	private String todayDescript;
	
	public TodayInfo(String calendarSolar, String calendarLunar,
			String todayDescript) {
		super();
		this.calendarSolar = calendarSolar;
		this.calendarLunar = calendarLunar;
		this.todayDescript = todayDescript;
	}
	
	public String getCalendarSolar() {
		return calendarSolar;
	}
	public void setCalendarSolar(String calendarSolar) {
		this.calendarSolar = calendarSolar;
	}
	public String getCalendarLunar() {
		return calendarLunar;
	}
	public void setCalendarLunar(String calendarLunar) {
		this.calendarLunar = calendarLunar;
	}
	public String getTodayDescript() {
		return todayDescript;
	}
	public void setTodayDescript(String todayDescript) {
		this.todayDescript = todayDescript;
	}
}
