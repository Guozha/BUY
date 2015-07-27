package com.guozha.buy.entry.mine;

import java.io.Serializable;
import java.util.List;

/**
 * 有效菜票
 * @author PeggyTong
 *
 */
public class UsefulTicket implements Serializable{

	private static final long serialVersionUID = 7193399639563157940L;
	
	private String returnCode;
	private String msg;
	private List<MarketTicket> tickets;
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<MarketTicket> getTickets() {
		return tickets;
	}
	public void setTickets(List<MarketTicket> tickets) {
		this.tickets = tickets;
	}
	
	
}
