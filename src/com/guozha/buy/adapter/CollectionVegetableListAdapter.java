package com.guozha.buy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.guozha.buy.R;
import com.guozha.buy.dialog.CustomDialog;

public class CollectionVegetableListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	
	private DeleteClickListener mDeletedClickListener;
	
	private Context mContext;
	
	public CollectionVegetableListAdapter(Context context){
		mContext = context;
		mDeletedClickListener = new DeleteClickListener();
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 6;
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
			convertView = mInflater.inflate(R.layout.list_fragment_collection_vegetable_item_cell, null);
			holder = new ViewHolder();
			holder.deleteButton = (ImageView) convertView.findViewById(R.id.delete_button);
			holder.deleteButton.setOnClickListener(mDeletedClickListener);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView deleteButton;
	}
	
	
	/**
	 * 删除按钮监听
	 * @author PeggyTong
	 *
	 */
	class DeleteClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			CustomDialog dialog = new CustomDialog(mContext, R.layout.dialog_delete_notify);
			dialog.setDismissButtonId(R.id.cancel_button);
		}
	}

}
