package com.guozha.buy.entry.market;

import java.io.Serializable;
import java.util.List;

/**
 * 商品条目列表（包括二级条目）
 * @author PeggyTong
 *
 */
public class GoodsItemType implements Serializable{

	private static final long serialVersionUID = 2292788298656313732L;
	
	private int frontTypeId;  	//商品ID
	private String shortName;       //类目简称
	private String typeName;		//类目名称
	private List<GoodsSecondItemType> frontTypeList;  //二级类目
	
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
	public List<GoodsSecondItemType> getFrontTypeList() {
		return frontTypeList;
	}
	public void setFrontTypeList(List<GoodsSecondItemType> frontTypeList) {
		this.frontTypeList = frontTypeList;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
