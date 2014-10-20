package cn.sdgundam.comicatsdgo.gd_api;

import android.util.Log;

import cn.sdgundam.comicatsdgo.data_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.VideoList;

/**
 * Created by xhguo on 10/14/2014.
 */
public abstract class GDApiService {
    public final void fetchHomeInfo(Boolean force) {
        FetchHomeInfoAsyncTask task = new FetchHomeInfoAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<HomeInfo> result) {
                super.onPostExecute(result);

                if (result.getE() != null) {
                    onFetchingHomeInfoWithError(result.getE());
                } else {
                    HomeInfo homeInfo = result.getWrappingObject();
                    if (homeInfo == null) {
                        onFetchingHomeInfoWithError(new Exception("未知错误"));
                    } else {
                        onReceiveHomeInfo(result.getWrappingObject());
                    }
                }
            }
        };

        task.execute();
    }

    public final void fetchVideoList(int gdCategory, int pageSize, int pageIndex) {
        FetchVideoListAsyncTask task = new FetchVideoListAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<VideoList> result) {
                super.onPostExecute(result);

                if (result.getE() != null) {
                    onFetchingHomeInfoWithError(result.getE());
                } else {
                    VideoList homeInfo = result.getWrappingObject();
                    if (homeInfo == null) {
                        onFetchingVideoListWithError(new Exception("未知错误"));
                    } else {
                        onReceiveVideoList(result.getWrappingObject());
                    }
                }
            }
        };
    }

    protected void onReceiveHomeInfo(HomeInfo homeInfo) {}
    protected void onFetchingHomeInfoWithError(Exception e) {}

    protected void onReceiveVideoList(VideoList videoList) {}
    protected void onFetchingVideoListWithError(Exception e) {}
}
