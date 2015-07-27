package com.guozha.buy.entry.cart;

import java.util.List;

/**
 * 时间列表
 * @author PeggyTong
 *
 */
public class TimeList {
	
	private List<PointTime> todayTimeList;
	private List<PointTime> tomorrowTimeList;
	
	public List<PointTime> getTodayTimeList() {
		return todayTimeList;
	}
	public void setTodayTimeList(List<PointTime> todayTimeList) {
		this.todayTimeList = todayTimeList;
	}
	public List<PointTime> getTomorrowTimeList() {
		return tomorrowTimeList;
	}
	public void setTomorrowTimeList(List<PointTime> tomorrowTimeList) {
		this.tomorrowTimeList = tomorrowTimeList;
	}
}
