package com.guozha.buy.adapter.newfold;

import java.util.Random;

import android.content.Context;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.guozha.buy.R;
import com.guozha.buy.util.DimenUtil;

/**
 * 主界面列表适配器
 * @author PeggyTong
 *
 */
public class MPageListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private int mScreenWidth;
	
	public MPageListAdapter(Context context){
		mInflater = LayoutInflater.from(context);
		//TODO 获取屏幕宽度可以放到ConfigManager中
		mScreenWidth = DimenUtil.getScreenWidthAndHeight(context)[0];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 50;
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

	Random random = new Random();
	int [] imags = new int[]{R.drawable.temp_mpage_img1, R.drawable.temp_mpage_img2, R.drawable.temp_mpage_img3};
	int [] tags = new int[]{R.drawable.temp_mpage_tag1, R.drawable.temp_mpage_tag2, R.drawable.temp_mpage_tag3};
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.adapter_list_mpage_item, null);
			holder = new ViewHolder();
			holder.leftImage= (ImageView) convertView.findViewById(R.id.mpage_item_left);
			holder.centerView = convertView.findViewById(R.id.mpage_item_center);
			holder.rightImage = (ImageView) convertView.findViewById(R.id.mpage_item_right);
			LayoutParams params = new LayoutParams(mScreenWidth / 2, mScreenWidth / 2);
			holder.centerView.setLayoutParams(params);
			holder.leftImage.setLayoutParams(params);
			holder.rightImage.setLayoutParams(params);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
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
		
		
		int rand1 = random.nextInt(3);
		int rand2 = random.nextInt(3);
		
		
		
		imageView.setImageResource(imags[rand1]);
		holder.centerView.setBackgroundResource(tags[rand2]);
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView leftImage;
		private View centerView;
		private ImageView rightImage;
	}

}
