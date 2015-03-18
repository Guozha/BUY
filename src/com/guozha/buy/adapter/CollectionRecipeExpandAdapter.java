package com.guozha.buy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.guozha.buy.R;
import com.guozha.buy.dialog.CollectionRecipeModifyDialog;
import com.guozha.buy.dialog.CustomDialog;
import com.guozha.buy.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

public class CollectionRecipeExpandAdapter extends AnimatedExpandableListAdapter{
	
	private LayoutInflater mInflater;
	private Context context;
	
	private ModifyClickListener mModifyClickListener;
	private DeleteClickListener mDeleteClickListener;
	
	public CollectionRecipeExpandAdapter(Context context){
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mModifyClickListener = new ModifyClickListener();
		mDeleteClickListener = new DeleteClickListener();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_fragment_collection_recipe_cell_group, null);
		}
		
		if(isExpanded){
			//设置图片
		}else{
			
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		GroupViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_fragment_collection_recipe_cell_child, null);
			holder = new GroupViewHolder();
			holder.modifyButton = (ImageView)
					convertView.findViewById(R.id.collection_recipe_modify_button);
			holder.deleteButton = (ImageView) 
					convertView.findViewById(R.id.collection_recipe_delete_button);
			holder.modifyButton.setOnClickListener(mModifyClickListener);
			holder.deleteButton.setOnClickListener(mDeleteClickListener);
			convertView.setTag(holder);
		}else{
			holder = (GroupViewHolder) convertView.getTag();
		}
		holder.modifyButton.setTag(groupPosition + ":" + childPosition);
		return convertView;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 5;
	}
	
	static class GroupViewHolder{
		private ImageView modifyButton;
		private ImageView deleteButton;
		
	}
	
	/**
	 * 修改按钮监听
	 * @author PeggyTong
	 *
	 */
	class ModifyClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, CollectionRecipeModifyDialog.class);
			context.startActivity(intent);
		}
	}
	
	/**
	 * 删除按钮监听
	 * @author PeggyTong
	 *
	 */
	class DeleteClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			CustomDialog dialog = new CustomDialog(context, R.layout.dialog_delete_notify);
			dialog.setDismissButtonId(R.id.cancel_button);
		}
	}

}
