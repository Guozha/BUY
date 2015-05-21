package com.guozha.buy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;

public class FoundSubjectDetailListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private int mScreenWidth;
	private int mPicMargin;
	public FoundSubjectDetailListAdapter(Context context){
		mInflater = LayoutInflater.from(context);
		mScreenWidth = DimenUtil.getScreenWidth(context);
		mPicMargin = DimenUtil.dp2px(context, 5);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
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
			convertView = mInflater.inflate(R.layout.list_subject_detail_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.subject_detail_item_title);
			holder.content = (TextView) convertView.findViewById(R.id.subject_detail_item_content);
			
			holder.pic1 = (ImageView) convertView.findViewById(R.id.subject_detail_item_pic1);
			holder.pic2 = (ImageView) convertView.findViewById(R.id.subject_detail_item_pic2);
			holder.pic3 = (ImageView) convertView.findViewById(R.id.subject_detail_item_pic3);
			
			holder.price = (TextView) convertView.findViewById(R.id.subject_detail_item_price);
			holder.detailBtn = (Button) convertView.findViewById(R.id.subject_detail_item_detail_btn);
			holder.cartBtn = (Button) convertView.findViewById(R.id.subject_detail_item_cart_btn);
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(mScreenWidth, mScreenWidth / 2);
			params.setMargins(0, mPicMargin, 0, mPicMargin);
			holder.pic1.setLayoutParams(params);
			holder.pic2.setLayoutParams(params);
			holder.pic3.setLayoutParams(params);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText("五芳斋鲜肉粽2只装");
		holder.content.setText("端午节起源于中国，最初是中国人民祛病防疫的节日，吴越之地春秋之前有在农历五月初五以龙舟竞渡形式举行部落图腾祭祀的习俗；后因诗人屈原在这一天死去，便成了中国汉族人民纪念屈原的传统节日；部分地区也有纪念伍子胥、曹娥等说法。");
		holder.pic1.setImageResource(R.drawable.temp_subject_item_imag);
		holder.pic2.setImageResource(R.drawable.temp_subject_item_imag);
		holder.pic3.setImageResource(R.drawable.temp_subject_item_imag);
		holder.price.setText("价格：19.8");
		return convertView;
	}
	
	static class ViewHolder{
		private TextView title;
		private TextView content;
		private ImageView pic1;
		private ImageView pic2;
		private ImageView pic3;
		private TextView price;
		private Button detailBtn;
		private Button cartBtn;
	}

}
