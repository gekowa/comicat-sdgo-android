package cn.sdgundam.comicatsdgo.video;

import android.app.Activity;

import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.Log;

/**
 * Created by xhguo on 11/5/2014.
 */
public class VitamioLibsChecker {
    public static final void checkVitamioLibs(Activity ctx) {
        if (!Vitamio.isInitialized(ctx)) {
            Log.d("VitamioLibsChecker: Initializing Vitamio.");
            Vitamio.initialize(ctx);
            Log.d("VitamioLibsChecker: Initialized Vitamio.");
        }
    }
}
