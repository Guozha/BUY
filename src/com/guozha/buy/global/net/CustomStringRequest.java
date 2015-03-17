package com.guozha.buy.global.net;

import com.android.volley.RetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

public class CustomStringRequest extends StringRequest{

	public CustomStringRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	public CustomStringRequest(int method, String url,
			Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RetryPolicy getRetryPolicy() {
		
		return super.getRetryPolicy();
	}
}
