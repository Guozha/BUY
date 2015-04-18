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
	private static final String COMMEN_TITILE = "小菜来了！做饭这档子事，除了乐趣，还能剩什么~";
	private static final String COMMEN_CONTENT = "且看我如何将你一秒变大厨！";
	private static final String DESCRIPTOR = "com.umeng.share";
	private static final String COMMEN_TARGET_URL = "http://download.wymc.com.cn/app/buyer_app.html";
	
	private UMImage mCommenShareImage;
	
	private SnsPostListener mShareResultListener;
	
	private static final String QQ_APPID = "1103475446";
	private static final String QQ_APPKEY = "HVh2KnfqqeHFuzCt";
	
	private static final String WX_APPID = "wxc17fccd72a2dbea7";
	private static final String WX_SECRET = "6f913bd53fdfd3529c98a7ae84df4d73";

	private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(DESCRIPTOR);
	
	public ShareManager(Activity activity){
		this(activity, null);
	}
	
	public ShareManager(Activity activity, SnsPostListener resultListener){
		mCommenShareImage = new UMImage(activity, R.drawable.logo_share);
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

		qqSsoHandler.setTitle("这里是");
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
	 * 打开分享面板
	 */
	public void showSharePlatform(Activity activity){
		setQQContent(COMMEN_TITILE, COMMEN_CONTENT);
		setQzoneContent(COMMEN_TITILE, COMMEN_CONTENT);
		setWeixinContent(COMMEN_TITILE, COMMEN_CONTENT);
		setWXFriendsContent(COMMEN_TITILE, COMMEN_CONTENT);
		setSinaContent(COMMEN_TITILE, COMMEN_CONTENT);
		mController.openShare(activity, false);
	}
	
	/**
	 * 分享到新浪微博
	 * @param context
	 */
	public void shareToSina(Context context){
		shareToSina(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 分享到QQ
	 * @param context
	 */
	public void shareToQQ(Context context){
		shareToQQ(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 分享到QQ空间
	 * @param context
	 */
	public void shareToQZone(Context context){
		shareToQZone(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 分享到微信
	 * @param context
	 */
	public void shareToWeixin(Context context){
		shareToWeixin(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 自定义分享到微信朋友圈
	 * @param context
	 * @param image
	 * @param title
	 * @param content
	 * @param url
	 */
	public void shareToWeixinFriends(Context context, UMImage image, String title, String content, String url){
		//设置分享内容
		CircleShareContent circleShareContent = new CircleShareContent();
		
		
		circleShareContent.setTitle(title);
		circleShareContent.setShareContent(content);
		circleShareContent.setTargetUrl(url);
		circleShareContent.setShareImage(image);
		mController.setShareMedia(circleShareContent);
		mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE, mShareResultListener);
	}
	
	/**
	 * 分享到微信朋友圈
	 * @param context
	 */
	public void shareToWXFriends(Context context){
		shareToWXFriends(context, COMMEN_TITILE, COMMEN_CONTENT);
	}
	
	/**
	 * 分享到新浪微博
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToSina(Context context, String title, String content){
		setSinaContent(title, content);
		//分享
		mController.postShare(context, SHARE_MEDIA.SINA, mShareResultListener);
	}

	/**
	 * 设置分享到新浪微博的内容
	 * @param title
	 * @param content
	 */
	private void setSinaContent(String title, String content) {
		//设置分享内容
		SinaShareContent sinaShareContent = new SinaShareContent();
		sinaShareContent.setTitle(title);
		sinaShareContent.setShareContent(content);
		sinaShareContent.setTargetUrl(COMMEN_TARGET_URL);
		sinaShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(sinaShareContent);
	}
	
	/**
	 * 分享到QQ
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToQQ(Context context, String title, String content){
		setQQContent(title, content);
		//分享
		mController.postShare(context, SHARE_MEDIA.QQ, mShareResultListener);
	}

	/**
	 * 设置分享到QQ的内容
	 * @param title
	 * @param content
	 */
	private void setQQContent(String title, String content) {
		//设置分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setTitle(title);
		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(COMMEN_TARGET_URL);
		qqShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(qqShareContent);
	}
	
	/**
	 * 分享到QQ空间
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToQZone(Context context, String title, String content){
		setQzoneContent(title, content);
		//分享
		mController.postShare(context, SHARE_MEDIA.QZONE, mShareResultListener);
	}

	/**
	 * 设置分享到QQ空间的内容
	 * @param title
	 * @param content
	 */
	private void setQzoneContent(String title, String content) {
		//设置分享内容
		QZoneShareContent qzoneShareContent = new QZoneShareContent();
		qzoneShareContent.setTitle(title);
		qzoneShareContent.setShareContent(content);
		qzoneShareContent.setTargetUrl(COMMEN_TARGET_URL);
		qzoneShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(qzoneShareContent);
	}
	
	/**
	 * 分享到微信
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToWeixin(Context context, String title, String content){
		setWeixinContent(title, content);
		//分享
		mController.postShare(context, SHARE_MEDIA.WEIXIN, mShareResultListener);
	}

	/**
	 * 设置分享到微信的内容
	 * @param title
	 * @param content
	 */
	private void setWeixinContent(String title, String content) {
		//设置分享内容
		WeiXinShareContent weixinShareContent = new WeiXinShareContent();
		weixinShareContent.setTitle(title);
		weixinShareContent.setShareContent(content);
		weixinShareContent.setTargetUrl(COMMEN_TARGET_URL);
		weixinShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(weixinShareContent);
	}
	
	/**
	 * 分享到微信朋友圈
	 * @param context
	 * @param title 分享标题
	 * @param content 分享内容
	 */
	public void shareToWXFriends(Context context, String title, String content){
		setWXFriendsContent(title, content);
		//分享
		mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE, mShareResultListener);
	}

	/**
	 * 设置微信朋友圈分享的内容
	 * @param title
	 * @param content
	 */
	private void setWXFriendsContent(String title, String content) {
		//设置分享内容
		CircleShareContent circleShareContent = new CircleShareContent();
		circleShareContent.setTitle(title);
		circleShareContent.setShareContent(content);
		circleShareContent.setTargetUrl(COMMEN_TARGET_URL);
		circleShareContent.setShareImage(mCommenShareImage);
		mController.setShareMedia(circleShareContent);
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
