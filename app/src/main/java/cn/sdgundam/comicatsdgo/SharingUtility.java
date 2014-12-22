package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.socialize.media.UMImage;

/**
 * Created by xhguo on 12/18/2014.
 */
public class SharingUtility {

    public static void setupWeixinShare(Context context, UMSocialService umSocialService, String title, String content, String url, String image) {
        String appId = context.getResources().getString(R.string.weixin_app_id);
        String appSecret = context.getResources().getString(R.string.weixin_app_secret);
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(url);
        if (image != null) {
            weixinContent.setShareImage(new UMImage(context, image));
        }
        umSocialService.setShareMedia(weixinContent);

        //设置微信朋友圈分享内容
        CircleShareContent circleShareContent = new CircleShareContent();
        circleShareContent.setShareContent(content);
        circleShareContent.setTitle(title);
        circleShareContent.setTargetUrl(url);
        if (image != null) {
            circleShareContent.setShareImage(new UMImage(context, image));
        }
        umSocialService.setShareMedia(circleShareContent);
    }

    public static void setupQQShare(Context context, UMSocialService umSocialService, String title, String content, String url, String image) {
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity)context,
                context.getResources().getString(R.string.qq_app_id),
                context.getResources().getString(R.string.qq_app_key));
        qqSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        //设置分享文字
        qqShareContent.setShareContent(content);
        //设置分享title
        qqShareContent.setTitle(title);
        //设置分享图片
        if (image != null) {
            qqShareContent.setShareImage(new UMImage(context, image));
        }
        //设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl(url);
        umSocialService.setShareMedia(qqShareContent);


        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity)context,
                context.getResources().getString(R.string.qq_app_id),
                context.getResources().getString(R.string.qq_app_key));
        qZoneSsoHandler.addToSocialSDK();
        QZoneShareContent qzone = new QZoneShareContent();
        //设置分享文字
        qzone.setShareContent(content);
        //设置点击消息的跳转URL
        qzone.setTargetUrl(url);
        //设置分享内容的标题
        qzone.setTitle(title);
        //设置分享图片
        if (image != null) {
            qzone.setShareImage(new UMImage(context, image));
        }
        umSocialService.setShareMedia(qzone);

    }

    public static void configureListener(final Context context, final UMSocialService umSocialService) {
        umSocialService.registerListener(new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int code, SocializeEntity socializeEntity) {
                if (code == 200) {
                    Toast.makeText(context, "分享成功!", Toast.LENGTH_SHORT)
                            .show();
                } else if (code != 40000) {
                    Toast.makeText(context, "分享失败, 错误代码: " + code, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

}
