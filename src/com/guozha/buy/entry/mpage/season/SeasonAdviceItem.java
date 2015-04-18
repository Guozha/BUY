package com.guozha.buy.entry.mpage.season;

import java.io.Serializable;

/**
 * 时令推荐菜品
 * @author PeggyTong
 *
 */
public class SeasonAdviceItem implements Serializable{

	private static final long serialVersionUID = -284225141266755419L;
	
	private int goodsId;  		//商品ID
	private String goodsName;  	//商品名称
	private String memo;   		//商品描述
	private String goodsImg;   	//商品图标
	private String word;		//关键词
	
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
}
