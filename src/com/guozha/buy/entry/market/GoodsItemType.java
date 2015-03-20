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
	
	private String frontTypeId;  	//商品ID
	private String shortName;       //类目简称
	private List<GoodsSecondItemType> frontTypeList;  //二级类目
	
	public GoodsItemType(String frontTypeId, String shortName,
			List<GoodsSecondItemType> frontTypeList) {
		super();
		this.frontTypeId = frontTypeId;
		this.shortName = shortName;
		this.frontTypeList = frontTypeList;
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
	public List<GoodsSecondItemType> getFrontTypeList() {
		return frontTypeList;
	}
	public void setFrontTypeList(List<GoodsSecondItemType> frontTypeList) {
		this.frontTypeList = frontTypeList;
	}
}
