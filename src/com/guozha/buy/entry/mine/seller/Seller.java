package com.guozha.buy.entry.mine.seller;

/**
 * 我的卖家
 * @author PeggyTong
 *
 */
public class Seller {

	private int mySellerId;     //业务id
	private int sellerId;		//卖家id
	private String sellerName;	//卖家名称
	private byte[] logo;		//Logo
	private String mainBusi;    //主营介绍
	private int transCount;     //交易次数
	private String sellerTag; 	//卖家标签
	
	public int getMySellerId() {
		return mySellerId;
	}
	public void setMySellerId(int mySellerId) {
		this.mySellerId = mySellerId;
	}
	public int getSellerId() {
		return sellerId;
	}
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	public String getMainBusi() {
		return mainBusi;
	}
	public void setMainBusi(String mainBusi) {
		this.mainBusi = mainBusi;
	}
	public int getTransCount() {
		return transCount;
	}
	public void setTransCount(int transCount) {
		this.transCount = transCount;
	}
	public String getSellerTag() {
		return sellerTag;
	}
	public void setSellerTag(String sellerTag) {
		this.sellerTag = sellerTag;
	}
}
