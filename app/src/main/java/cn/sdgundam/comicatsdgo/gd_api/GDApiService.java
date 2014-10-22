package cn.sdgundam.comicatsdgo.gd_api;

import android.util.Log;

import cn.sdgundam.comicatsdgo.data_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.VideoList;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchHomeInfoListener;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchVideoListListener;

/**
 * Created by xhguo on 10/14/2014.
 */
public class GDApiService {
    FetchHomeInfoListener homeInfoListener;
    public GDApiService(FetchHomeInfoListener homeInfoListener) {
        this.homeInfoListener = homeInfoListener;
    }

    public final void fetchHomeInfo(Boolean force) {
        FetchHomeInfoAsyncTask task = new FetchHomeInfoAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<HomeInfo> result) {
                super.onPostExecute(result);

                if (homeInfoListener != null) {
                    if (result.getE() != null) {
                        homeInfoListener.onFetchingHomeInfoWithError(result.getE());
                    } else {
                        HomeInfo homeInfo = result.getWrappingObject();
                        if (homeInfo == null) {
                            homeInfoListener.onFetchingHomeInfoWithError(new Exception("未知错误"));
                        } else {
                            homeInfoListener.onReceiveHomeInfo(result.getWrappingObject());
                        }
                    }
                }


            }
        };

        task.execute();
    }

    FetchVideoListListener videoListListener;
    public GDApiService(FetchVideoListListener videoListListener) {
        this.videoListListener = videoListListener;
    }

    public final void fetchVideoList(int gdCategory, int pageSize, int pageIndex) {
        FetchVideoListAsyncTask task = new FetchVideoListAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<VideoList> result) {
                super.onPostExecute(result);

                if (videoListListener != null) {
                    if (result.getE() != null) {
                        videoListListener.onFetchingVideoListWithError(result.getE());
                    } else {
                        VideoList homeInfo = result.getWrappingObject();
                        if (homeInfo == null) {
                            videoListListener.onFetchingVideoListWithError(new Exception("未知错误"));
                        } else {
                            videoListListener.onReceiveVideoList(result.getWrappingObject());
                        }
                    }
                }
            }
        };
    }
}
