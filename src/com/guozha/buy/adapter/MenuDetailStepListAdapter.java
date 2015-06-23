package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.found.menu.MenuStep;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.DimenUtil;

//发现 - 菜谱详情 - 步骤 适配器
public class MenuDetailStepListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private int mScreenWidth;
	private LinearLayout.LayoutParams mParams;
	private List<MenuStep> mMenuSteps;
	private BitmapCache mBitmapCache;
	public MenuDetailStepListAdapter(Context context, List<MenuStep> menuSteps, BitmapCache bitmapCache){
		mInflater = LayoutInflater.from(context);
		mMenuSteps = menuSteps;
		mBitmapCache = bitmapCache;
		mScreenWidth = DimenUtil.getScreenWidth(context);
		mParams = new LinearLayout.LayoutParams(mScreenWidth, mScreenWidth / 2);
	}

	@Override
	public int getCount() {
		if(mMenuSteps == null) return 0;
		return mMenuSteps.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuSteps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_menu_detail_step_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.menu_detail_step_img);
			holder.text = (TextView) convertView.findViewById(R.id.menu_detail_step_text);
			holder.image.setLayoutParams(mParams);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		MenuStep menuStep = mMenuSteps.get(position);
		holder.image.setImageResource(R.drawable.default_icon_large);
		mBitmapCache.loadBitmaps(holder.image, menuStep.getStepImg());
		holder.text.setText(menuStep.getStepDesc());
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView image;
		private TextView text;
	}

}
