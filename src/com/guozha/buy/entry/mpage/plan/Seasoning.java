package com.guozha.buy.entry.mpage.plan;

import java.io.Serializable;

/**
 * 调味品
 * @author PeggyTong
 *
 */
public class Seasoning implements Serializable{
	
	private static final long serialVersionUID = -5717458724057508601L;
	
	private String seasoningsName;
	private String seasoningsAmount;
	
	public String getSeasoningsName() {
		return seasoningsName;
	}
	public void setSeasoningsName(String seasoningsName) {
		this.seasoningsName = seasoningsName;
	}
	public String getSeasoningsAmount() {
		return seasoningsAmount;
	}
	public void setSeasoningsAmount(String seasoningsAmount) {
		this.seasoningsAmount = seasoningsAmount;
	}
	
}
