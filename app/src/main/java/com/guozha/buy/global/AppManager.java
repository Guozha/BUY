package com.guozha.buy.global;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class AppManager {
	
	private static AppManager instance;
	
	private List<Activity> activitys;
	
	private AppManager(){
		activitys = new ArrayList<Activity>();
	}
	
	public static AppManager getInstance(){
		if(instance == null) {
			instance = new AppManager();
		}
		return instance;
	}
	
	public void addActivity(Activity activity){
		activitys.add(activity);
	}
	
	public void exit(){
		for(int i = 0; i < activitys.size(); i++){
			activitys.get(i).finish();
		}
	}
}
