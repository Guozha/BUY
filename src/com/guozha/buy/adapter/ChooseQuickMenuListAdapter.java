package com.guozha.buy.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.guozha.buy.R;
import com.guozha.buy.entry.global.QuickMenu;
import com.guozha.buy.util.LogUtil;

public class ChooseQuickMenuListAdapter extends BaseAdapter{
	
	private List<Integer> mChoosedMenusPosition = new ArrayList<Integer>(); //选择的快捷菜单
	private LayoutInflater mInflater;
	
	private List<QuickMenu> mQuickMenus;
	private List<String> mChoosedId;
	
	public ChooseQuickMenuListAdapter(Context context, 
			List<QuickMenu> quickMenus, List<String> choosedId){
		mInflater = LayoutInflater.from(context);
		mQuickMenus = quickMenus;
		mChoosedId = choosedId;
	}
	
	public List<Integer> getChoosedMenusPosition(){
		return mChoosedMenusPosition;
	}

	@Override
	public int getCount() {
		if(mQuickMenus == null) return 0;
		return mQuickMenus.size();
	}

	@Override
	public Object getItem(int position) {
		return mQuickMenus.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_quick_menu_cell_item, null);
			holder = new ViewHolder();
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.choose_menu_list_cell1);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.checkBox.setTag(position);
		
		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Integer tag = (Integer) buttonView.getTag();
				if(isChecked){
					if(mChoosedMenusPosition.size() >= 5){
						buttonView.setChecked(false);
						return;
					}
					if(!mChoosedMenusPosition.contains(tag)){
						mChoosedMenusPosition.add(tag);
					}
				}else{
					mChoosedMenusPosition.remove(tag);
				}
			}
		});
		
		
		if(mChoosedId.contains(String.valueOf(mQuickMenus.get(position).getMenuId()))){
			holder.checkBox.setChecked(true);
		}
		holder.checkBox.setText(mQuickMenus.get(position).getName());
		return convertView;
	}
	
	static class ViewHolder{
		CheckBox checkBox;
	}

}
