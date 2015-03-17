package com.guozha.buy.fragment;

import java.io.File;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.activity.LoginActivity;
import com.guozha.buy.entry.AccountInfo;
import com.guozha.buy.global.CustomApplication;
import com.guozha.buy.global.MainPageInitDataManager;
import com.guozha.buy.util.BitmapUtil;
import com.guozha.buy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMine extends MainTabBaseFragment implements OnClickListener{
	
	private static final String PAGE_NAME = "MinePage";
	
	private ImageView mMineHeadImg;
	
	private TextView mMinePhoneNum;
	private TextView mMineRemainMoney;
	private TextView mMineTickes;
	private TextView mMineBeans;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_mine, container, false);
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
		
		view.findViewById(R.id.mine_orderform).setOnClickListener(this);
		view.findViewById(R.id.mine_advice_active).setOnClickListener(this);
		view.findViewById(R.id.mine_collection).setOnClickListener(this);
		view.findViewById(R.id.mine_ticket).setOnClickListener(this);
		view.findViewById(R.id.mine_address).setOnClickListener(this);
		view.findViewById(R.id.mine_buyer).setOnClickListener(this);
		
		setInfos();
	}
	
	private void setInfos(){
		if(mDataManager == null) return;
		AccountInfo accountInfo = mDataManager.getAccountInfo(null);
		if(accountInfo == null) return;
		mMinePhoneNum.setText(accountInfo.getMobileNo());
		mMineTickes.setText("菜票 " + accountInfo.getTicketAmount() + "张");
		mMineBeans.setText("菜豆 " + accountInfo.getBeanAmount() + "个");
		mMineRemainMoney.setText("我的余额 ￥" + accountInfo.getBalance());
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
		Intent intent;
		switch (view.getId()) {
		case R.id.mine_head_img:  	//更换头像
			showChooseImageMethodDialog();
			break;
		case R.id.mine_orderform: 	//我的订单
			intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_advice_active: //推荐有奖
			break;
		case R.id.mine_collection: 	//我的收藏
			
			break;
		case R.id.mine_ticket: 		//我的菜票
			
			break;
		case R.id.mine_address:		//我的地址
			
			break;
		case R.id.mine_buyer:		//我的卖家
			
			break;	

		default:
			break;
		}
	}
	
	/**
	 * 显示选择图片方式对话框
	 */
	private void showChooseImageMethodDialog(){
		new AlertDialog.Builder(getActivity())
		.setTitle("设置头像")
		.setNegativeButton("相册", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 1);
			}
		})
		.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, 
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/head.jpg")));
				startActivityForResult(intent, 2);
			}
		}).show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1: //从相册获取
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
		super.onActivityResult(requestCode, resultCode, data);
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
		    //初始化ActionBar	
			initActionBar(getActivity().getActionBar());
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
