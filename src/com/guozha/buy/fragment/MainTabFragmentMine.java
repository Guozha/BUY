package com.guozha.buy.fragment;

import java.io.File;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.LoginActivity;
import com.guozha.buy.activity.mine.MyAddressActivity;
import com.guozha.buy.activity.mine.MyCollectionActivity;
import com.guozha.buy.activity.mine.MyOrderActivity;
import com.guozha.buy.activity.mine.MySellerActivity;
import com.guozha.buy.activity.mine.MyTicketActivity;
import com.guozha.buy.activity.mine.SharePraiseActivity;
import com.guozha.buy.dialog.CustomDialog;
import com.guozha.buy.dialog.RemindLoginDialog;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.util.BitmapUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMine extends MainTabBaseFragment implements OnClickListener{
	
	private static final String PAGE_NAME = "MinePage";
	
	private ImageView mMineHeadImg;
	
	private TextView mMinePhoneNum;
	private TextView mMineRemainMoney;
	private TextView mMineTickes;
	private TextView mMineBeans;
	
	private View mAccountInfoArea;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_mine, container, false);
		initActionBar("我的");
		initView(view);
		return view;
	}
	
	/**
	 * 初始化视图
	 * @param view
	 */
	private void initView(View view){
		if(view == null) return;
		//TODO 设置头像功能此版本暂时不做
		mMineHeadImg = (ImageView) view.findViewById(R.id.mine_head_img);
		mMineHeadImg.setOnClickListener(this);
		
		mMinePhoneNum = (TextView) view.findViewById(R.id.mine_msg_phonenum);
		mMineRemainMoney = (TextView) view.findViewById(R.id.mine_msg_remain_money);
		mMineTickes = (TextView) view.findViewById(R.id.mine_msg_tickes);
		mMineBeans = (TextView) view.findViewById(R.id.mine_msg_bean);
		
		mAccountInfoArea = view.findViewById(R.id.mine_top_account_message_area);
		view.findViewById(R.id.mine_orderform).setOnClickListener(this);
		view.findViewById(R.id.mine_advice_active).setOnClickListener(this);
		view.findViewById(R.id.mine_collection).setOnClickListener(this);
		view.findViewById(R.id.mine_ticket).setOnClickListener(this);
		view.findViewById(R.id.mine_address).setOnClickListener(this);
		view.findViewById(R.id.mine_buyer).setOnClickListener(this);
		
		setInfos();
	}
	
	private void setInfos(){
		if(mAccountInfoArea == null) return;
		mAccountInfoArea.setVisibility(View.VISIBLE);
		
		if(ConfigManager.getInstance().getUserToken() == null){
			mAccountInfoArea.setVisibility(View.GONE);
			return;
		}
		if(mDataManager == null) {
			mDataManager = MainPageInitDataManager.getInstance();
		}
		AccountInfo accountInfo = mDataManager.getAccountInfo(null);
		if(accountInfo == null) {
			mAccountInfoArea.setVisibility(View.GONE);
			return;
		}
		
		if(mMinePhoneNum == null) return;
		mMinePhoneNum.setText(accountInfo.getMobileNo());
		if(mMineTickes == null) return;
		mMineTickes.setText("菜票 " + accountInfo.getTicketAmount() + "张");
		if(mMineBeans == null) return;
		mMineBeans.setText("菜豆 " + accountInfo.getBeanAmount() + "个");
		if(mMineRemainMoney == null) return;
		mMineRemainMoney.setText("我的余额 ￥" + UnitConvertUtil.getSwitchedMoney(accountInfo.getBalance()));
		setTextColor();
	}
	
	@Override
	public void loadDataCompleted(MainPageInitDataManager dataManager, int handlerType) {
		switch (handlerType) {
		case MainPageInitDataManager.HAND_INITDATA_MSG_ACCOUNTINFO:
			this.mDataManager = dataManager;
			setInfos();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置文字颜色
	 */
	private void setTextColor() {
		String msgRemainMoney = mMineRemainMoney.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(msgRemainMoney);
		
		ForegroundColorSpan redSpan = new ForegroundColorSpan(
				getResources().getColor(R.color.color_app_base_1));
		int totalSpanSart = msgRemainMoney.indexOf("￥");
		builder.setSpan(redSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(redSpan, totalSpanSart, msgRemainMoney.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mMineRemainMoney.setText(builder);
		
		String msgTickes = mMineTickes.getText().toString();
		builder.clear();
		builder.append(msgTickes);
		builder.setSpan(redSpan, msgTickes.indexOf(" "), msgTickes.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mMineTickes.setText(builder);
		
		String msgBeans = mMineBeans.getText().toString();
		builder.clear();
		builder.append(msgBeans);
		builder.setSpan(redSpan, msgBeans.indexOf(" "), msgBeans.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mMineBeans.setText(builder);
	}

	@Override
	public void onClick(View view) {
		//如果没有登录
		if(ConfigManager.getInstance().getUserToken(getActivity()) == null){
			return;
		}
		loginedStatuEvent(view);
	}

	/**
	 * 登录状态的事件处理
	 * @param view
	 */
	private void loginedStatuEvent(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.mine_head_img:  	//更换头像
			//showChooseImageMethodDialog();
			break;
		case R.id.mine_orderform: 	//我的订单
			intent = new Intent(getActivity(), MyOrderActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_advice_active: //推荐有奖
			intent = new Intent(getActivity(), SharePraiseActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_collection: 	//我的收藏
			intent = new Intent(getActivity(), MyCollectionActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_ticket: 		//我的菜票
			intent = new Intent(getActivity(), MyTicketActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_address:		//我的地址
			intent = new Intent(getActivity(), MyAddressActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_buyer:		//我的卖家
			intent = new Intent(getActivity(), MySellerActivity.class);
			startActivity(intent);
			break;	
		default:
			break;
		}
	}

	/**
	 * 没有登录状态的事件处理
	 * @param view
	 */
	private void outLoginStatuEvent(View view) {
		Intent intent;
		if(view.getId() == R.id.mine_head_img){
			intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			return;
		}
		String turnActivityName = null;
		switch (view.getId()) {
		case R.id.mine_orderform: 	//我的订单
			turnActivityName = "com.guozha.buy.activity.mine.MyOrderActivity";
			break;
		case R.id.mine_advice_active: //推荐有奖
			turnActivityName = "com.guozha.buy.activity.mine.AdvicePraiseActivity";
			break;
		case R.id.mine_collection: 	//我的收藏
			turnActivityName = "com.guozha.buy.activity.mine.MyCollectionActivity";
			break;
		case R.id.mine_ticket: 		//我的菜票
			turnActivityName = "com.guozha.buy.activity.mine.MyTicketActivity";
			break;
		case R.id.mine_address:		//我的地址
			turnActivityName = "com.guozha.buy.activity.mine.MyAddressActivity";
			break;
		case R.id.mine_buyer:		//我的卖家
			turnActivityName = "com.guozha.buy.activity.mine.MySellerActivity";
			break;	
		}
		if(turnActivityName == null) return;
		intent = new Intent(getActivity(), RemindLoginDialog.class);
		startActivity(intent);
		return;
	}
	
	/**
	 * 显示选择图片方式对话框
	 */
	private void showChooseImageMethodDialog(){
		final CustomDialog setHeadDialog = new CustomDialog(getActivity(), R.layout.dialog_set_head);
		Button albumButton = (Button) setHeadDialog.getViewById(R.id.album_button);
		Button photoButton = (Button) setHeadDialog.getViewById(R.id.photograph_button);
		albumButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setHeadDialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 1);
			}
		});
		
		photoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setHeadDialog.dismiss();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, 
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/head.jpg")));
				startActivityForResult(intent, 2);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1: //从相册获取
			if(data == null) return;
			startPhotoZoom(data.getData());
			break;
		case 2: //相机拍照
			File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		case 3: 
			if(data != null){
				setPicToView(data);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 裁剪图片
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/*");  
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪  
        intent.putExtra("crop", "true");  
        // aspectX aspectY 是宽高的比例  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        // outputX outputY 是裁剪图片宽高  
        intent.putExtra("outputX", 172);  
        intent.putExtra("outputY", 172);  
        intent.putExtra("return-data", true);  
        startActivityForResult(intent, 3); 
	}
	
	/**
	 * 保存裁剪后的图片数据
	 * @param data
	 */
	private void setPicToView(Intent picdata){
		Bundle extras = picdata.getExtras();
		if(extras != null){
			Bitmap headPic = extras.getParcelable("data");  
			Bitmap circleHead = BitmapUtil.createCircleBitmap(headPic);
			mMineHeadImg.setImageBitmap(circleHead);
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			//View可见
			//友盟页面统计
			MobclickAgent.onPageStart(PAGE_NAME);
		}else{
			//View不可见
			
			//友盟页面统计
			MobclickAgent.onPageEnd(PAGE_NAME);
		}
	}
	
	/**
	 * 初始化ActionBar
	 * @param actionbar
	 */
	private void initActionBar(ActionBar actionbar) {
		if(actionbar == null) return;
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.actionbar_base_view);
		TextView title = (TextView) actionbar.getCustomView().findViewById(R.id.title);
		title.setText("我的");
	}

}
