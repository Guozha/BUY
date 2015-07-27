package com.guozha.buy.entry.market;

import java.io.Serializable;

/**
 * 商品二级类目
 * @author PeggyTong
 *
 */
public class GoodsSecondItemType implements Serializable{
	
	private static final long serialVersionUID = 3257954867444841460L;
	
	private int frontTypeId;  //类目ID
	private String shortName;	 //类目简称
	private String typeName;	//类目名称
	
	public GoodsSecondItemType(int frontTypeId, String shortName,
			String typeName) {
		super();
		this.frontTypeId = frontTypeId;
		this.shortName = shortName;
		this.typeName = typeName;
	}
	
	public int getFrontTypeId() {
		return frontTypeId;
	}
	public void setFrontTypeId(int frontTypeId) {
		this.frontTypeId = frontTypeId;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
