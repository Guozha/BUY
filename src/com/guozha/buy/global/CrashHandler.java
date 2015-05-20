package com.guozha.buy.global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 捕获android程序奔溃日志
 * @author PeggyTong
 *
 */
public class CrashHandler implements UncaughtExceptionHandler{
	
	private static CrashHandler instance = new CrashHandler();
	
	private Context mContext;
	//系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();
	// 用于格式化日期,作为日志文件名的一部分
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	
	
	/**
	 * 单例
	 */
	private CrashHandler(){
		
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static CrashHandler getInstance(){
		return instance;
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	public void init(Context context){
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	/**
	 * 发生异常时会转入改函数处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if(!handleException(ex)){
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		}else{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}
	
	/**
	 * 自定义错误处理，收集错误信息，发送错误报告
	 * @param ex
	 */
	private boolean handleException(Throwable ex){
		if(ex == null){
			return false;
		}
		
		//使用Toast显示异常信息
		new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉，程序出现异常", Toast.LENGTH_LONG).show();
				Looper.loop();
			};
		}.start();
		
		//收集设备参数
		collectDeviceInfo(mContext);
		//保存日志文件
		//String str = saveCrashInfo2File(ex);
		return false;
	}
	
	/**
	 * 收集设备信息
	 * @param context
	 */
	public void collectDeviceInfo(Context context){
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? null
						: pi.versionName;
				int versionCode = pi.versionCode;
				infos.put("versionName", versionName);
				infos.put("versionCode", String.valueOf(versionCode));
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append("[" + key + ", " + value + "]\n");
		}
		sb.append(getStackTraceString(ex));
		try {
			String time = formatter.format(new Date());

			TelephonyManager mTelephonyMgr = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			
			String imei = mTelephonyMgr.getDeviceId();
			if (TextUtils.isEmpty(imei)) {
				imei = "unkonowDeviceId";
			}

			String fileName = time + "_" + imei + ".txt";

			File sdDir = null;

			if (Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED))
				sdDir = Environment.getExternalStorageDirectory();

			File cacheDir = new File(sdDir + File.separator + "buy");
			if (!cacheDir.exists())
				cacheDir.mkdir();

			File filePath = new File(cacheDir + File.separator + fileName);

			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(sb.toString().getBytes());
			fos.close();

			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 获取捕捉到的异常的字符串
	 */
	public static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}

		Throwable t = tr;
		
		while (t != null) {
			if (t instanceof UnknownHostException)
			{
				return "";
			}
			t = t.getCause();
		}

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		return sw.toString();
	}
}
