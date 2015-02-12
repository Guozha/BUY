package com.guozha.buy.util;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * 网络操作工具类
 * @author Administrator
 *
 */
public class NetUtil {
	
	private RequestQueue mQueue;
	private Response.ErrorListener errorListener;
	private static NetUtil mNetUtil;
	
	private NetUtil(Context context){
		mQueue = Volley.newRequestQueue(context.getApplicationContext());
		errorListener = new ErrorListener();
	}

	/**
	 * 获取NetUtil实例
	 * @param context
	 * @return
	 */
	public static NetUtil getInstance(Context context){
		if(mNetUtil == null){
			mNetUtil = new NetUtil(context);
		}
		return mNetUtil;
	}

	/**
	 * get方式请求string字符串
	 * @param url
	 * @param responsListener
	 */
	public void volleyRequestByGet(String url, Listener<String> responsListener){
		StringRequest stringRequest = 
				new StringRequest(url, responsListener, errorListener);
		mQueue.add(stringRequest);
	}
	
	/**
	 * post方式请求string字符串
	 * @param url
	 * @param responsListener
	 */
	public void volleyRequestByPost(String url, Listener<String> responsListener){
		StringRequest stringRequest = 
				new StringRequest(Method.POST, url, responsListener, errorListener);  
		mQueue.add(stringRequest);
	}
	
	/**
	 * get方式请求Json格式的数据
	 * @param url
	 * @param responsListener
	 */
	public void volleyJsonRequestByGet(String url, Listener<JSONObject> responsListener){
		JsonObjectRequest jsonObjectRequest = 
				new JsonObjectRequest(url, null, responsListener, errorListener);
		mQueue.add(jsonObjectRequest);
	}
	
	/**
	 * post方式请求Json格式的数据
	 * @param url
	 * @param responsListener
	 */
	public void volleyJsonRequestByPost(String url, Listener<JSONObject> responsListener){
		JsonObjectRequest jsonObjectRequest = 
				new JsonObjectRequest(Method.POST, url, null, responsListener, errorListener);
		mQueue.add(jsonObjectRequest);
	}
	
	
	public void volleyImageRequest(String url, Listener<Bitmap> responsListener){
		ImageRequest imageRequest = 		//第三、四个参数，如果设置为0则默认不压缩，否则会压缩
				new ImageRequest(url, responsListener, 0, 0, Config.ARGB_8888, errorListener);
		mQueue.add(imageRequest);
		

	}
	
	/**
	 * 可以实现图片缓存，避免重复发送请求
	 * @param url
	 * @param imageview
	 * @param defaultImageResId
	 * @param errorImageResId
	 */
	public void volleyImageRequest(String url, 
			ImageView imageview, int defaultImageResId, int errorImageResId){
		ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
		ImageListener listener = 
				ImageLoader.getImageListener(
						imageview, defaultImageResId, errorImageResId);
		//imageLoader.get(url, listener, 0, 0);
		imageLoader.get(url, listener);
	}
	
	/**
	 * 错误结果
	 * @author Administrator
	 *
	 */
	class ErrorListener implements com.android.volley.Response.ErrorListener{

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e("TEST", error.getMessage(), error);  
		}
	}
}
