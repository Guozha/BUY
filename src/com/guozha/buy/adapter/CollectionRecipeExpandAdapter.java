package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.dialog.CollectionRecipeModifyDialog;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.entry.mine.collection.CollectionDir;
import com.guozha.buy.entry.mine.collection.CollectionMenu;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.CollectionModel;
import com.guozha.buy.model.result.CollectionModelResult;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

/**
 * 菜谱收藏适配器
 * @author PeggyTong
 *
 */
public class CollectionRecipeExpandAdapter extends AnimatedExpandableListAdapter{
	
	private LayoutInflater mInflater;
	
	private ModifyClickListener mModifyClickListener;
	private DeleteClickListener mDeleteClickListener;
	private List<CollectionDir> mCollectionDir;
	private BitmapCache mBitmapCache;
	private Context mContext;
	private CollectionModel mCollectionModel;
	
	public CollectionRecipeExpandAdapter(Context context, List<CollectionDir> collectionDir, BitmapCache bitmapCache){
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		mCollectionDir = collectionDir;
		mModifyClickListener = new ModifyClickListener();
		mDeleteClickListener = new DeleteClickListener();
		mBitmapCache = bitmapCache;
		mCollectionModel = new CollectionModel(new MyCollectionModelResult());
	}

	@Override
	public int getGroupCount() {
		if(mCollectionDir == null) return 0;
		return mCollectionDir.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mCollectionDir.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mCollectionDir.get(groupPosition).getMenuInfoList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_fragment_collection_recipe_cell_group, null);
		}
		TextView recipeDirName = (TextView) convertView.findViewById(R.id.collection_recipe_dirname);
		recipeDirName.setText(mCollectionDir.get(groupPosition).getDirName());
		if(isExpanded){
			//设置图片
		}else{
			
		}
		recipeDirName.setTag(mCollectionDir.get(groupPosition).getMyDirId());
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
		ChildViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_fragment_collection_recipe_cell_child, null);
			holder = new ChildViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.collection_cooke_book_img);
			holder.name = (TextView) convertView.findViewById(R.id.collection_cooke_book_name);
			holder.cookeWay = (TextView) convertView.findViewById(R.id.cooke_way);
			holder.cookeTime = (TextView) convertView.findViewById(R.id.cooke_time);
			holder.cookeCalorie = (TextView) convertView.findViewById(R.id.cooke_calorie);
			holder.modifyButton = (ImageView) convertView.findViewById(R.id.collection_recipe_modify_button);
			holder.deleteButton = (ImageView) convertView.findViewById(R.id.collection_recipe_delete_button);
			holder.modifyButton.setOnClickListener(mModifyClickListener);
			holder.deleteButton.setOnClickListener(mDeleteClickListener);
			convertView.setTag(holder);
		}else{
			holder = (ChildViewHolder) convertView.getTag();
		}
		CollectionMenu relationRecipe = mCollectionDir.get(groupPosition).getMenuInfoList().get(childPosition);
		holder.image.setImageResource(R.drawable.default_icon);
		mBitmapCache.loadBitmaps(holder.image, relationRecipe.getMenuImg());
		holder.name.setText(relationRecipe.getMenuName());
		holder.cookeWay.setText(relationRecipe.getCookieWay());
		holder.cookeTime.setText(relationRecipe.getCookieTime() + "min");
		holder.cookeCalorie.setText(relationRecipe.getCalories() + "大卡/100g");
		holder.modifyButton.setTag(relationRecipe.getMyMenuId());
		holder.deleteButton.setTag(relationRecipe.getMyMenuId());
		return convertView;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		if(mCollectionDir == null) return 0;
		CollectionDir collectionDir = mCollectionDir.get(groupPosition);
		List<CollectionMenu> relationRecipe = collectionDir.getMenuInfoList();
		if(relationRecipe == null)return 0;
		return relationRecipe.size();
	}
	
	static class ChildViewHolder{
    	private ImageView image;
    	private TextView name;
    	private TextView cookeWay;
    	private TextView cookeTime;
    	private TextView cookeCalorie;
    	
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
		public void onClick(View view) {
			int menuId = (Integer) view.getTag();
			Intent intent = new Intent(mContext, CollectionRecipeModifyDialog.class);
			intent.putExtra("menuId", menuId);
			mContext.startActivity(intent);
		}
	}
	
	/**
	 * 删除按钮监听
	 * @author PeggyTong
	 *
	 */
	class DeleteClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			final int menuId = (Integer) view.getTag();
			final CustomDialog dialog = new CustomDialog(mContext, R.layout.dialog_delete_notify);
			dialog.setDismissButtonId(R.id.cancel_button);
			dialog.getViewById(R.id.agree_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					requestDeleteRecipeItem(menuId);
				}
			});
		}
	}
	
	/**
	 * 请求删除收藏菜谱
	 */
	private void requestDeleteRecipeItem(int menuId){
		String token = ConfigManager.getInstance().getUserToken(mContext);
		if(token == null) return;
		mCollectionModel.requestDeleMenuCollectItem(mContext, token, menuId);
	}
	
	private UpdateRecipeListener mUpdateRecipeListener;
	
	public void setOnUpdateRecipeListener(UpdateRecipeListener updateRecipeListener){
		this.mUpdateRecipeListener = updateRecipeListener;
	}
	
	public interface UpdateRecipeListener {
		public void update();
	}
	
	class MyCollectionModelResult extends CollectionModelResult{
		@Override
		public void requestDeleMenuCollectItemResult(String returnCode,
				String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				if(mUpdateRecipeListener != null){
					mUpdateRecipeListener.update();
				}
			}else{
				ToastUtil.showToast(mContext, msg);
			}
		}
	}
}
