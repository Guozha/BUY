package com.guozha.buy.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.pay.Keys;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	public static final int RESULT_CODE = 0x0002;
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	api = WXAPIFactory.createWXAPI(this, Keys.APP_ID);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		/**
		String status = "0";
		if(resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
			if(resp.errCode == 0){ //成功
				status = "1";
				ToastUtil.showToast(this, "支持成功");
			}else{
				status = "0";
				ToastUtil.showToast(this, "支付失败:" + resp.errStr);
			}
		}
		Intent intent = getIntent();
		intent.putExtra("status", status);
		setResult(RESULT_CODE, intent);
		**/
		this.finish();
	}
}