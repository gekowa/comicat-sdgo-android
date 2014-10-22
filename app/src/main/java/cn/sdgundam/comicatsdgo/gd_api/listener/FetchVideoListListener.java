package cn.sdgundam.comicatsdgo.gd_api.listener;

import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.VideoList;

/**
 * Created by xhguo on 10/14/2014.
 */

public interface FetchVideoListListener {
    void onReceiveVideoList(VideoList videoList);
    void onFetchingVideoListWithError(Exception e);
}