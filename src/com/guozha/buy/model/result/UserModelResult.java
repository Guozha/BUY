package com.guozha.buy.model.result;

import java.util.List;

import com.guozha.buy.entry.global.UserInfor;
import com.guozha.buy.entry.mine.MarketTicket;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.entry.mine.address.AddressInfo;
import com.guozha.buy.entry.mine.address.Country;
import com.guozha.buy.entry.mine.address.KeyWord;
import com.guozha.buy.model.UserModel.UserModelInterface;

public class UserModelResult implements UserModelInterface{

	@Override
	public void obtainPhoneValidateResult(String returnCode, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestCheckLogin(String returnCode, String msg,
			UserInfor userInfor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestPasswordLogin(String returnCode, String msg,
			UserInfor userInfor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestListAddressResult(List<AddressInfo> adressInfos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestCountryListResult(List<Country> countrys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestAddressBuilding(List<KeyWord> keyWords) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestAddAddressResult(String returnCode, String buildFlag,
			String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestDeleteAddressResult(String returnCode, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestDefaultAddressResult(String returnCode, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestAccountInfoResult(AccountInfo accountInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestLoginOutResult(String returnCode, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestMyTicketResult(List<MarketTicket> marketTickets) {
		// TODO Auto-generated method stub
		
	}
}
