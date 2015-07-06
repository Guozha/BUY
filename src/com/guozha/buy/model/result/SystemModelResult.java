package com.guozha.buy.model.result;

import com.guozha.buy.entry.global.SearchResult;
import com.guozha.buy.model.SystemModel.SystemModelInterface;

public class SystemModelResult implements SystemModelInterface{

	@Override
	public void requestSystemTime(long systemTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestSystemSearch(SearchResult searchResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestWarnPlanResult(String returnCode, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestFeadbackResult(String returnCode, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestInviteShareResult(String returnCode, int inviteId,
			String shareUrl, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestInviteInfoResult(int drawAmount, int usedAmount,
			int awardPrice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestImagePathResult(String imgPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestWeixinNumResult(String weixinnum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestServiceFeeRuleResult(String title, String content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestInviteRuleResult(String title, String content) {
		// TODO Auto-generated method stub
		
	}
}
