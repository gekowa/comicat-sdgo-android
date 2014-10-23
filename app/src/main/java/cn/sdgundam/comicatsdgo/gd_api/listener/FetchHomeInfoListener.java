package cn.sdgundam.comicatsdgo.gd_api.listener;

import cn.sdgundam.comicatsdgo.data_model.HomeInfo;

/**
 * Created by xhguo on 10/14/2014.
 */

public interface FetchHomeInfoListener {
    void onReceiveHomeInfo(HomeInfo homeInfo);
    void onFetchingHomeInfoWithError(Exception e);
}
