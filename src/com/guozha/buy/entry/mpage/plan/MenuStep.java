package com.guozha.buy.entry.mpage.plan;

import java.io.Serializable;

/**
 * 菜谱步骤
 * @author PeggyTong
 *
 */
public class MenuStep implements Serializable{

	private static final long serialVersionUID = 2340339665674726630L;
	
	private String stepDesc;
	private String stepImg;
	
	public String getStepDesc() {
		return stepDesc;
	}
	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}
	public String getStepImg() {
		return stepImg;
	}
	public void setStepImg(String stepImg) {
		this.stepImg = stepImg;
	}
	
	
}
