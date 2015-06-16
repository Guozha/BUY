package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.found.SubjectDetail;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.util.DimenUtil;

public class FoundSubjectDetailListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<SubjectDetail> mSubjectDetails;
	private Context mContext;
	private BitmapCache mBitmapCache;
	public FoundSubjectDetailListAdapter(Context context, List<SubjectDetail> subjectDetails, BitmapCache bitmapCache){
		mContext = context;
		mBitmapCache = bitmapCache;
		mSubjectDetails = subjectDetails;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(mSubjectDetails == null) return 0;
		return mSubjectDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return mSubjectDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_subject_detail_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.subject_detail_item_title);
			holder.content = (TextView) convertView.findViewById(R.id.subject_detail_item_content);
			holder.list = (ListView) convertView.findViewById(R.id.subject_detail_item_list);
			holder.price = (TextView) convertView.findViewById(R.id.subject_detail_item_price);
			holder.detailBtn = (Button) convertView.findViewById(R.id.subject_detail_item_detail_btn);
			holder.cartBtn = (Button) convertView.findViewById(R.id.subject_detail_item_cart_btn);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		SubjectDetail subjectDetail = mSubjectDetails.get(position);
		holder.title.setText(subjectDetail.getItemName());
		holder.content.setText(subjectDetail.getItemDesc());
		FoundSubjectDetailItemListAdapter itemListAdapter = 
				new FoundSubjectDetailItemListAdapter(mContext, subjectDetail.getItemDetailList(), mBitmapCache);
		holder.list.setAdapter(itemListAdapter);
		holder.price.setText("价格：19.8");
		return convertView;
	}
	
	static class ViewHolder{
		private TextView title;
		private TextView content;
		private ListView list;
		private TextView price;
		private Button detailBtn;
		private Button cartBtn;
	}

}
