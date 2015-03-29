package com.guozha.buy.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.cart.CartBaseItem;
import com.guozha.buy.entry.cart.CartBaseItem.CartItemType;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartCookMaterial;
import com.guozha.buy.util.LogUtil;

/**
 * 购物车列表适配器
 * @author PeggyTong
 *
 */
public class CartItemListAdapter extends BaseExpandableListAdapter{
	
	private LayoutInflater mInflater;
	private int mTitleIndex;
	private Resources mResource;
	private List<CartBaseItem> mCartItems;
	
	public CartItemListAdapter(Context context, List<CartBaseItem> cartItems){
		mResource = context.getResources();
		mInflater = LayoutInflater.from(context);
		mCartItems = cartItems;
	}

	@Override
	public int getGroupCount() {
		return mCartItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		CartBaseItem cartBaseItem = mCartItems.get(groupPosition);
		if(cartBaseItem.getItemType() == CartItemType.CookBook && groupPosition != 0){
			CartCookItem cookItem = (CartCookItem) cartBaseItem;
			
			return cookItem.getGoodsList().size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mCartItems.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		CartBaseItem cartBaseItem = mCartItems.get(groupPosition);
		if(cartBaseItem.getItemType() == CartItemType.CookBook){
			CartCookItem cookItem = (CartCookItem) cartBaseItem;
			return cookItem.getGoodsList().get(childPosition);
		}else{
			return null;
		}
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
		GroupViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_cell_cart_list, null);
			holder = new GroupViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.cart_list_cell_title);
			holder.minus = (ImageView) convertView.findViewById(R.id.cart_list_cell_minus);
			holder.num = (TextView) convertView.findViewById(R.id.cart_list_cell_num);
			holder.plus = (ImageView) convertView.findViewById(R.id.cart_list_cell_plus);
			holder.price = (TextView) convertView.findViewById(R.id.cart_list_cell_price);
			holder.close = (ImageView) convertView.findViewById(R.id.cart_list_cell_close);
			
			convertView.setTag(holder);
		}else{
			holder = (GroupViewHolder) convertView.getTag();
		}
		
		CartBaseItem baseItem = mCartItems.get(groupPosition);
		
		
		if(groupPosition == 0 || baseItem.getCartId() == -1){
			holder.minus.setVisibility(View.INVISIBLE);
			holder.num.setVisibility(View.INVISIBLE);
			holder.plus.setVisibility(View.INVISIBLE);
			holder.price.setVisibility(View.INVISIBLE);
			holder.close.setVisibility(View.INVISIBLE);
			holder.title.setTextColor(mResource.getColor(R.color.color_app_base_1));
			if(groupPosition == 0){
				holder.title.setText("菜谱");
			}else{
				holder.title.setText("逛菜场");
			}
		}else{
			holder.minus.setVisibility(View.VISIBLE);
			holder.num.setVisibility(View.VISIBLE);
			holder.plus.setVisibility(View.VISIBLE);
			holder.price.setVisibility(View.VISIBLE);
			holder.close.setVisibility(View.VISIBLE);
			holder.title.setTextColor(mResource.getColor(R.color.color_app_base_4));
			holder.title.setText(baseItem.getGoodsName());
		}
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_cell_cart_material_list, null);
			holder = new ChildViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.cart_list_cell_material_title);
			
			convertView.setTag(holder);
		}else{
			holder = (ChildViewHolder) convertView.getTag();
		}
		
		CartBaseItem baseItem = mCartItems.get(groupPosition);
		if(baseItem.getItemType() == CartItemType.CookBook){
			CartCookItem cartCookItem = (CartCookItem) baseItem;
			CartCookMaterial material = cartCookItem.getGoodsList().get(childPosition);
			holder.title.setText(material.getGoodsName() + "  " 
			+ material.getAmount() + material.getUnit());
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	static class GroupViewHolder{
		private TextView title;
		private ImageView minus;
		private TextView num;
		private ImageView plus;
		private TextView price;
		private ImageView close;
	}
	
	static class ChildViewHolder{
		private TextView title;
	}

}
