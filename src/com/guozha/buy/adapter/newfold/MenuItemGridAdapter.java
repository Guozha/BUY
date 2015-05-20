package com.guozha.buy.adapter.newfold;

import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;

public class MenuItemGridAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private int mGridSpace;
	private int mGridWidth;
	private int mGridHeight;
	private int mBgColor;
	
	public MenuItemGridAdapter(Context context, int gridSpace){
		mInflater = LayoutInflater.from(context);
		int screenWidth = DimenUtil.getScreenWidthAndHeight(context)[0];
		mGridSpace = gridSpace;
		mGridWidth = mGridHeight = screenWidth / 2 - gridSpace;
		mBgColor = context.getResources().getColor(R.color.color_app_base_6);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 20;
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

	Random radom = new Random();
	int[] imags = new int[]{R.drawable.temp_mpage_img1, R.drawable.temp_mpage_img2, R.drawable.temp_mpage_img3};
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_menu_item_grid_item, null);
			holder = new ViewHolder();
			holder.frame = convertView.findViewById(R.id.menuitem_griditem);
			holder.icon = (ImageView) convertView.findViewById(R.id.menuitem_griditem_icon);
			holder.name = (TextView) convertView.findViewById(R.id.menuitem_griditem_name);
			FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(mGridWidth, mGridHeight);
			holder.icon.setLayoutParams(param);
			convertView.setTag(holder);
			convertView.setBackgroundColor(mBgColor);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == 0 || position == 1){
			holder.frame.setPadding(0, mGridSpace, 0, 0);
		}else if(position == getCount() - 1 || position == getCount() - 2){
			holder.frame.setPadding(0, 0, 0, mGridSpace);
		}else{
			holder.frame.setPadding(0, 0, 0, 0);
		}
		
		holder.icon.setImageResource(imags[radom.nextInt(3)]);
		holder.name.setText("蔬菜沙拉");
		return convertView;
	}
	
	static class ViewHolder{
		private View frame;
		private ImageView icon;
		private TextView name;
	}

}
