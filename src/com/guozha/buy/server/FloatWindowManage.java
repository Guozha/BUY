package com.guozha.buy.server;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.guozha.buy.controller.CustomApplication;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.view.AddCartAnimWindow;
import com.guozha.buy.view.AddCartAnimWindow.OnAnimEndListener;

public class FloatWindowManage {
	private static WindowManager mWindowManager;
	private static AddCartAnimWindow mCollectAnimWindow;
	private static LayoutParams mCollectWindowParams;
	
	private FloatWindowManage(){ }
	
	public enum CartDirection{
		TOP,
		BOTTOM
	}
	
	private static WindowManager getWindowManager(Context context){
		if(mWindowManager == null){
			mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

	public static AddCartAnimWindow createAddCartWindow(String showText, CartDirection cartDirection){
		Context context = CustomApplication.getContext();
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = DimenUtil.getScreenWidth(context);
		int screenHeight = DimenUtil.getScreenHeight(context);
		if(mCollectAnimWindow == null){
			mCollectAnimWindow = new AddCartAnimWindow(showText, cartDirection, mWindowManager, context);
			mCollectWindowParams = new LayoutParams();
			mCollectWindowParams.type = LayoutParams.TYPE_PHONE;  
			mCollectWindowParams.format = PixelFormat.RGBA_8888;  
			mCollectWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL  
                    | LayoutParams.FLAG_NOT_FOCUSABLE;  
			mCollectWindowParams.gravity = Gravity.LEFT | Gravity.TOP;  
			//mCollectWindowParams.width = CollectionAnimWindow.maxWindowDimen;  
			//mCollectWindowParams.height = CollectionAnimWindow.maxWindowDimen;  
			///mCollectWindowParams.x = (screenWidth - CollectionAnimWindow.maxWindowDimen) / 2;  
			//mCollectWindowParams.y = (screenHeight - CollectionAnimWindow.maxWindowDimen) / 2; 
			mCollectWindowParams.width = screenWidth;
			mCollectWindowParams.height = screenHeight;
			mCollectWindowParams.x = 0;
			mCollectWindowParams.y = 0;
			mCollectAnimWindow.setParams(mCollectWindowParams); 
			mCollectAnimWindow.setOnAnimEndListener(new OnAnimEndListener() {
				@Override
				public void animEnd() {
					removeAddCartWindow();
				}
			});
            windowManager.addView(mCollectAnimWindow, mCollectWindowParams);  
		}
		return mCollectAnimWindow;
	}
	
    public static void removeAddCartWindow() {  
    	Context context = CustomApplication.getContext();
        if (mCollectAnimWindow != null) {  
            WindowManager windowManager = getWindowManager(context);  
            windowManager.removeView(mCollectAnimWindow);  
          	mCollectAnimWindow = null;  
        }  
    } 
}
