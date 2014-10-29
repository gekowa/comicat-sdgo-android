package cn.sdgundam.comicatsdgo.gd_api;

import cn.sdgundam.comicatsdgo.data_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.PostInfo;
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

    FetchGeneralListListener postListListener;
    public GDApiService(FetchGeneralListListener postListListener) {
        this.postListListener = postListListener;
    }

    public final void fetchVideoList(int gdCategory, int pageSize, int pageIndex) {
        FetchVideoListAsyncTask task = new FetchVideoListAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<PostList<VideoListItem>> result) {
                super.onPostExecute(result);

                if (postListListener != null) {
                    if (result.getE() != null) {
                        postListListener.onFetchingPostListWithError(result.getE());
                    } else {
                        PostList<VideoListItem> info = result.getPayload();
                        if (info == null) {
                            postListListener.onFetchingPostListWithError(new Exception("未知错误"));
                        } else {
                            postListListener.onReceivePostList(result.getPayload());
                        }
                    }
                }
            }
        };

        task.execute(gdCategory, pageSize, pageIndex);
    }


    public final void fetchNewsList(int gdCategory, int pageSize, int pageIndex) {
        FetchNewsListAsyncTask task = new FetchNewsListAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<PostList<PostInfo>> result) {
                super.onPostExecute(result);

                if (postListListener != null) {
                    if (result.getE() != null) {
                        postListListener.onFetchingPostListWithError(result.getE());
                    } else {
                        PostList<PostInfo> info = result.getPayload();
                        if (info == null) {
                            postListListener.onFetchingPostListWithError(new Exception("未知错误"));
                        } else {
                            postListListener.onReceivePostList(result.getPayload());
                        }
                    }
                }
            }
        };

        task.execute(gdCategory, pageSize, pageIndex);
    }
}
