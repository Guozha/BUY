package com.guozha.buy.controller.best.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.controller.SetWarnTimeActivity;
import com.guozha.buy.controller.dialog.CustomDialog;
import com.guozha.buy.controller.mine.AboutOurActivity;
import com.guozha.buy.controller.mine.MyAddressActivity;
import com.guozha.buy.controller.mine.MyCollectionActivity;
import com.guozha.buy.controller.mine.MyInvateNumActivity;
import com.guozha.buy.controller.mine.MyOrderActivity;
import com.guozha.buy.controller.mine.MyTicketActivity;
import com.guozha.buy.controller.mine.SharePraiseActivity;
import com.guozha.buy.entry.mine.account.AccountInfo;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.UserModel;
import com.guozha.buy.model.result.UserModelResult;
import com.guozha.buy.util.BitmapUtil;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.util.UnitConvertUtil;
import com.umeng.analytics.MobclickAgent;

public class MainTabFragmentMine extends MainTabBaseFragment implements OnClickListener{
	
	private static final String PAGE_NAME = "MinePage";
	
	public static final int REQUEST_CODE_LOGIN = 0;
	public static final int REQUEST_CODE_SETTING = 1;
	
	private static final int HAND_ACCOUNT_INFO_COMPLETED = 0x0001;
	
	private ImageView mMineHeadImg;
	private TextView mMinePhoneNum;
	private TextView mMineRemainMoney;
	private TextView mMineTickes;
	private TextView mMineBeans;
	
	private View mHeadArea;
	private View mAccountInfoArea;
	
	private AccountInfo mAccountInfo;
	private UserModel mUserModel = new UserModel(new MyUserModelResult());
	
	private Handler mHandle = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_ACCOUNT_INFO_COMPLETED:
				setInfos();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maintab_mine, container, false);
		initActionBar("我的");
		initView(view);
		initData();
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
		mHeadArea = view.findViewById(R.id.mine_head_area);
		view.findViewById(R.id.mine_order_button).setOnClickListener(this);
		view.findViewById(R.id.mine_warn_button).setOnClickListener(this);
		view.findViewById(R.id.mine_invitation_button).setOnClickListener(this);
		view.findViewById(R.id.mine_collection_button).setOnClickListener(this);
		view.findViewById(R.id.mine_ticket_button).setOnClickListener(this);
		view.findViewById(R.id.mine_address_button).setOnClickListener(this);
		view.findViewById(R.id.mine_weixin_kefu__button).setOnClickListener(this);
		view.findViewById(R.id.mine_online_service__button).setOnClickListener(this);
		view.findViewById(R.id.mine_invate_num_button).setOnClickListener(this);
		mAccountInfoArea.setVisibility(View.GONE);
	}
	
	private void initData(){
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken();
		if(token == null) {
			mAccountInfoArea.setVisibility(View.GONE);
			mHeadArea.setBackgroundResource(R.drawable.background_personal);
			mMineHeadImg.setImageResource(R.drawable.button_login);
			return;
		}
		mUserModel.requestAccountInfo(getActivity(), token, userId);
	}
	
	private void setInfos(){
		if(mAccountInfo == null) return;
		mAccountInfoArea.setVisibility(View.VISIBLE);
		mHeadArea.setBackgroundColor(getResources().getColor(R.color.color_app_base_24));
		mMineHeadImg.setImageResource(R.drawable.tag_personal);
		
		if(mMinePhoneNum == null) return;
		mMinePhoneNum.setText(mAccountInfo.getMobileNo());
		if(mMineTickes == null) return;
		mMineTickes.setText(String.valueOf(mAccountInfo.getTicketAmount()));
		if(mMineBeans == null) return;
		mMineBeans.setText(String.valueOf(mAccountInfo.getBeanAmount()));
		if(mMineRemainMoney == null) return;
		mMineRemainMoney.setText(String.valueOf(UnitConvertUtil.getSwitchedMoney(mAccountInfo.getBalance())));
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		//如果没有登录
		if(ConfigManager.getInstance().getUserToken(getActivity()) == null) return;
		switch (view.getId()) {
		case R.id.mine_head_img:  	//更换头像
			//showChooseImageMethodDialog();
			break;
		case R.id.mine_order_button: 	//我的订单
			intent = new Intent(getActivity(), MyOrderActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_warn_button:
			intent = new Intent(getActivity(), SetWarnTimeActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_invitation_button: //推荐有奖
			intent = new Intent(getActivity(), SharePraiseActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_collection_button: 	//我的收藏
			intent = new Intent(getActivity(), MyCollectionActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_ticket_button: 		//我的菜票
			intent = new Intent(getActivity(), MyTicketActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_address_button:		//我的地址
			intent = new Intent(getActivity(), MyAddressActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_online_service__button:
			dialServerTelephone();
			break;
		case R.id.mine_weixin_kefu__button:
			dialogWeixinKefu();
			break;
		case R.id.mine_invate_num_button:
			intent = new Intent(getActivity(), MyInvateNumActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 显示选择图片方式对话框
	 */
	/*
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
	*/
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.e("onActivityResult..");
		initData(); 
		/*
		switch (requestCode) {
		case 0: //请求登录
			if(resultCode == LoginActivity.RESULT_CODE_LOGIN){
				if(data != null && data.getBooleanExtra(LoginActivity.LOGIN_STATUS, false)){
					
				}
			}
			break;
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
		*/
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
	
	/**
	 * 拨打客服电话
	 */
	private void dialServerTelephone() {
		final CustomDialog dialDialog = 
			new CustomDialog(getActivity(), R.layout.dialog_dial_telephone);
		dialDialog.setDismissButtonId(R.id.cancel_button);
		dialDialog.getViewById(R.id.dial_tel_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialDialog.dismiss();
				String phoneNum = "0571-86021150";
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
				MainTabFragmentMine.this.startActivity(intent);
			}
		});
	}
	
	private void dialogWeixinKefu(){
		final CustomDialog dialDialog = 
			new CustomDialog(getActivity(), R.layout.dialog_weixin_kefu);
		TextView weixinnum = (TextView) dialDialog.getViewById(R.id.weixinnum);
		weixinnum.setText("微信号：" + ConfigManager.getInstance().getWeixinNum());
		dialDialog.setDismissButtonId(R.id.cancel_button);
		dialDialog.getViewById(R.id.copy_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				copyText(ConfigManager.getInstance().getWeixinNum());
				dialDialog.dismiss();
			}
		});
	}
	
	private void copyText(String text) {
		ClipboardManager clipboard = (ClipboardManager) 
				getActivity().getSystemService(android.content.Context.CLIPBOARD_SERVICE);  
		ClipData clip =ClipData.newPlainText("orderMessage", text);
		clipboard.setPrimaryClip(clip);
		ToastUtil.showToast(MainTabFragmentMine.this.getActivity(), "已经复制到剪贴板");
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

	class MyUserModelResult extends UserModelResult{
		@Override
		public void requestAccountInfoResult(AccountInfo accountInfo) {
			mAccountInfo = accountInfo;
			mHandle.sendEmptyMessage(HAND_ACCOUNT_INFO_COMPLETED);
		}
	}

}
