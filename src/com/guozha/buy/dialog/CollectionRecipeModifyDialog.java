package com.guozha.buy.dialog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.adapter.CollectionRecipeModifyListAdapter;
import com.guozha.buy.entry.mine.collection.CollectionDir;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.ToastUtil;

/**
 * 菜谱收藏更改分类对话框
 * @author PeggyTong
 *
 */
public class CollectionRecipeModifyDialog extends Activity{
	
	private static final int REQUEST_CODE = 0x0001;
	private static final int HAND_DATA_COMPLETED = 0x0001; //数据请求完成

	private ListView mRecipeModifyList;
	private List<CollectionDir> mCollectionDir = new ArrayList<CollectionDir>();
	private CollectionRecipeModifyListAdapter mCollectionRecipeModifyAdapter;
	private int mMenuId;	//要移动的菜谱ID
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_DATA_COMPLETED:
				updateView();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_collection_recipe_modify);
		//让Dialog全屏
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mMenuId = bundle.getInt("menuId");
			}
		}
		
		initView();
		requestCollectionRecipeData();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		//点击空白区域
		findViewById(R.id.select_weight_free_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CollectionRecipeModifyDialog.this.finish();
			}
		});
		
		//点击新增分类
		findViewById(R.id.create_new_class).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CollectionRecipeModifyDialog.this, CreateFolderDialog.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		
		mRecipeModifyList = (ListView) findViewById(R.id.collection_recipe_modify_list);
		mRecipeModifyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				requestModifyCollectionFolder(mCollectionDir.get(position).getMyDirId());
			}
		});
	}
	
	/**
	 * 请求更改收藏夹位置
	 * @param dirId
	 */
	private void requestModifyCollectionFolder(int dirId){
		String token = ConfigManager.getInstance().getUserToken(CollectionRecipeModifyDialog.this);
		if(token == null) return;
		RequestParam paramPath = new RequestParam("account/myfavo/adjustMenuFavo")
		.setParams("token", token)
		.setParams("myMenuId", mMenuId)
		.setParams("myDirId", dirId);
		HttpManager.getInstance(CollectionRecipeModifyDialog.this).volleyJsonRequestByPost(
			HttpManager.URL + paramPath, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						String returnCode = response.getString("returnCode");
						if("1".equals(returnCode)){
							CollectionRecipeModifyDialog.this.finish();
						}else{
							ToastUtil.showToast(CollectionRecipeModifyDialog.this, response.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	/**
	 * 更新列表
	 */
	private void updateView(){
		if(mCollectionRecipeModifyAdapter == null){
			mCollectionRecipeModifyAdapter = new CollectionRecipeModifyListAdapter(this, mCollectionDir);
			mRecipeModifyList.setAdapter(mCollectionRecipeModifyAdapter);
		}else{
			mCollectionRecipeModifyAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE){
			requestCollectionRecipeData();
		}
	}
	
	/**
	 * 请求收藏菜谱数据
	 */
	private void requestCollectionRecipeData(){
		int userId = ConfigManager.getInstance().getUserId();
		int addressId = ConfigManager.getInstance().getChoosedAddressId();
		RequestParam paramPath = new RequestParam("account/myfavo/listMenuFavo")
		.setParams("userId", userId)
		.setParams("addressId", addressId);
		HttpManager.getInstance(CollectionRecipeModifyDialog.this).volleyRequestByPost(
			HttpManager.URL + paramPath, new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
					List<CollectionDir> collectionDir = gson.fromJson(response, new TypeToken<List<CollectionDir>>() { }.getType());
					mCollectionDir.clear();
					mCollectionDir.addAll(collectionDir);
					handler.sendEmptyMessage(HAND_DATA_COMPLETED);
				}
			});
	}
}
