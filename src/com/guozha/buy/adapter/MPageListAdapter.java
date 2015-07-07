package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mpage.BestMenuItem;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.DimenUtil;

/**
 * 主界面列表适配器
 * @author PeggyTong
 *
 */
public class MPageListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private int mScreenWidth;
	private Resources mResources;
	private List<BestMenuItem> mMenuItems;
	private BitmapCache mBitmapCache;
	public MPageListAdapter(Context context, List<BestMenuItem> menuItems, BitmapCache bitmapCache){
		mMenuItems = menuItems;
		mInflater = LayoutInflater.from(context);
		//TODO 获取屏幕宽度可以放到ConfigManager中
		mScreenWidth = DimenUtil.getScreenWidth(context);
		mResources = context.getResources();
		mBitmapCache = bitmapCache;
	}

	@Override
	public int getCount() {
		if(mMenuItems == null) return 0;
		return mMenuItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.adapter_list_mpage_item, null);
			holder = new ViewHolder();
			holder.leftImage= (ImageView) convertView.findViewById(R.id.mpage_item_left);
			holder.centerView = convertView.findViewById(R.id.mpage_item_center);
			holder.rightImage = (ImageView) convertView.findViewById(R.id.mpage_item_right);
			holder.centerIcon = (ImageView) convertView.findViewById(R.id.mpage_item_center_image);
			holder.centerTitle = (TextView) convertView.findViewById(R.id.mpage_item_center_title);
			holder.centerDescript = (TextView) convertView.findViewById(R.id.mpage_item_center_descript);
			LayoutParams params = new LayoutParams(mScreenWidth / 2, mScreenWidth / 2);
			holder.centerView.setLayoutParams(params);
			holder.leftImage.setLayoutParams(params);
			holder.rightImage.setLayoutParams(params);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.leftImage.setImageResource(R.drawable.default_360_360);
		holder.rightImage.setImageResource(R.drawable.default_360_360);
		ImageView imageView;
		if(position % 2 == 0){
			holder.leftImage.setVisibility(View.VISIBLE);
			holder.rightImage.setVisibility(View.GONE);
			imageView = holder.leftImage;
		}else{
			imageView = holder.rightImage;
			holder.rightImage.setVisibility(View.VISIBLE);
			holder.leftImage.setVisibility(View.GONE);
		}
		BestMenuItem menuItem = mMenuItems.get(position);
		mBitmapCache.loadBitmaps(imageView, menuItem.getMenuImg());
		holder.centerView.setBackgroundColor(menuItem.getBgColor());
		holder.centerIcon.setImageResource(R.drawable.icon_drink_color);
		holder.centerIcon.setImageResource(R.drawable.icon_default);
		mBitmapCache.loadBitmaps(holder.centerIcon, menuItem.getPickImg());
		holder.centerTitle.setText(menuItem.getMenuName());
		holder.centerTitle.setTextColor(menuItem.getFontColor());
		holder.centerDescript.setText(menuItem.getPickDesc());
		holder.centerDescript.setTextColor(menuItem.getFontColor());
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView leftImage;
		private View centerView;
		private ImageView rightImage;
		
		private ImageView centerIcon;
		private TextView centerTitle;
		private TextView centerDescript;
	}

}
