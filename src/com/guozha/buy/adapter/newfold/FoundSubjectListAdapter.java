package com.guozha.buy.adapter.newfold;

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
	
	public FoundSubjectListAdapter(Context context){
		mInflater = LayoutInflater.from(context);
		mScreenWidth = DimenUtil.getScreenWidth(context);
		mMargin = DimenUtil.dp2px(context, 3);
		mBgColor = context.getResources().getColor(R.color.color_app_base_6);
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
		holder.image.setImageResource(R.drawable.temp_subject_item_imag);
		holder.tag.setBackgroundResource(R.drawable.temp_subject_item_tag);
		holder.tag.setText("活动");
		holder.title.setText("[端午]远归的自己，回家给爸妈做饭");
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView image;
		private TextView tag;
		private TextView title;
	}

}
