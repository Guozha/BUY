package com.guozha.buy.global.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import libcore.io.DiskLruCache;
import libcore.io.DiskLruCache.Snapshot;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.guozha.buy.util.HttpUtil;
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
	private Set<BitmapWorkerTask> mTaskCollection;
	
	private LruCache<String, Bitmap> mLruCache;
	
	private Context mContext;
	
	public BitmapCache(){
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
	}
	
	public BitmapCache(Context context){
		mContext = context;
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
		mTaskCollection = new HashSet<BitmapWorkerTask>();
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
				BitmapWorkerTask task = new BitmapWorkerTask(imageView);
				mTaskCollection.add(task);
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
	
	/////////////////////////////////////异步任务////////////////////////////////////
	
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private ImageView mImageView;
		public BitmapWorkerTask(ImageView imageView){
			mImageView = imageView;
		}
		/**
		 * 图片的URL地址
		 */
		private String imageUrl;

		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			FileDescriptor fileDescriptor = null;
			FileInputStream fileInputStream = null;
			Snapshot snapShot = null;
			try {
				// 生成图片URL对应的key
				final String key = HttpUtil.hashKeyForDisk(imageUrl);
				// 查找key对应的缓存
				if(mDiskLruCache == null) return null;
				snapShot = mDiskLruCache.get(key);
				if (snapShot == null) {
					// 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
					DiskLruCache.Editor editor = mDiskLruCache.edit(key);
					if (editor != null) {
						OutputStream outputStream = editor.newOutputStream(0);
						if (downloadUrlToStream(imageUrl, outputStream)) {
							editor.commit();
						} else {
							editor.abort();
						}
					}
					// 缓存被写入后，再次查找key对应的缓存
					snapShot = mDiskLruCache.get(key);
				}
				if (snapShot != null) {
					fileInputStream = (FileInputStream) snapShot.getInputStream(0);
					fileDescriptor = fileInputStream.getFD();
				}
				// 将缓存数据解析成Bitmap对象
				Bitmap bitmap = null;
				if (fileDescriptor != null) {
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}
				if (bitmap != null) {
					// 将Bitmap对象添加到内存缓存当中
					addBitmapToMemoryCache(params[0], bitmap);
				}
				return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileDescriptor == null && fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			// 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
			//TODO
			//ImageView imageView = (ImageView) mParentView.findViewWithTag(imageUrl);
			if(bitmap == null){
				HttpManager.getInstance(mContext).volleyImageRequest(imageUrl, mImageView, 0, 0);
			}
			if(mImageView != null && bitmap != null){
				mImageView.setImageBitmap(bitmap);
			}
			mTaskCollection.remove(this);
		}

		/**
		 * 建立HTTP请求，并获取Bitmap对象。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 解析后的Bitmap对象
		 */
		private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
			HttpURLConnection urlConnection = null;
			BufferedOutputStream out = null;
			BufferedInputStream in = null;
			try {
				final URL url = new URL(urlString);
				urlConnection = (HttpURLConnection) url.openConnection();
				in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
				out = new BufferedOutputStream(outputStream, 8 * 1024);
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				return true;
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		
		/**
		 * 将一张图片存储到LruCache中。
		 * 
		 * @param key
		 *            LruCache的键，这里传入图片的URL地址。
		 * @param bitmap
		 *            LruCache的键，这里传入从网络上下载的Bitmap对象。
		 */
		public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
			if (getBitmapFromMemoryCache(key) == null) {
				putBitmap(key, bitmap);
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

}
