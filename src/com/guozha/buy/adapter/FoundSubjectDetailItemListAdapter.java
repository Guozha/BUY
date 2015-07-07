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
import com.guozha.buy.entry.found.SubjectDetailItem;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.DimenUtil;
import com.guozha.buy.util.LogUtil;

public class FoundSubjectDetailItemListAdapter extends BaseAdapter{
	
	private List<SubjectDetailItem> mItemDetails;
	private LayoutInflater mInflater;
	private BitmapCache mBitmapCache;
	private LinearLayout.LayoutParams mLayoutParams;
	public FoundSubjectDetailItemListAdapter(Context context, 
			List<SubjectDetailItem> itemDetailList, BitmapCache bitmapCache){
		mBitmapCache = bitmapCache;
		mInflater = LayoutInflater.from(context);
		mItemDetails = itemDetailList;
		int screenWidth = DimenUtil.getScreenWidth(context);
		int picMargin = DimenUtil.dp2px(context, 5);
		mLayoutParams = 
				new LinearLayout.LayoutParams(screenWidth, screenWidth / 2);
		mLayoutParams.setMargins(0, picMargin, 0, picMargin);
	}

	@Override
	public int getCount() {
		if(mItemDetails == null) return 0;
		return mItemDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_subject_detail_item_detail_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.list_subjecet_detail_item_img);
			holder.text = (TextView) convertView.findViewById(R.id.list_subject_detail_item_descript);
			holder.image.setLayoutParams(mLayoutParams);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		SubjectDetailItem detailItem = mItemDetails.get(position);
		if(detailItem.getItemDetailImg() == null || "".equals(detailItem.getItemDetailImg().trim())){
			holder.image.setVisibility(View.GONE);
		}else{
			holder.image.setVisibility(View.VISIBLE);
			holder.image.setImageResource(R.drawable.default_720_360);
			mBitmapCache.loadBitmaps(holder.image, detailItem.getItemDetailImg());
		}
		if(detailItem.getItemDetailDesc() == null){
			holder.text.setVisibility(View.GONE);
		}else{
			holder.text.setVisibility(View.VISIBLE);
			holder.text.setText(detailItem.getItemDetailDesc());
		}
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView image;
		private TextView text;
	}
}
