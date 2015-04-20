package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.mine.address.AddressInfo;

/**
 * 选择地址列表
 * @author PeggyTong
 *
 */
public class ChooseAddressListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<AddressInfo> mAddressInfos;
	
	public ChooseAddressListAdapter(Context context, List<AddressInfo> addressInfo){
		mInflater = LayoutInflater.from(context);
		mAddressInfos = addressInfo;
	}

	@Override
	public int getCount() {
		if(mAddressInfos == null) return 0;
		return mAddressInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mAddressInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_choose_address_cell, null);
		}
		TextView nameText = (TextView) convertView.findViewById(R.id.address_receivename);
		TextView mobileText = (TextView) convertView.findViewById(R.id.address_mobileno);
		TextView detailText = (TextView) convertView.findViewById(R.id.address_detail);
		TextView defaultText = (TextView) convertView.findViewById(R.id.address_default);
		AddressInfo addressInfo = mAddressInfos.get(position);
		nameText.setText(addressInfo.getReceiveName());
		mobileText.setText(addressInfo.getMobileNo());
		detailText.setText(addressInfo.getBuildingName() + addressInfo.getDetailAddr());
		if("1".equals(addressInfo.getDefaultFlag())){
			defaultText.setVisibility(View.VISIBLE);
		}else{
			defaultText.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

}
