package com.guozha.buy.entry;

import java.io.Serializable;

/**
 * 账户信息
 * @author PeggyTong
 *
 */
public class AccountInfo implements Serializable{

	private static final long serialVersionUID = -6994116826505095239L;
	
	private String mobileNo;  //电话
	private byte[] headImg;   //头像二进制
	private int balance;	  //余额
	private int ticketAmount; //彩票数
	private int beanAmount;   //菜豆数
	
	public AccountInfo(String mobileNo, byte[] headImg, int balance,
			int ticketAmount, int beanAmount) {
		super();
		this.mobileNo = mobileNo;
		this.headImg = headImg;
		this.balance = balance;
		this.ticketAmount = ticketAmount;
		this.beanAmount = beanAmount;
	}
	
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public byte[] getHeadImg() {
		return headImg;
	}
	public void setHeadImg(byte[] headImg) {
		this.headImg = headImg;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getTicketAmount() {
		return ticketAmount;
	}
	public void setTicketAmount(int ticketAmount) {
		this.ticketAmount = ticketAmount;
	}
	public int getBeanAmount() {
		return beanAmount;
	}
	public void setBeanAmount(int beanAmount) {
		this.beanAmount = beanAmount;
	}
}
