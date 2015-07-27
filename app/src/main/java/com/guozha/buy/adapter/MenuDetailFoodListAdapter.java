package com.guozha.buy.adapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.found.menu.MenuGoods;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 菜谱详情-食材  适配器
 * @author PeggyTong
 *
 */
public class MenuDetailFoodListAdapter extends BaseAdapter implements OnCheckedChangeListener{
	
	private LayoutInflater mInflater;
	private List<MenuGoods> mMenuGoodss;
	private boolean mCanChoose;
	private Set<String> mCheckedId;
	public MenuDetailFoodListAdapter(Context context, boolean canChoose, List<MenuGoods> menuGoods){
		mInflater = LayoutInflater.from(context);
		mMenuGoodss = menuGoods;
		mCanChoose = canChoose;
		mCheckedId = new HashSet<String>();
	}

	@Override
	public int getCount() {
		if(mMenuGoodss == null) return 0;
		return mMenuGoodss.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuGoodss.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public Set<String> getCheckedIds(){
		return mCheckedId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_menu_detail_food_item, null);
		}
		TextView name = (TextView) convertView.findViewById(R.id.menu_detail_list_item_name);
		TextView weight = (TextView) convertView.findViewById(R.id.menu_detail_list_item_weight);
		CheckBox check = (CheckBox) convertView.findViewById(R.id.menu_detail_list_item_check);
		check.setOnCheckedChangeListener(this);
		MenuGoods menuGoods = mMenuGoodss.get(position);
		mCheckedId.add(String.valueOf(menuGoods.getGoodsId()));
		check.setChecked(true);
		if(!mCanChoose){
			check.setEnabled(false);
		}
		check.setTag(position);
		name.setText(menuGoods.getGoodsName());
		weight.setText(menuGoods.getAmount());
		return convertView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int position = (Integer) buttonView.getTag();
		String goodsId = String.valueOf(mMenuGoodss.get(position).getGoodsId());
		if(isChecked){
			mCheckedId.add(goodsId);
		}else{
			mCheckedId.remove(goodsId);
		}
	}
}
