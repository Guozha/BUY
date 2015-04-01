package com.guozha.buy.global.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import libcore.io.DiskLruCache;
import libcore.io.DiskLruCache.Snapshot;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.guozha.buy.util.HttpUtil;

/**
 * 图片异步下载任务
 * @author PeggyTong
 *
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	
	private DiskLruCache mDiskLruCache;
	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private LruCache<String, Bitmap> mMemoryCache;
	/**
	 * 记录所有正在下载或等待下载的任务。
	 */
	private Set<BitmapWorkerTask> mTaskCollection;
	
	private View mParentView;
	
	public BitmapWorkerTask(
			LruCache<String, Bitmap> mLruCache,
			DiskLruCache diskLruCache, 
			Set<BitmapWorkerTask> taskCollection,
			View parentView){
		mDiskLruCache = diskLruCache;
		mMemoryCache = mLruCache;
		mTaskCollection = taskCollection;
		mParentView = parentView;
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
		ImageView imageView = (ImageView) mParentView.findViewWithTag(imageUrl);
		if (imageView != null && bitmap != null) {
			imageView.setImageBitmap(bitmap);
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
			mMemoryCache.put(key, bitmap);
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
		return mMemoryCache.get(key);
	}
}
