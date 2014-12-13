package cn.sdgundam.comicatsdgo;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import net.youmi.android.AdManager;

import cn.sdgundam.comicatsdgo.gd_api.GDApiService;

/**
 * Created by xhguo on 12/9/2014.
 */
public class GDApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        GDApiService apiService = new GDApiService(this);
        apiService.checkOriginUpdate(true);

        if (BuildConfig.DEBUG) {
            Picasso.with(this).setIndicatorsEnabled(true);
        }

        // Log.d("Device Info: ", getDeviceInfo(this));

        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
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
