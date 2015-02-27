package com.guozha.buy.share;

import android.app.Activity;
import android.content.Context;

import com.guozha.buy.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 分享管理类
 * @author PeggyTong
 *
 */
public class ShareManager{
	private static final String COMMEN_TITILE = "this is title";
	private static final String COMMEN_CONTENT = "this is content";
	private static final String DESCRIPTOR = "com.umeng.share";
	private static final String COMMEN_TARGET_URL = "http://www.wymc.com.cn";
	
	private UMImage mCommenShareImage;
	
	private SnsPostListener mShareResultListener;
	
	private static final String QQ_APPID = "100424468";
	private static final String QQ_APPKEY = "c7394704798a158208a74ab60104f0ba";
	
	private static final String WX_APPID = "wx967daebe835fbeac";
	private static final String WX_SECRET = "5bb696d9ccd75a38c8a0bfe0675559b3";

	private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(DESCRIPTOR);
	
	public ShareManager(Activity activity){
		this(activity, null);
	}
	
	public ShareManager(Activity activity, SnsPostListener resultListener){
		mCommenShareImage = new UMImage(activity, R.drawable.ic_launcher);
		if(resultListener == null){
			this.mShareResultListener = new ShareResultListener();
		}else{
			this.mShareResultListener = resultListener;
		}
		//添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		//添加QQ和QZone授权
		addQQAndQZoneSSO(activity);
		//添加微信、微信朋友圈授权
		addWXPlatfromSSO(activity);
	}
	
	/**
	 * 添加QQ和QQ空间授权
	 * @param activity
	 */
	private void addQQAndQZoneSSO(Activity activity){
		//添加QQ支持
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, QQ_APPID, QQ_APPKEY);
		qqSsoHandler.setTargetUrl(COMMEN_TARGET_URL);
		qqSsoHandler.addToSocialSDK();
		
		//添加QZone支持
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, QQ_APPID, QQ_APPKEY);
		qZoneSsoHandler.addToSocialSDK();
	}
	
	/**
	 * 添加微信和微信朋友圈授权
	 * @param context
	 */
	private void addWXPlatfromSSO(Context context){
		//添加微信支持
		UMWXHandler wxHandler = new UMWXHandler(context, WX_APPID, WX_SECRET);
		wxHandler.addToSocialSDK();
		
		//添加朋友圈支持
		UMWXHandler wxCircleHandler = new UMWXHandler(context, WX_APPID, WX_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}
	
	/**
	 * 分享到新浪微博
	 * @param context
	 */
	public void shareToSina(Context context){
		shareToSina(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 分享到新浪微博
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToSina(Context context, String title, String content){
		//设置分享内容
		SinaShareContent sinaShareContent = new SinaShareContent();
		sinaShareContent.setTitle(title);
		sinaShareContent.setShareContent(content);
		sinaShareContent.setTargetUrl(COMMEN_TARGET_URL);
		sinaShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(sinaShareContent);
		//分享
		mController.postShare(context, SHARE_MEDIA.SINA, mShareResultListener);
	}
	
	/**
	 * 分享到QQ
	 * @param context
	 */
	public void shareToQQ(Context context){
		shareToQQ(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 分享到QQ
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToQQ(Context context, String title, String content){
		//设置分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setTitle(title);
		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(COMMEN_TARGET_URL);
		qqShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(qqShareContent);
		//分享
		mController.postShare(context, SHARE_MEDIA.QQ, mShareResultListener);
	}
	
	/**
	 * 分享到QQ空间
	 * @param context
	 */
	public void shareToQZone(Context context){
		shareToQZone(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 分享到QQ空间
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToQZone(Context context, String title, String content){
		//设置分享内容
		QZoneShareContent qzoneShareContent = new QZoneShareContent();
		qzoneShareContent.setTitle(title);
		qzoneShareContent.setShareContent(content);
		qzoneShareContent.setTargetUrl(COMMEN_TARGET_URL);
		qzoneShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(qzoneShareContent);
		//分享
		mController.postShare(context, SHARE_MEDIA.QZONE, mShareResultListener);
	}
	
	/**
	 * 分享到微信
	 * @param context
	 */
	public void shareToWeixin(Context context){
		shareToWeixin(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 分享到微信
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToWeixin(Context context, String title, String content){
		//设置分享内容
		WeiXinShareContent weixinShareContent = new WeiXinShareContent();
		weixinShareContent.setTitle(title);
		weixinShareContent.setShareContent(content);
		weixinShareContent.setTargetUrl(COMMEN_TARGET_URL);
		weixinShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(weixinShareContent);
		//分享
		mController.postShare(context, SHARE_MEDIA.WEIXIN, mShareResultListener);
	}
	
	/**
	 * 分享到微信朋友圈
	 * @param context
	 */
	public void shareToWXFriends(Context context){
		shareToWXFriends(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 分享到微信朋友圈
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToWXFriends(Context context, String title, String content){
		//设置分享内容
		CircleShareContent circleShareContent = new CircleShareContent();
		circleShareContent.setTitle(title);
		circleShareContent.setShareContent(content);
		circleShareContent.setTargetUrl(COMMEN_TARGET_URL);
		circleShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(circleShareContent);
		//分享
		mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE, mShareResultListener);
	}
	
	/**
	 * 分享结果回调监听
	 * @author PeggyTong
	 *
	 */
	class ShareResultListener implements SnsPostListener{

		@Override
		public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
			if(eCode == StatusCode.ST_CODE_SUCCESSED){
				//分享成功
			}else{
				//分享失败
			}
		}

		@Override
		public void onStart() {
			//开始分享回调
		}
	}
}
