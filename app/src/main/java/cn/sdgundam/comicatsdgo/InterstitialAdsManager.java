package cn.sdgundam.comicatsdgo;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by xhguo on 12/19/2014.
 */
public class InterstitialAdsManager {
    // TODO: counter success and fail

    static final String LOG_TAG = InterstitialAdsManager.class.getSimpleName();

    Resources resources;

    String[] allProviders;

    Dictionary<String, Integer> counterByProvider;   // provider / counter
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


        counterByProvider = new Hashtable<String, Integer>();
        for (int i = 0; i < allProviders.length; i++) {
            counterByProvider.put(allProviders[i], 0);
        }

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
        Log.d(LOG_TAG, "Will displaying Ads: " + provider);
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
}
