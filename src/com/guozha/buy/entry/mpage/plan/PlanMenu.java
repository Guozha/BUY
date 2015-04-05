package com.guozha.buy.entry.mpage.plan;

import java.io.Serializable;

/**
 * 计划菜单
 * @author guozha
 *
 */
public class PlanMenu implements Serializable{

	private static final long serialVersionUID = -2097126270148540306L;
	
	private int firstMenuId;
	private String firstMenuName;
	private String firstMenuImg;
	private int firstMenuCheck;
	
	private int secondMenuId;
	private String secondMenuName;
	private String secondMenuImg;
	private int secondMenuCheck;

	private int thirdMenuId;
	private String thirdMenuName;
	private String thirdMenuImg;
	private int thirdMenuCheck;
	
	private int fourMenuId;
	private String fourMenuName;
	private String fourMenuImg;
	private int fourMenuCheck;
	
	private int fiveMenuId;
	private String fiveMenuName;
	private String fiveMenuImg;
	private int fiveMenuCheck;
	
	private int sixMenuId;
	private String sixMenuName;
	private String sixMenuImg;
	private int sixMenuCheck;
	
	private String planDate;
	private int week;
	
	public int getFirstMenuId() {
		return firstMenuId;
	}
	public void setFirstMenuId(int firstMenuId) {
		this.firstMenuId = firstMenuId;
	}
	public String getFirstMenuName() {
		return firstMenuName;
	}
	public void setFirstMenuName(String firstMenuName) {
		this.firstMenuName = firstMenuName;
	}
	public String getFirstMenuImg() {
		return firstMenuImg;
	}
	public void setFirstMenuImg(String firstMenuImg) {
		this.firstMenuImg = firstMenuImg;
	}
	public int getSecondMenuId() {
		return secondMenuId;
	}
	public void setSecondMenuId(int secondMenuId) {
		this.secondMenuId = secondMenuId;
	}
	public String getSecondMenuName() {
		return secondMenuName;
	}
	public void setSecondMenuName(String secondMenuName) {
		this.secondMenuName = secondMenuName;
	}
	public String getSecondMenuImg() {
		return secondMenuImg;
	}
	public void setSecondMenuImg(String secondMenuImg) {
		this.secondMenuImg = secondMenuImg;
	}
	public int getThirdMenuId() {
		return thirdMenuId;
	}
	public void setThirdMenuId(int thirdMenuId) {
		this.thirdMenuId = thirdMenuId;
	}
	public String getThirdMenuName() {
		return thirdMenuName;
	}
	public void setThirdMenuName(String thirdMenuName) {
		this.thirdMenuName = thirdMenuName;
	}
	public String getThirdMenuImg() {
		return thirdMenuImg;
	}
	public void setThirdMenuImg(String thirdMenuImg) {
		this.thirdMenuImg = thirdMenuImg;
	}
	public int getFourMenuId() {
		return fourMenuId;
	}
	public void setFourMenuId(int fourMenuId) {
		this.fourMenuId = fourMenuId;
	}
	public String getFourMenuName() {
		return fourMenuName;
	}
	public void setFourMenuName(String fourMenuName) {
		this.fourMenuName = fourMenuName;
	}
	public String getFourMenuImg() {
		return fourMenuImg;
	}
	public void setFourMenuImg(String fourMenuImg) {
		this.fourMenuImg = fourMenuImg;
	}
	public int getFiveMenuId() {
		return fiveMenuId;
	}
	public void setFiveMenuId(int fiveMenuId) {
		this.fiveMenuId = fiveMenuId;
	}
	public String getFiveMenuName() {
		return fiveMenuName;
	}
	public void setFiveMenuName(String fiveMenuName) {
		this.fiveMenuName = fiveMenuName;
	}
	public String getFiveMenuImg() {
		return fiveMenuImg;
	}
	public void setFiveMenuImg(String fiveMenuImg) {
		this.fiveMenuImg = fiveMenuImg;
	}
	public int getSixMenuId() {
		return sixMenuId;
	}
	public void setSixMenuId(int sixMenuId) {
		this.sixMenuId = sixMenuId;
	}
	public String getSixMenuName() {
		return sixMenuName;
	}
	public void setSixMenuName(String sixMenuName) {
		this.sixMenuName = sixMenuName;
	}
	public String getSixMenuImg() {
		return sixMenuImg;
	}
	public void setSixMenuImg(String sixMenuImg) {
		this.sixMenuImg = sixMenuImg;
	}
	public String getPlanDate() {
		return planDate;
	}
	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getFirstMenuCheck() {
		return firstMenuCheck;
	}
	public void setFirstMenuCheck(int firstMenuCheck) {
		this.firstMenuCheck = firstMenuCheck;
	}
	public int getSecondMenuCheck() {
		return secondMenuCheck;
	}
	public void setSecondMenuCheck(int secondMenuCheck) {
		this.secondMenuCheck = secondMenuCheck;
	}
	public int getThirdMenuCheck() {
		return thirdMenuCheck;
	}
	public void setThirdMenuCheck(int thirdMenuCheck) {
		this.thirdMenuCheck = thirdMenuCheck;
	}
	public int getFourMenuCheck() {
		return fourMenuCheck;
	}
	public void setFourMenuCheck(int fourMenuCheck) {
		this.fourMenuCheck = fourMenuCheck;
	}
	public int getFiveMenuCheck() {
		return fiveMenuCheck;
	}
	public void setFiveMenuCheck(int fiveMenuCheck) {
		this.fiveMenuCheck = fiveMenuCheck;
	}
	public int getSixMenuCheck() {
		return sixMenuCheck;
	}
	public void setSixMenuCheck(int sixMenuCheck) {
		this.sixMenuCheck = sixMenuCheck;
	}
}
