package com.guozha.buy.adapter;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.entry.cart.TimeList;
import com.guozha.buy.entry.mine.order.ExpandListData;
import com.guozha.buy.entry.mine.order.OrderDetail;
import com.guozha.buy.entry.mine.order.OrderDetailGoods;
import com.guozha.buy.entry.mine.order.OrderSummaryPage;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.model.OrderModel;
import com.guozha.buy.model.OrderModel.OrderModelInterface;
import com.guozha.buy.model.result.OrderModelResult;
import com.guozha.buy.util.UnitConvertUtil;

public class OrderDetailMenusListAdapter extends BaseExpandableListAdapter{
	
	private LayoutInflater mInflater;
	private List<ExpandListData> mExpandListDatas;
	private boolean mShowFace;
	private Context mContext;
	private int mOrderId = -1;
	private OrderModel mOrderModel;
	
	public OrderDetailMenusListAdapter(Context context, List<ExpandListData> expandListData, boolean showFace, int orderId){
		this(context, expandListData, showFace);
		mOrderId = orderId;
	}
	
	public OrderDetailMenusListAdapter(Context context, List<ExpandListData> expandListData, boolean showFace){
		mInflater = LayoutInflater.from(context);
		mExpandListDatas = expandListData;
		mShowFace = showFace;
		mContext = context;
		mOrderModel = new OrderModel(new OrderModelResult());
	}

	@Override
	public int getGroupCount() {
		if(mExpandListDatas == null) return 0 ;
		return mExpandListDatas.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(mExpandListDatas == null) return 0;
		ExpandListData expandListData = mExpandListDatas.get(groupPosition);
		if(expandListData == null) return 0;
		List<OrderDetailGoods> orderDetailMenus = expandListData.getMenuslist();
		if(orderDetailMenus == null) return 0;
		return orderDetailMenus.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mExpandListDatas.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return  mExpandListDatas.get(groupPosition).getMenuslist().get(childPosition);
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
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_order_detail_goods_cell, null);
		}
		TextView groupTitle = (TextView) convertView.findViewById(R.id.group_title);
		TextView groupAmount = (TextView) convertView.findViewById(R.id.group_amount);
		TextView groupPrice = (TextView) convertView.findViewById(R.id.group_price);
		View groupGradeArea = convertView.findViewById(R.id.group_grade_area);
		ExpandListData expandListData = mExpandListDatas.get(groupPosition);
		groupTitle.setText(expandListData.getName());
		groupAmount.setText(UnitConvertUtil.getSwitchedWeight(expandListData.getAmount(), expandListData.getUnit()));
		groupPrice.setText(UnitConvertUtil.getSwitchedMoney(expandListData.getPrice()) + "元");
		if(!mShowFace){
			groupGradeArea.setVisibility(View.GONE);
		}else{
			if(expandListData.getType() == 0){ //如果是菜谱
				groupGradeArea.setVisibility(View.GONE);
			}else{
				groupGradeArea.setVisibility(View.VISIBLE);
				ImageView face1 = (ImageView) convertView.findViewById(R.id.group_grade_face1);
				ImageView face2 = (ImageView) convertView.findViewById(R.id.group_grade_face2);
				ImageView face3 = (ImageView) convertView.findViewById(R.id.group_grade_face3);
				ClickFaceListener clickFaceListener = new ClickFaceListener(face1, face2, face3);
				face1.setTag(expandListData.getId());
				face2.setTag(expandListData.getId());
				face3.setTag(expandListData.getId());
				face1.setOnClickListener(clickFaceListener);
				face2.setOnClickListener(clickFaceListener);
				face3.setOnClickListener(clickFaceListener);
			}
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_order_detail_menu_cell, null);
		}
		TextView materailTitle = (TextView) convertView.findViewById(R.id.cart_list_cell_material_title);
		View childGradeArea = convertView.findViewById(R.id.child_grade_area);
		OrderDetailGoods orderDetailMenus = 
				mExpandListDatas.get(groupPosition).getMenuslist().get(childPosition);
		materailTitle.setText(orderDetailMenus.getGoodsName() + "  " 
				+ UnitConvertUtil.getSwitchedWeight(orderDetailMenus.getAmount(), orderDetailMenus.getUnit()));
		if(mShowFace){
			childGradeArea.setVisibility(View.VISIBLE);
			ImageView face1 = (ImageView) convertView.findViewById(R.id.child_grade_face1);
			ImageView face2 = (ImageView) convertView.findViewById(R.id.child_grade_face2);
			ImageView face3 = (ImageView) convertView.findViewById(R.id.child_grade_face3);
			ClickFaceListener clickFaceListener = new ClickFaceListener(face1, face2, face3);
			face1.setTag(orderDetailMenus.getId());
			face2.setTag(orderDetailMenus.getId());
			face3.setTag(orderDetailMenus.getId());
			face1.setOnClickListener(clickFaceListener);
			face2.setOnClickListener(clickFaceListener);
			face3.setOnClickListener(clickFaceListener);
		}else{
			childGradeArea.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	/**
	 * 点击笑脸评价监听
	 * @author PeggyTong
	 *
	 */
	class ClickFaceListener implements OnClickListener{
		private ImageView mFace1;
		private ImageView mFace2;
		private ImageView mFace3;
		public ClickFaceListener(ImageView face1, ImageView face2, ImageView face3){
			mFace1 = face1;
			mFace2 = face2;
			mFace3 = face3;
		}
		
		@Override
		public void onClick(View view) {
			mFace1.setImageResource(R.drawable.main_emoticons_cool_normal);
			mFace2.setImageResource(R.drawable.main_emoticons_wondering_normal);
			mFace3.setImageResource(R.drawable.main_emoticons_angry_normal);
			int id = (Integer) view.getTag();
			ImageView icon = (ImageView) view;
			int marketType = -1; // 1 菜谱  2 菜场
			int goodsStar = -2; //1 满意 2 一般 3不满
			switch (view.getId()) {
			case R.id.group_grade_face1:
				goodsStar = 1;
				marketType = 2;
				icon.setImageResource(R.drawable.main_emoticons_cool_selected);
				break;
			case R.id.group_grade_face2:
				goodsStar = 0;
				marketType = 2;
				icon.setImageResource(R.drawable.main_emoticons_wondering_selected);
				break;
			case R.id.group_grade_face3:
				goodsStar = -1;
				marketType = 2;
				icon.setImageResource(R.drawable.main_emoticons_angry_selected);
				break;
			case R.id.child_grade_face1:
				goodsStar = 1;
				marketType = 1;
				icon.setImageResource(R.drawable.main_emoticons_cool_selected);
				break;
			case R.id.child_grade_face2:
				goodsStar = 0;
				marketType = 1;
				icon.setImageResource(R.drawable.main_emoticons_wondering_selected);
				break;
			case R.id.child_grade_face3:
				goodsStar = -1;
				marketType = 1;
				icon.setImageResource(R.drawable.main_emoticons_angry_selected);
				break;
			}
			if(marketType == -1 || goodsStar == -2) return;
			String token = ConfigManager.getInstance().getUserToken();
			mOrderModel.requestGradeProduct(
					mContext, id, mOrderId, token, marketType, goodsStar);
		}
	}
}
