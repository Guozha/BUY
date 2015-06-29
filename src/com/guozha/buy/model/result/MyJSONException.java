package com.guozha.buy.model.result;

import org.json.JSONException;

import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.util.ToastUtil;

public class MyJSONException extends JSONException{

	private static final long serialVersionUID = -1451134460356780997L;

	public MyJSONException(String s) {
		super(s);
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
		ToastUtil.showToast(CustomApplication.getContext(), "JSON数据解析异常");
	}
}
