package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.found.FoundMenu;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.DimenUtil;

public class MenuItemGridAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private int mGridSpace;
	private int mGridWidth;
	private int mGridHeight;
	private int mBgColor;
	private List<FoundMenu> mFoundMenus;
	private BitmapCache mBitmapCache;
	public MenuItemGridAdapter(Context context, int gridSpace, List<FoundMenu> foundMenus, BitmapCache bitmapCache){
		mInflater = LayoutInflater.from(context);
		mFoundMenus = foundMenus;
		mBitmapCache = bitmapCache;
		int screenWidth = DimenUtil.getScreenWidth(context);
		mGridSpace = gridSpace;
		mGridWidth = mGridHeight = screenWidth / 2 - gridSpace;
		mBgColor = context.getResources().getColor(R.color.color_app_base_23);
	}

	@Override
	public int getCount() {
		if(mFoundMenus == null) return 0;
		return mFoundMenus.size();
	}

	@Override
	public Object getItem(int position) {
		return mFoundMenus.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

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
		FoundMenu foundMenu = mFoundMenus.get(position);
		holder.icon.setImageResource(R.drawable.default_360_360);
		mBitmapCache.loadBitmaps(holder.icon, foundMenu.getMenuImg());
		holder.name.setText(foundMenu.getMenuName());
		return convertView;
	}
	
	static class ViewHolder{
		private View frame;
		private ImageView icon;
		private TextView name;
	}

}
