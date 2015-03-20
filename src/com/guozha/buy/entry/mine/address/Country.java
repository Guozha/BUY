package com.guozha.buy.entry.mine.address;

import java.io.Serializable;

/**
 * 区
 * @author PeggyTong
 *
 */
public class Country implements Serializable{

	private static final long serialVersionUID = -7301877121894244299L;
	
	private int areaId;
	private String areaName;
	
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
