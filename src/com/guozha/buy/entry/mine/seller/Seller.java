package com.guozha.buy.entry.mine.seller;

/**
 * 我的卖家
 * @author PeggyTong
 *
 */
public class Seller {

	private int mySellerId;
	private int sellerId;
	private String sellerName;
	private byte[] logo;
	private String mainBusi;
	private String sellerTag;
	
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
	public String getSellerTag() {
		return sellerTag;
	}
	public void setSellerTag(String sellerTag) {
		this.sellerTag = sellerTag;
	}
}
