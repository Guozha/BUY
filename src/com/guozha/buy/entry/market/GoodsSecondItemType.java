package com.guozha.buy.entry.market;

import java.io.Serializable;

/**
 * 商品二级类目
 * @author PeggyTong
 *
 */
public class GoodsSecondItemType implements Serializable{
	
	private static final long serialVersionUID = 3257954867444841460L;
	
	private String frontTypeId;  //类目ID
	private String shortName;	 //类目简称
	
	public GoodsSecondItemType(String frontTypeId, String shortName) {
		super();
		this.frontTypeId = frontTypeId;
		this.shortName = shortName;
	}
	
	public String getFrontTypeId() {
		return frontTypeId;
	}
	public void setFrontTypeId(String frontTypeId) {
		this.frontTypeId = frontTypeId;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
