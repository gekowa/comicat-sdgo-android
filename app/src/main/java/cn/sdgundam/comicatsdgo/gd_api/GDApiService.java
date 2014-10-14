package cn.sdgundam.comicatsdgo.gd_api;

import android.util.Log;

import cn.sdgundam.comicatsdgo.data_model.HomeInfo;

/**
 * Created by xhguo on 10/14/2014.
 */
public abstract class GDApiService {
    GDApiServiceCallbacks callbacks;

    public GDApiServiceCallbacks getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(GDApiServiceCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void fetchHomeInfo(Boolean force) {
        FetchHomeInfoAsyncTask task = new FetchHomeInfoAsyncTask() {
            @Override
            protected void onPostExecute(HomeInfo homeInfo) {
                super.onPostExecute(homeInfo);

                if (homeInfo != null) {
                    onReceiveHomeInfo(homeInfo);
                } else {
                    onFetchingHomeInfoWithError(null);
                }
            }
        };

        task.execute(false);
    }

    protected abstract void onReceiveHomeInfo(HomeInfo homeInfo);
    protected abstract void onFetchingHomeInfoWithError(Exception e);
}
