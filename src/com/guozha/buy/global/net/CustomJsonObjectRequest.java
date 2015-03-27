package com.guozha.buy.global.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

public class CustomJsonObjectRequest extends JsonObjectRequest{

	public CustomJsonObjectRequest(int method, String url,
			JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	public CustomJsonObjectRequest(String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
	}
	
	
	@Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            // solution 1:
            //String jsonString = new String(response.data, "UTF-8");
            // solution 2:
            response.headers.put(HTTP.CONTENT_TYPE,
                response.headers.get("content-type"));
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            //
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
    
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();  
		headers.put("Charset", "UTF-8");  
		headers.put("Content-Type", "application/x-javascript");  
		headers.put("Accept-Encoding", "gzip,deflate");  
		return headers;  
	}
	
}
