package com.guozha.buy.global.weak;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

/**
 * 所有主线程中的Handler,或在其它长时间运行的线程里的Handler
 * 一定不要使用“内部非静态类”或“内部匿名类”的方式
 * 改为使用“内部静态类”并从本类派生，通过弱引用来访问外部类实例
 * @author PeggyTong
 *
 * @param <T> 外部类
 */

public abstract class WeakReferenceHandler<T> extends Handler{
	
	private final WeakReference<T> weakRef;
	
	public WeakReferenceHandler(T ref){
		this.weakRef = new WeakReference<T>(ref);
	}
	
	/**
	 * 解除引用
	 */
	public void clearRef(){
		weakRef.clear();
	}
	
	/**
	 * 派生类不要再重写此方法了，改用handleMessage(T, Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		T ref = weakRef.get();
		if(ref != null){
			handleMessage(ref, msg);
		}
	}
	
	/**
	 * 派生的Handler来实现
	 * @param ref 属主 （外部类），保证不为NULL
	 * @param msg 消息
	 */
	protected abstract void handleMessage(T ref, Message msg);

}
