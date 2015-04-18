package com.guozha.buy.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.dialog.CollectionRecipeModifyDialog;
import com.guozha.buy.dialog.CustomDialog;
import com.guozha.buy.entry.mine.collection.CollectionDir;
import com.guozha.buy.entry.mine.collection.Material;
import com.guozha.buy.entry.mine.collection.RecipeListItem;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.guozha.buy.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

/**
 * 菜谱收藏适配器
 * @author PeggyTong
 *
 */
public class CollectionRecipeExpandAdapter extends AnimatedExpandableListAdapter{
	
	private LayoutInflater mInflater;
	private Context context;
	
	private ModifyClickListener mModifyClickListener;
	private DeleteClickListener mDeleteClickListener;
	private List<CollectionDir> mCollectionDir;
	private BitmapCache mBitmapCache;
	
	public CollectionRecipeExpandAdapter(Context context, List<CollectionDir> collectionDir, BitmapCache bitmapCache){
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mCollectionDir = collectionDir;
		mModifyClickListener = new ModifyClickListener();
		mDeleteClickListener = new DeleteClickListener();
		mBitmapCache = bitmapCache;
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
			holder.recipeImg = (ImageView) convertView.findViewById(R.id.recipe_collection_img);
			holder.recipeName = (TextView) convertView.findViewById(R.id.recipe_collection_name);
			holder.recipeDescript = (TextView) convertView.findViewById(R.id.recipe_collection_descript);
			holder.modifyButton = (ImageView)
					convertView.findViewById(R.id.collection_recipe_modify_button);
			holder.deleteButton = (ImageView) 
					convertView.findViewById(R.id.collection_recipe_delete_button);
			holder.modifyButton.setOnClickListener(mModifyClickListener);
			holder.deleteButton.setOnClickListener(mDeleteClickListener);
			convertView.setTag(holder);
		}else{
			holder = (ChildViewHolder) convertView.getTag();
		}
		CollectionDir collectionDir = mCollectionDir.get(groupPosition);
		RecipeListItem recipeCollection = collectionDir.getMenuInfoList().get(childPosition);
		holder.recipeImg.setImageResource(R.drawable.default_icon);
		mBitmapCache.loadBitmaps(holder.recipeImg, recipeCollection.getMenuImg());
		holder.recipeName.setText(recipeCollection.getMenuName());
		holder.recipeDescript.setText(getRecipeDescript(recipeCollection.getGoodsList()));
		holder.modifyButton.setTag(recipeCollection.getMyMenuId());
		holder.deleteButton.setTag(recipeCollection.getMyMenuId());
		return convertView;
	}
	
	/**
	 * 获取主料拼接串
	 * @param materials
	 * @return
	 */
	private String getRecipeDescript(List<Material> materials){
		StringBuffer buffer = new StringBuffer("主料:");
		if(materials == null) return buffer.toString();
		for(int i = 0; i < materials.size(); i++){
			Material material = materials.get(i);
			buffer.append(material.getGoodsName());
			buffer.append(UnitConvertUtil.getSwitchedWeight(material.getAmount(), material.getUnit()));
			if(buffer.length() > 30) break;
			buffer.append("、");
		}
		return buffer.toString();
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		if(mCollectionDir == null) return 0;
		CollectionDir collectionDir = mCollectionDir.get(groupPosition);
		List<RecipeListItem> recipeCollections = collectionDir.getMenuInfoList();
		if(recipeCollections == null)return 0;
		return recipeCollections.size();
	}
	
	static class ChildViewHolder{
		private ImageView recipeImg;
		private TextView recipeName;
		private TextView recipeDescript;
		
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
			Intent intent = new Intent(context, CollectionRecipeModifyDialog.class);
			intent.putExtra("menuId", menuId);
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
		public void onClick(View view) {
			final int menuId = (Integer) view.getTag();
			final CustomDialog dialog = new CustomDialog(context, R.layout.dialog_delete_notify);
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
		String token = ConfigManager.getInstance().getUserToken();
		RequestParam paramPath = new RequestParam("account/myfavo/deleteMyMenu")
		.setParams("token", token)
		.setParams("myMenuId", menuId);
		HttpManager.getInstance(context).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							if(mUpdateRecipeListener != null){
								mUpdateRecipeListener.update();
							}
						}else{
							ToastUtil.showToast(context, response.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
			});
	}
	
	private UpdateRecipeListener mUpdateRecipeListener;
	
	public void setOnUpdateRecipeListener(UpdateRecipeListener updateRecipeListener){
		this.mUpdateRecipeListener = updateRecipeListener;
	}
	
	public interface UpdateRecipeListener {
		public void update();
	}
}
