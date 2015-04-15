package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mpage.plan.MenuStep;
import com.guozha.buy.global.net.BitmapCache;

/**
 * 菜谱详情的制作过程列表适配器
 * @author PeggyTong
 *
 */
public class DetailStepListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<MenuStep> mMenuStep;
	private BitmapCache mBitmapCahe;
	public DetailStepListAdapter(Context context, List<MenuStep> menuStep, BitmapCache bitmapCache){
		mInflater = LayoutInflater.from(context);
		mMenuStep = menuStep;
		mBitmapCahe = bitmapCache;
	}

	@Override
	public int getCount() {
		if(mMenuStep == null) return 0;
		return mMenuStep.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuStep.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.detail_step_list_item_cell, null);
		}
		TextView stepNum = (TextView) convertView.findViewById(R.id.step_num);
		TextView stepDescrip = (TextView) convertView.findViewById(R.id.step_descrip);
		ImageView stepImg = (ImageView) convertView.findViewById(R.id.step_img);
		
		stepNum.setText((position + 1) + "、");
		MenuStep menuStep = mMenuStep.get(position);
		stepDescrip.setText(menuStep.getStepDesc());
		mBitmapCahe.loadBitmaps(stepImg, menuStep.getStepImg());
		return convertView;
	}

}
