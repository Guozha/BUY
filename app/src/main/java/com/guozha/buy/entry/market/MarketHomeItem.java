package com.guozha.buy.entry.market;

import java.io.Serializable;
import java.util.List;


/**
 * 逛菜场首页的每一类商品（包含6个简要商品信息）
 * @author PeggyTong
 *
 */
public class MarketHomeItem implements Serializable{
	
	private static final long serialVersionUID = -2867434615615216044L;
	
	private int frontTypeId;	//类目ID
	private String typeName;	//类目名称
	private String shortName; 	//类目简介
	
	private List<ItemSaleInfo> goodsList; 	//商品列表

	public int getFrontTypeId() {
		return frontTypeId;
	}

	public void setFrontTypeId(int frontTypeId) {
		this.frontTypeId = frontTypeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<ItemSaleInfo> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<ItemSaleInfo> goodsList) {
		this.goodsList = goodsList;
	}
	
}
