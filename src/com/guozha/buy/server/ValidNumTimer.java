package com.guozha.buy.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ValidNumTimer extends Timer{
	
	private Timer mTimer;
	private static ValidNumTimer mValidNumTimer;
	private static int mTimeCount = 0;
	private List<ValidNumTimer.TimerObserver> observers = 
			new ArrayList<ValidNumTimer.TimerObserver>();
	
	public static ValidNumTimer getInstance(){
		if(mValidNumTimer == null){
			mValidNumTimer = new ValidNumTimer();
		}
		return mValidNumTimer;
	}
	
	public void startTimer(){
		if(mTimer == null){
			mTimer = new Timer();
			mTimeCount = 60;
			mTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					mTimeCount--;
					for(TimerObserver observer : observers){
						observer.timeChanged(mTimeCount);
					}
					if(mTimeCount < 0){ //计时完成
						mTimeCount = 0;
						mTimer.cancel();
						mTimer = null;  //释放
					}
				}
			}, 0, 1000);
		}
	}
	
	public int getTimerCount(){
		return mTimeCount;
	}
	
	public void registObserver(TimerObserver observer){
		observers.add(observer);
	}
	
	public void cancelObserver(TimerObserver observer){
		observers.remove(observer);
	}
	
	/**
	 * 时间的观察者
	 * @author PeggyTong
	 *
	 */
	public interface TimerObserver{
		public void timeChanged(int time);
	}
}
