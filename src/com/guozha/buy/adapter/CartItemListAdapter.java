package com.guozha.buy.adapter;

import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.entry.cart.CartBaseItem;
import com.guozha.buy.entry.cart.CartBaseItem.CartItemType;
import com.guozha.buy.entry.cart.CartCookItem;
import com.guozha.buy.entry.cart.CartCookMaterial;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.ShopCartModel;
import com.guozha.buy.model.result.ShopCartModelResult;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;

/**
 * 购物车列表适配器
 * @author PeggyTong
 *
 */
public class CartItemListAdapter extends BaseExpandableListAdapter implements OnClickListener, OnLongClickListener{
	
	private LayoutInflater mInflater;
	private Resources mResource;
	private Activity mContext;
	private List<CartBaseItem> mCartItems;
	
	private ShopCartModel mShopCartModel;
	
	public CartItemListAdapter(Activity context, List<CartBaseItem> cartItems){
		mResource = context.getResources();
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mCartItems = cartItems;
		mShopCartModel = new ShopCartModel(new MyShopCartModelResult());
	}

	@Override
	public int getGroupCount() {
		if(mCartItems == null) return 0;
		return mCartItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(mCartItems == null || mCartItems.isEmpty()) return 0;
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
			holder.cartitemgroup = convertView.findViewById(R.id.cart_group_item);
			holder.title = (TextView) convertView.findViewById(R.id.cart_list_cell_title);
			holder.minus = (Button) convertView.findViewById(R.id.cart_list_cell_minus);
			holder.num = (TextView) convertView.findViewById(R.id.cart_list_cell_num);
			holder.plus = (Button) convertView.findViewById(R.id.cart_list_cell_plus);
			holder.price = (TextView) convertView.findViewById(R.id.cart_list_cell_price);
			holder.minus.setOnClickListener(this);
			holder.plus.setOnClickListener(this);
			holder.cartitemgroup.setOnLongClickListener(this);
			convertView.setTag(holder);
		}else{
			holder = (GroupViewHolder) convertView.getTag();
		}
		CartBaseItem baseItem = mCartItems.get(groupPosition);
		holder.cartitemgroup.setTag(baseItem.getCartId());
		if(baseItem.getCartId() == -1){
			holder.minus.setVisibility(View.INVISIBLE);
			holder.num.setVisibility(View.INVISIBLE);
			holder.plus.setVisibility(View.INVISIBLE);
			holder.price.setVisibility(View.INVISIBLE);
			holder.title.setTextColor(mResource.getColor(R.color.color_app_base_1));
			holder.title.setText(baseItem.getGoodsName());
		}else{
			holder.minus.setVisibility(View.VISIBLE);
			holder.num.setVisibility(View.VISIBLE);
			holder.plus.setVisibility(View.VISIBLE);
			holder.price.setVisibility(View.VISIBLE);
			holder.title.setTextColor(mResource.getColor(R.color.color_app_base_4));
			holder.title.setText(baseItem.getGoodsName());
			holder.num.setText(UnitConvertUtil.getSwitchedWeight(baseItem.getAmount(), baseItem.getUnit()));
			if("1".equals(baseItem.getCartStatus())){
				holder.price.setText(UnitConvertUtil.getSwitchedMoney(baseItem.getPrice()) + "元");
			}else{
				holder.price.setText("已下架");
			}
			holder.minus.setTag(groupPosition);
			holder.plus.setTag(groupPosition);
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
			+ UnitConvertUtil.getSwitchedWeight(material.getAmount(), material.getUnit()));
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	static class GroupViewHolder{
		private View cartitemgroup;
		private TextView title;
		private Button minus;
		private TextView num;
		private Button plus;
		private TextView price;
	}
	
	static class ChildViewHolder{
		private TextView title;
	}

	@Override
	public void onClick(final View view) {
		switch (view.getId()) {
		case R.id.cart_list_cell_minus:
			clickMinusButton(view);
			break;
		case R.id.cart_list_cell_plus:
			clickPlusButton(view);
			break;
		}
	}
	
	/**
	 * 点击减少按钮
	 * @param view
	 */
	private void clickMinusButton(final View view){
		updateCartItemAmount(view, false);
	}
	
	/**
	 * 点击增加按钮
	 * @param view
	 */
	private void clickPlusButton(final View view){
		updateCartItemAmount(view, true);
	}

	/**
	 * 更新购物车数据数量
	 * @param view
	 * @param plus
	 */
	private void updateCartItemAmount(final View view, boolean plus) {
		int position  = (Integer) view.getTag();
		CartBaseItem cartBaseItem = mCartItems.get(position);
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken(mContext);
		if(token == null) return;
		int gap = 1;
		if("01".equals(cartBaseItem.getUnit())){
			gap = UnitConvertUtil.getPlusAmount(cartBaseItem.getAmount(), 
					UnitConvertUtil.getSwichedUnit(cartBaseItem.getAmount(), cartBaseItem.getUnit()));
		}
		int amount;
		if(plus){
			amount = cartBaseItem.getAmount() + gap;
		}else{
			int minAmount = cartBaseItem.getAmount() - gap;
			if(minAmount < cartBaseItem.getMinAmount())
				minAmount = cartBaseItem.getMinAmount();
			amount = minAmount;
		}
		mShopCartModel.requestUpdateCart(mContext, cartBaseItem.getCartId(), amount, token, userId);
	}
	
	class MyShopCartModelResult extends ShopCartModelResult{
		@Override
		public void requestUpdateCartResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				if(mItemChanged != null){
					mItemChanged.changed();
				}
			}else{
				ToastUtil.showToast(mContext, msg);
			}
		}
	}
	
	private CartItemChanged mItemChanged;
	
	public void setCartItemChangedListener(CartItemChanged itemChanged){
		mItemChanged = itemChanged;
	}
	public interface CartItemChanged{
		public void changed();
		public void delete(int cartId);
	}

	@Override
	public boolean onLongClick(View view) {
		Integer cartId  = (Integer) view.getTag();
		if(cartId == null) return false;
		if(mItemChanged != null){
			mItemChanged.delete(cartId);
		}
		return true;
	}
}
