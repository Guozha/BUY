package com.guozha.buy.model;

import java.util.List;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.entry.found.FoundSubjectPage;
import com.guozha.buy.entry.found.MenuFirstType;
import com.guozha.buy.entry.found.SubjectDetail;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.result.FoundModelResult;
import com.guozha.buy.util.LogUtil;

public class FoundModel extends BaseModel{
	
	private FoundModelInterface mInterface;
	
	public FoundModel(){
		mInterface = new FoundModelResult();
	}
	
	public FoundModel(FoundModelInterface foundModelInterface){
		mInterface = foundModelInterface;
	}

	public interface FoundModelInterface{
		
		public void requestFoundSubjectListResult(FoundSubjectPage foundSubjectPage);
		
		public void requestFoundSubjectDetailResult(List<SubjectDetail> subjectDetails);
		
		public void requestFoundMenuTypesResult(List<MenuFirstType> menuFirstTypes);
		
	}
	
	public void requestFoundSubjectList(final Context context, int pageNum){
		RequestParam paramPath = new RequestParam("v31/found/subject/list")
		.setParams("pageNum", pageNum);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					FoundSubjectPage foundSubjectPage = gson.fromJson(response, new TypeToken<FoundSubjectPage>() { }.getType());
					mInterface.requestFoundSubjectListResult(foundSubjectPage);
				}
		});
	}
	
	public void requestFoundSubjectDetail(final Context context, int subjectId){
		RequestParam paramPath = new RequestParam("v31/found/subject/detail")
		.setParams("subjectId", subjectId);
		HttpManager.getInstance(context).volleyRequestByPost(
			paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<SubjectDetail> subjectDetails = gson.fromJson(response, new TypeToken<List<SubjectDetail>>() { }.getType());
					mInterface.requestFoundSubjectDetailResult(subjectDetails);
				}
		});
	}
	
	public void requestFoundMenuTypes(final Context context){
		RequestParam paramPath = new RequestParam("v31/found/menuType/list");
		HttpManager.getInstance(context).volleyRequestByPost(
				paramPath, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				List<MenuFirstType> menuFirstTypes = gson.fromJson(response, new TypeToken<List<MenuFirstType>>() { }.getType());
				mInterface.requestFoundMenuTypesResult(menuFirstTypes);
			}
		});
	}
}
