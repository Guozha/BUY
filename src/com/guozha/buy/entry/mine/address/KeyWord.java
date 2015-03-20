package com.guozha.buy.entry.mine.address;

import java.io.Serializable;

/**
 * 关键字
 * @author PeggyTong
 *
 */
public class KeyWord implements Serializable{
	
	private static final long serialVersionUID = -4798773994361661699L;
	
	private int buildingId;
	private String buildingName;
	
	public int getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	
	

}
