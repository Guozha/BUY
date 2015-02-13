package com.guozha.buy.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.guozha.buy.entry.VegetableInfo;

public class VegetableItemAdapter extends BaseAdapter{
	
	private List<VegetableInfo> mVegetables;
	
	public VegetableItemAdapter(List<VegetableInfo> vegetables){
		this.mVegetables = vegetables;
	}

	@Override
	public int getCount() {
		return mVegetables.size();
	}

	@Override
	public Object getItem(int position) {
		return mVegetables.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return null;
	}

}
