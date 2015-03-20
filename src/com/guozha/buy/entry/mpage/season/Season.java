package com.guozha.buy.entry.mpage.season;

import java.io.Serializable;
import java.util.List;

/**
 * 节气
 * @author PeggyTong
 *
 */
public class Season implements Serializable{
	
	private static final long serialVersionUID = -4058495099470042309L;
	
	private String season;  		//节气
	private String seasonPicUrl;  	//节气图片地址
	private List<SeasonAdviceItem> goodsList;  //推荐商品列表
	
	public Season(String season, String seasonPicUrl,
			List<SeasonAdviceItem> goodsList) {
		super();
		this.season = season;
		this.seasonPicUrl = seasonPicUrl;
		this.goodsList = goodsList;
	}
	
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getSeasonPicUrl() {
		return seasonPicUrl;
	}
	public void setSeasonPicUrl(String seasonPicUrl) {
		this.seasonPicUrl = seasonPicUrl;
	}
	public List<SeasonAdviceItem> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<SeasonAdviceItem> goodsList) {
		this.goodsList = goodsList;
	}
	
}
