package com.guozha.buy.model;

import android.content.Context;

import com.guozha.buy.util.ToastUtil;

public class BaseModel {
	public static final String REQUEST_SUCCESS = "1";
	
	public void jsonException(Context context){
		ToastUtil.showToast(context, "数据解析异常");
	}
}
