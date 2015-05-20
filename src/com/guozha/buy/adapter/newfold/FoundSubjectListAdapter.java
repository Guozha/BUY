package com.guozha.buy.adapter.newfold;

import com.guozha.buy.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 发现-主题 适配器
 * @author PeggyTong
 *
 */
public class FoundSubjectListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	
	public FoundSubjectListAdapter(Context context){
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//mInflater.inflate(R.layout., root)
		return null;
	}

}
