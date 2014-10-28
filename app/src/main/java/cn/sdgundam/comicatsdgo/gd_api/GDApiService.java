package cn.sdgundam.comicatsdgo.gd_api;

import cn.sdgundam.comicatsdgo.data_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.PostList;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchGeneralListListener;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchHomeInfoListener;

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
                        HomeInfo homeInfo = result.getPayload();
                        if (homeInfo == null) {
                            homeInfoListener.onFetchingHomeInfoWithError(new Exception("未知错误"));
                        } else {
                            homeInfoListener.onReceiveHomeInfo(result.getPayload());
                        }
                    }
                }


            }
        };

        task.execute();
    }

    FetchGeneralListListener videoListListener;
    public GDApiService(FetchGeneralListListener videoListListener) {
        this.videoListListener = videoListListener;
    }

    public final void fetchVideoList(int gdCategory, int pageSize, int pageIndex) {
        FetchVideoListAsyncTask task = new FetchVideoListAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<PostList<VideoListItem>> result) {
                super.onPostExecute(result);

                if (videoListListener != null) {
                    if (result.getE() != null) {
                        videoListListener.onFetchingPostListWithError(result.getE());
                    } else {
                        PostList<VideoListItem> info = result.getPayload();
                        if (info == null) {
                            videoListListener.onFetchingPostListWithError(new Exception("未知错误"));
                        } else {
                            videoListListener.onReceivePostList(result.getPayload());
                        }
                    }
                }
            }
        };

        task.execute(gdCategory, pageSize, pageIndex);
    }
}
