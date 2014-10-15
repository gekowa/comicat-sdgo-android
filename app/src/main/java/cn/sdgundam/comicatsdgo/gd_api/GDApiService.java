package cn.sdgundam.comicatsdgo.gd_api;

import android.util.Log;

import cn.sdgundam.comicatsdgo.data_model.HomeInfo;

/**
 * Created by xhguo on 10/14/2014.
 */
public abstract class GDApiService {
    public void fetchHomeInfo(Boolean force) {
        FetchHomeInfoAsyncTask task = new FetchHomeInfoAsyncTask() {
            @Override
            protected void onPostExecute(HomeInfo homeInfo) {
                super.onPostExecute(homeInfo);

                if (homeInfo == null) {
                    onFetchingHomeInfoWithError(new Exception("未知错误"));
                } else if (homeInfo.getE() != null) {
                    onFetchingHomeInfoWithError(homeInfo.getE());
                } else {
                    onReceiveHomeInfo(homeInfo);
                }
            }
        };

        task.execute();
    }

    protected abstract void onReceiveHomeInfo(HomeInfo homeInfo);
    protected abstract void onFetchingHomeInfoWithError(Exception e);
}
