package com.guozha.buy.global.net;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
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
import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.Util;

/**
 * 网络操作工具类
 * @author Administrator
 *
 */
public class HttpManager {
	
	private RequestQueue mQueue;	//请求队列
	private Response.ErrorListener errorListener;  //错误消息监听
	private static HttpManager mNetUtil;
	
	private HttpManager(Context context){
		mQueue = Volley.newRequestQueue(context.getApplicationContext());
		errorListener = new ErrorListener();
	}

	/**
	 * 获取NetUtil实例
	 * @param context
	 * @return
	 */
	public static HttpManager getInstance(Context context){
		if(mNetUtil == null){
			mNetUtil = new HttpManager(context);
		}
		return mNetUtil;
	}

	/**
	 * get方式请求string字符串
	 * @param url
	 * @param responsListener
	 */
	//public void volleyRequestByGet(String url, Listener<String> responsListener){
	//	StringRequest stringRequest = 
	//			new StringRequest(url, responsListener, errorListener);
	//	mQueue.add(stringRequest);
	//}
	
	/**
	 * post方式请求string字符串
	 * @param url
	 * @param responsListener
	 */
	//public void volleyRequestByPost(String url, Listener<String> responsListener){
	//	StringRequest stringRequest = 
	//			new StringRequest(Method.POST, url, responsListener, errorListener);  
	//	mQueue.add(stringRequest);
	//}
	
	/**
	 * post方式请求string字符串
	 * @param url
	 * @param responsListener
	 */
	public void volleyRequestByPost(RequestParam paramPath, Listener<String> responsListener){
		//volleyRequestByPost(paramPath.toString(), responsListener);
		StringRequest stringRequest = 
				new GZStringRequest(paramPath.getParams(), Method.POST, paramPath.getUrl(), responsListener, errorListener);  
		mQueue.add(stringRequest);
	}
	
	
	/**
	 * get方式请求Json格式的数据
	 * @param url
	 * @param responsListener
	 */
	//public void volleyJsonRequestByGet(String url, Listener<JSONObject> responsListener){
	//	JsonObjectRequest jsonObjectRequest = 
	//			new JsonObjectRequest(url, null, responsListener, errorListener);
	//	mQueue.add(jsonObjectRequest);
	//}
	
	/**
	 * post方式请求Json格式的数据
	 * @param url
	 * @param responsListener
	 */
	//public void volleyJsonRequestByPost(String url, Listener<JSONObject> responsListener){
	//	JsonObjectRequest jsonObjectRequest = 
	//			new JsonObjectRequest(Method.POST, url, null, responsListener, errorListener);
	//	mQueue.add(jsonObjectRequest);
	//}
	
	/**
	 * post方式请求Json格式的数据
	 * @param url
	 * @param responsListener
	 */
	//public void volleyJsonRequestByPost(RequestParam paramPath, Listener<JSONObject> responsListener){
		//volleyJsonRequestByPost(paramPath.toString(), responsListener);
	//	JSONObject jsonObject = new JSONObject(paramPath.getParams());
	//	LogUtil.e("jsonObject == " + jsonObject);
	//	JsonObjectRequest jsonObjectRequest = 
	//			new JsonObjectRequest(Method.POST, paramPath.getUrl(), jsonObject, responsListener, errorListener);
	//	mQueue.add(jsonObjectRequest);
	//}
	
	/**
	 * 请求图片资源
	 * @param url
	 * @param responsListener
	 */
	 void volleyImageRequest(String url, Listener<Bitmap> responsListener){
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
		ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.getInstance());
		ImageListener listener = 
				ImageLoader.getImageListener(
						imageview, defaultImageResId, errorImageResId);
		//这里的第三个和第四个参数是图片允许的最大宽度和高度
		//imageLoader.get(url, listener, 0, 0);
		if(url == null) return;
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
			if(!Util.isNetConnection(CustomApplication.getContext())){
				ToastUtil.showToast(CustomApplication.getContext(), "你的网络已经断开");
			}else{
				ToastUtil.showToast(CustomApplication.getContext(), error.getMessage());
			}
		}
	}
}
