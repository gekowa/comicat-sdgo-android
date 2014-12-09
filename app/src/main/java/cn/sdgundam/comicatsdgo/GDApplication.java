package cn.sdgundam.comicatsdgo;

import android.app.Application;

import com.squareup.picasso.Picasso;

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

        // for debugging
        Picasso.with(this).setIndicatorsEnabled(true);
    }
}
