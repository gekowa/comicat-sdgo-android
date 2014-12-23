package cn.sdgundam.comicatsdgo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.spot.SpotManager;

import cn.dm.android.DMOfferWall;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;

/**
 * Created by xhguo on 12/9/2014.
 */
public class GDApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initImageLoader(getApplicationContext());

        GDApiService apiService = new GDApiService(this);
        apiService.checkOriginUpdate(true);

//        if (BuildConfig.DEBUG) {
//            Picasso.with(this).setIndicatorsEnabled(true);
//        }

        // Log.d("Device Info: ", getDeviceInfo(this));

//        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);

        // 有米
        AdManager.getInstance(this).init(
                getResources().getString(R.string.youmi_app_id),
                getResources().getString(R.string.youmi_app_secret), false);

        // 有米 积分墙
        OffersManager.getInstance(this).onAppLaunch();

        // 有米 插屏
        SpotManager.getInstance(this).loadSpotAds();

        // 多盟 积分墙
        DMOfferWall.init(this, getResources().getString(R.string.domob_publisher_id));

        // 初始化插屏广告管理
        InterstitialAdsManager.init(this.getResources());
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(20 * 1024 * 1024)  // 20MB
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(500 * 1024 * 1024) // 500 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // 有米 积分墙
        OffersManager.getInstance(this).onAppExit();

        // 有米 插屏
        SpotManager.getInstance(this).unregisterSceenReceiver();
    }

    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
