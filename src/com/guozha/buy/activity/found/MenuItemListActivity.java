package com.guozha.buy.activity.found;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.newfold.MenuItemGridAdapter;
import com.guozha.buy.util.DimenUtil;

/**
 * 菜谱列表
 * @author PeggyTong
 *
 */
public class MenuItemListActivity extends BaseActivity{
	
	private GridView mMenuItemGrid;
	private MenuItemGridAdapter mItemGridAdapter;
	
	private int mGridSpace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menulist);
		mGridSpace = DimenUtil.dp2px(this, 10);
		customActionBarStyle("xxx");
		initView();
	}
	
	private void initView(){
		mMenuItemGrid = (GridView) findViewById(R.id.menuitem_grid);
		mMenuItemGrid.setHorizontalSpacing(mGridSpace);
		mMenuItemGrid.setVerticalSpacing(mGridSpace);
		mMenuItemGrid.setPadding(mGridSpace, 0, mGridSpace, 0);
		mItemGridAdapter = new MenuItemGridAdapter(this, mGridSpace);
		mMenuItemGrid.setAdapter(mItemGridAdapter);
		
		mMenuItemGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
}
