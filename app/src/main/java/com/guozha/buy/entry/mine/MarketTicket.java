package com.guozha.buy.entry.mine;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜票
 * @author PeggyTong
 *
 */
public class MarketTicket implements Serializable{

	private static final long serialVersionUID = 7021166583285235584L;
	
	private int myTicketId;
	private String ticketType;
	private int parValue;
	private int forPrice;
	private Date validDate;
	
	public int getMyTicketId() {
		return myTicketId;
	}
	public void setMyTicketId(int myTicketId) {
		this.myTicketId = myTicketId;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public int getParValue() {
		return parValue;
	}
	public void setParValue(int parValue) {
		this.parValue = parValue;
	}
	public int getForPrice() {
		return forPrice;
	}
	public void setForPrice(int forPrice) {
		this.forPrice = forPrice;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
}
