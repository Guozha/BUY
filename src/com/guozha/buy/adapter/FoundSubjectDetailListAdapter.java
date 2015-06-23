package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.dialog.WeightSelectDialog;
import com.guozha.buy.controller.found.MenuDetailActivity;
import com.guozha.buy.controller.market.VegetableDetailActivity;
import com.guozha.buy.entry.found.SubjectDetail;
import com.guozha.buy.global.net.BitmapCache;

public class FoundSubjectDetailListAdapter extends BaseAdapter implements OnClickListener{
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
			holder.detailBtn.setOnClickListener(this);
			holder.cartBtn.setOnClickListener(this);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		SubjectDetail subjectDetail = mSubjectDetails.get(position);
		holder.detailBtn.setTag(position);
		holder.cartBtn.setTag(position);
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

	@Override
	public void onClick(View view) {
		int position = (Integer) view.getTag();
		SubjectDetail subjectDetail = mSubjectDetails.get(position);
		if(subjectDetail == null) return;
		Intent intent;
		switch (view.getId()) {
		case R.id.subject_detail_item_detail_btn:
			if("01".equals(subjectDetail.getProductType())){
				intent = new Intent(mContext, MenuDetailActivity.class);
				intent.putExtra("menuId", subjectDetail.getGoodsOrMenuId());
			}else{
				intent = new Intent(mContext, VegetableDetailActivity.class);
				intent.putExtra("goodsId", subjectDetail.getGoodsOrMenuId());
			}
			break;
		case R.id.subject_detail_item_cart_btn:
			if("01".equals(subjectDetail.getProductType())){ //菜谱
				//TODO 这块应该让3.6接口可以直接加入菜谱（全选食材）
				intent = new Intent(mContext, MenuDetailActivity.class);
				intent.putExtra("menuId", subjectDetail.getGoodsOrMenuId());
			}else{
				intent = new Intent(mContext, WeightSelectDialog.class);
				intent.putExtra("goodsId", subjectDetail.getGoodsOrMenuId());
				intent.putExtra("unitPrice", subjectDetail.getUnitPrice());
				intent.putExtra("unit", subjectDetail.getUnit());
			}
			mContext.startActivity(intent);
			break;
		}
	}

}
