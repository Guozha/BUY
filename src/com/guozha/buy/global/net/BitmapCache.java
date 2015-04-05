package com.guozha.buy.global.net;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import libcore.io.DiskLruCache;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.guozha.buy.util.LogUtil;

/**
 * 实现图片的缓存
 * @author Administrator
 *
 */
public class BitmapCache implements ImageCache{
	
	/**
	 * 图片硬盘缓存核心类。
	 */
	private DiskLruCache mDiskLruCache;
	
	/**
	 * 记录所有正在下载或等待下载的任务。
	 */
	private Set<BitmapWorkerTask> taskCollection;
	
	private LruCache<String, Bitmap> mLruCache;
	
	public BitmapCache(Context context){
		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// 使用内存的1/8缓存图片
		int cacheSize = maxMemory / 10;  
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {  
            @Override  
            protected int sizeOf(String key, Bitmap bitmap) {  
                return bitmap.getByteCount();
            }  
        };  
        
		try {
			// 获取图片缓存路径
			File cacheDir = getDiskCacheDir(context, "guozha");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			// 创建DiskLruCache实例，初始化缓存数据
			mDiskLruCache = DiskLruCache
					.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
		taskCollection = new HashSet<BitmapWorkerTask>();
	}
	
	/**
	 * 根据传入的uniqueName获取硬盘缓存的路径地址。
	 */
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			File cacheDir = context.getExternalCacheDir();
			if(cacheDir == null){
				cachePath = context.getCacheDir().getPath();
			}else{
				cachePath = cacheDir.getPath();
			}
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 获取当前应用程序的版本号。
	 */
	public int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
					0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public Bitmap getBitmap(String url) {
		return mLruCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mLruCache.put(url, bitmap);
	}
	
	/**
	 * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
	 * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
	 */
	public void loadBitmaps(ImageView imageView, String imageUrl) {
		if(imageUrl == null) return;
		imageUrl = HttpManager.URL + imageUrl;
		try {
			Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
			if (bitmap == null) {
				BitmapWorkerTask task = new BitmapWorkerTask(
						mLruCache, mDiskLruCache, taskCollection, imageView);
				taskCollection.add(task);
				task.execute(imageUrl);
			} else {
				if (imageView != null && bitmap != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return getBitmap(key);
	}

}
