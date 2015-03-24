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
import com.guozha.buy.entry.QuickMenus;
import com.guozha.buy.global.CustomApplication;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;

/**
 * 快捷菜单适配器
 * @author PeggyTong
 *
 */
public class ChooseMenuListAdapter extends BaseAdapter implements OnCheckedChangeListener{
	
	private List<String> mChoosedMenusID = new ArrayList<String>(); //选择的快捷菜单
	private LayoutInflater mInflater;
	private List<QuickMenus[]> mQuickMenus;
	
	public ChooseMenuListAdapter(Context context, List<QuickMenus[]> quickMenus){
		mQuickMenus = quickMenus;
		mInflater = LayoutInflater.from(context);
	}
	
	public List<String> getChoosedMenusID(){
		return mChoosedMenusID;
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
			convertView = mInflater.inflate(R.layout.list_item_choose_menu_cell, null);
			holder = new ViewHolder();
			holder.checkBox1 = (CheckBox) convertView.findViewById(R.id.choose_menu_list_cell1);
			holder.checkBox2 = (CheckBox) convertView.findViewById(R.id.choose_menu_lis_cell2);
			holder.checkBox3 = (CheckBox) convertView.findViewById(R.id.choose_menu_list_cell3);
			
			holder.checkBox1.setOnCheckedChangeListener(this);
			holder.checkBox2.setOnCheckedChangeListener(this);
			holder.checkBox3.setOnCheckedChangeListener(this);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		QuickMenus[] quickMenus = mQuickMenus.get(position);
		if(quickMenus.length == 3){
			holder.checkBox1.setTag(quickMenus[0].getId());
			holder.checkBox2.setTag(quickMenus[1].getId());
			holder.checkBox3.setTag(quickMenus[2].getId());
			
			holder.checkBox1.setVisibility(View.VISIBLE);
			holder.checkBox2.setVisibility(View.VISIBLE);
			holder.checkBox3.setVisibility(View.VISIBLE);
			
			holder.checkBox1.setText(quickMenus[0].getShowName());
			holder.checkBox2.setText(quickMenus[1].getShowName());
			holder.checkBox3.setText(quickMenus[2].getShowName());
			
			holder.checkBox1.setChecked(quickMenus[0].isChoosed());
			holder.checkBox2.setChecked(quickMenus[1].isChoosed());
			holder.checkBox3.setChecked(quickMenus[2].isChoosed());
		}else if(quickMenus.length == 2){
			holder.checkBox1.setTag(quickMenus[0].getId());
			holder.checkBox2.setTag(quickMenus[1].getId());
			
			holder.checkBox1.setText(quickMenus[0].getShowName());
			holder.checkBox2.setText(quickMenus[1].getShowName());
			
			holder.checkBox1.setChecked(quickMenus[0].isChoosed());
			holder.checkBox2.setChecked(quickMenus[1].isChoosed());

			holder.checkBox3.setVisibility(View.INVISIBLE);
		}else{
			holder.checkBox1.setTag(quickMenus[1].getId());
			
			holder.checkBox1.setText(quickMenus[0].getShowName());
			holder.checkBox1.setChecked(quickMenus[0].isChoosed());
			
			holder.checkBox2.setVisibility(View.INVISIBLE);
			holder.checkBox3.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder{
		private CheckBox checkBox1;
		private CheckBox checkBox2;
		private CheckBox checkBox3;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String tag = String.valueOf(buttonView.getTag());
		if(isChecked){
			LogUtil.e("isChecked_checkid = " + buttonView.getTag());
			if(mChoosedMenusID.size() >= 5){
				buttonView.setChecked(false);
				return;
			}
			if(!mChoosedMenusID.contains(tag)){
				mChoosedMenusID.add(tag);
			}
		}else{
			mChoosedMenusID.remove(tag);
		}
	}

}
