package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.Hashtable;

import cn.domob.android.ads.InterstitialAd;
import cn.domob.android.ads.InterstitialAdListener;

/**
 * Created by xhguo on 12/19/2014.
 */
public class InterstitialAdsManager {
    // TODO: counter success and fail

    static final String LOG_TAG = InterstitialAdsManager.class.getSimpleName();

    Resources resources;

    String[] allProviders;

//    Dictionary<String, Integer> counterByProvider;   // provider / counter
    Dictionary<String, Integer> counterByTag;
//     Dictionary<String, Integer> weightTable;

    int[] weightArray;

    static InterstitialAdsManager instance;

    public static void init(Resources resources) {
        instance = new InterstitialAdsManager(resources);
    }

    public static InterstitialAdsManager getInstance() {
        return instance;
    }

    private InterstitialAdsManager(Resources resources) {
        this.resources = resources;

        initInternal();
    }

    private void initInternal() {
        allProviders = resources.getStringArray(R.array.interstitial_ads_providers);

//        counterByProvider = new Hashtable<String, Integer>();
//        for (int i = 0; i < allProviders.length; i++) {
//            counterByProvider.put(allProviders[i], 0);
//        }

        resetWeightArray();

        counterByTag = new Hashtable<String, Integer>();
    }

    void resetWeightArray() {
        weightArray = resources.getIntArray(R.array.interstitial_ads_provider_weight);
    }

    public void displayNextAds(Context context) {
        String tag = context.getClass().getSimpleName();
        int freq = resources.getInteger(resources.getIdentifier("cn.sdgundam.comicatsdgo:integer/interstitial_ads_freq_" + tag, null, null));
        Integer num = counterByTag.get(tag);
        if (num == null) {
            num = 1;
        } else {
            num++;
        }

        if (num >= freq) {
            // should display!!
            displayAdsByWeight(context);

            num = 0;
        }

        counterByTag.put(tag, num);
    }

    private void displayAdsByWeight(Context context) {
        int picked = pick();
        if (picked >= 0 && picked < allProviders.length) {
            String provider = allProviders[picked];
            displayAdsOfProvider(context, provider);
        }
    }

    private void displayAdsOfProvider(Context context, String provider) {
        try {
            Method method = this.getClass().getDeclaredMethod("displayAdsOf" + provider, Context.class);
            method.setAccessible(true);
            method.invoke(this, context);
        }
        catch (NoSuchMethodException e) {
            System.out.println(e.toString());
        }
        catch (InvocationTargetException e) {
            System.out.println(e.toString());
        }
        catch (IllegalAccessException e) {
            System.out.println(e.toString());
        }
    }

    private int pick() {
        int sumWeight = 0;
        for (int i = 0; i < weightArray.length; i++) {
            sumWeight += weightArray[i];
        }

        if (sumWeight == 0) {
            resetWeightArray();
            return pick();
        }

        int toss = (int)((double)sumWeight * Math.random());
        int inc = 0;
        for (int i = 0; i < weightArray.length; i++) {
            inc += weightArray[i];
            if (inc > toss) {
                // shot!
                weightArray[i]--;
                return i;
            }
        }
        return -1;
    }

    private void displayAdsOfGDT(Context context) {
        final com.qq.e.ads.InterstitialAd ads  = new com.qq.e.ads.InterstitialAd((Activity)context,
                context.getResources().getString(R.string.gdt_app_id),
                context.getResources().getString(R.string.gdt_interstitial_ads_id));

        ads.setAdListener(new com.qq.e.ads.InterstitialAdListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onBack() {

            }

            @Override
            public void onAdReceive() {
                ads.show();
            }

            @Override
            public void onExposure() {

            }

            @Override
            public void onClicked() {

            }
        });

        ads.loadAd();
    }

    private void displayAdsOfYoumi(Context context) {
        AdManager.getInstance(context).init(resources.getString(R.string.youmi_app_id), resources.getString(R.string.youmi_app_secret), false);

        // 有米 插屏
        SpotManager.getInstance(context).showSpotAds(context, new SpotDialogListener() {
            @Override
            public void onShowSuccess() {
                Log.i("Youmi", "onShowSuccess");
            }

            @Override
            public void onShowFailed() {
                Log.i("Youmi", "onShowFailed");
            }

            @Override
            public void onSpotClosed() {
                Log.e("Youmi", "closed");
            }
        });
    }

    private void displayAdsOfDomob(final Context context) {
        final InterstitialAd ads = new InterstitialAd((Activity)context,
                context.getResources().getString(R.string.domob_publisher_id),
                context.getResources().getString(R.string.domob_interstitial_ads_id));


        ads.setInterstitialAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialAdReady() {
                ads.showInterstitialAd(context);
            }

            @Override
            public void onInterstitialAdFailed(cn.domob.android.ads.AdManager.ErrorCode errorCode) {

            }

            @Override
            public void onInterstitialAdPresent() {

            }

            @Override
            public void onInterstitialAdDismiss() {
                // ads.loadInterstitialAd();
            }

            @Override
            public void onLandingPageOpen() {

            }

            @Override
            public void onLandingPageClose() {

            }

            @Override
            public void onInterstitialAdLeaveApplication() {

            }

            @Override
            public void onInterstitialAdClicked(InterstitialAd interstitialAd) {

            }
        });

        ads.loadInterstitialAd();
//
//        if (ads.isInterstitialAdReady()) {
//            ads.showInterstitialAd(context);
//        }
    }
}
