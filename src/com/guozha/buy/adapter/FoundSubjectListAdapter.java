package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.found.FoundSubject;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.DimenUtil;

/**
 * 发现-主题 适配器
 * @author PeggyTong
 *
 */
public class FoundSubjectListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private int mScreenWidth;
	private int mMargin;
	private int mBgColor;
	private List<FoundSubject> mSubjectItems;
	private BitmapCache mBitmapCache;
	
	public FoundSubjectListAdapter(Context context, List<FoundSubject> subjectItems, BitmapCache bitmapCache){
		mSubjectItems = subjectItems;
		mBitmapCache = bitmapCache;
		mInflater = LayoutInflater.from(context);
		mScreenWidth = DimenUtil.getScreenWidth(context);
		mMargin = DimenUtil.dp2px(context, 3);
		mBgColor = context.getResources().getColor(R.color.color_app_base_6);
	}

	@Override
	public int getCount() {
		if(mSubjectItems == null) return 0;
		return mSubjectItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mSubjectItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_fragment_found_subject_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.subject_item_image);
			holder.tag = (TextView) convertView.findViewById(R.id.subject_item_tag);
			holder.title = (TextView) convertView.findViewById(R.id.subject_item_title);
			FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, mScreenWidth / 2);
			param.setMargins(mMargin, 0, mMargin, 0);
			holder.image.setLayoutParams(param);
			convertView.setTag(holder);
			convertView.setBackgroundColor(mBgColor);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		FoundSubject foundSubject = mSubjectItems.get(position);
		if("1".equals(foundSubject.getOldFlag())){
			holder.tag.setBackgroundResource(R.drawable.tag_activities_finished);
		}else{
			holder.tag.setBackgroundResource(R.drawable.tag_activities_current);
		}
		holder.image.setImageResource(R.drawable.default_icon_large);
		mBitmapCache.loadBitmaps(holder.image, foundSubject.getSubjectImg());
		
		holder.tag.setText(foundSubject.getSubjectType());
		holder.title.setText(foundSubject.getSubjectName());
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView image;
		private TextView tag;
		private TextView title;
	}
}
