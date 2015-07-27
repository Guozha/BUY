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
 * 我的地址列表适配器
 * @author PeggyTong
 *
 */
public class MyAdressListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<AddressInfo> mAddressInfos;
	private int mCurrentAddressId;
	public MyAdressListAdapter(Context context, List<AddressInfo> addressInfos, int currentAddressId){
		mInflater = LayoutInflater.from(context);
		mAddressInfos = addressInfos;
		mCurrentAddressId = currentAddressId;
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
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_my_address_item_cell, null);
			holder = new ViewHolder();
			holder.receiveName = (TextView) convertView.findViewById(R.id.address_receivename);
			holder.mobileNo = (TextView) convertView.findViewById(R.id.address_mobileno);
			holder.detailAddress = (TextView) convertView.findViewById(R.id.address_detail);
			holder.defaultAdress = (TextView) convertView.findViewById(R.id.address_default);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		AddressInfo addressInfo = mAddressInfos.get(position);
		holder.receiveName.setText(addressInfo.getReceiveName());
		holder.mobileNo.setText(addressInfo.getMobileNo());
		holder.detailAddress.setText(addressInfo.getCountyName() + addressInfo.getBuildingName() + addressInfo.getDetailAddr());
		if(mCurrentAddressId == addressInfo.getAddressId()){
			holder.defaultAdress.setVisibility(View.VISIBLE);
		}else{
			holder.defaultAdress.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	
	static class ViewHolder{
		private TextView receiveName;
		private TextView mobileNo;
		private TextView detailAddress;
		private TextView defaultAdress;
	}

}
