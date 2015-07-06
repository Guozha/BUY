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

public class ChooseAddressAdapter extends BaseAdapter{
	private List<AddressInfo> mAddressInfos;
	private LayoutInflater mInflater;
	public ChooseAddressAdapter(Context context, List<AddressInfo> addressInfos){
		mInflater = LayoutInflater.from(context);
		mAddressInfos = addressInfos;
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
			convertView = mInflater.inflate(R.layout.list_item_choose_address_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.address_item_name);
			holder.phone = (TextView) convertView.findViewById(R.id.address_item_phone);
			holder.detail = (TextView) convertView.findViewById(R.id.address_item_detail);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		AddressInfo addressInfo = mAddressInfos.get(position);
		holder.name.setText(addressInfo.getReceiveName());
		holder.phone.setText(addressInfo.getMobileNo());
		holder.detail.setText(addressInfo.getCityName() + addressInfo.getCityName() + addressInfo.getBuildingName() + addressInfo.getDetailAddr());
		return convertView;
	}

	static class ViewHolder{
		private TextView name;
		private TextView phone;
		private TextView detail;
	}
}
