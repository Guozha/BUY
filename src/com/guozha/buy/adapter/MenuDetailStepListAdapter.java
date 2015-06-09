package com.guozha.buy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;

//发现 - 菜谱详情 - 步骤 适配器
public class MenuDetailStepListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private int mScreenWidth;
	private LinearLayout.LayoutParams mParams;
	public MenuDetailStepListAdapter(Context context){
		mInflater = LayoutInflater.from(context);
		mScreenWidth = DimenUtil.getScreenWidth(context);
		mParams = new LinearLayout.LayoutParams(mScreenWidth, mScreenWidth / 2);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
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
			convertView = mInflater.inflate(R.layout.list_menu_detail_step_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.menu_detail_step_img);
			holder.text = (TextView) convertView.findViewById(R.id.menu_detail_step_text);
			holder.image.setLayoutParams(mParams);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.image.setImageResource(R.drawable.temp_subject_item_imag);
		holder.text.setText("软化的60克黄油加20g糖尿发大幅度发D大调的典范对方答复");
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView image;
		private TextView text;
	}

}
