package com.guozha.buy.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.CollectionRecipeModifyListAdapter;

/**
 * 菜谱收藏更改分类对话框
 * @author PeggyTong
 *
 */
public class CollectionRecipeModifyDialog extends Activity{

	private ListView mRecipeModifyList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_collection_recipe_modify);
		//让Dialog全屏
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
				startActivity(intent);
				CollectionRecipeModifyDialog.this.finish();
			}
		});
		
		mRecipeModifyList = (ListView) findViewById(R.id.collection_recipe_modify_list);
		mRecipeModifyList.setAdapter(new CollectionRecipeModifyListAdapter(this));
	}
}
