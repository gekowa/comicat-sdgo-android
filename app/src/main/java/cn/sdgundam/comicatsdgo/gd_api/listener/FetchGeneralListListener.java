package cn.sdgundam.comicatsdgo.gd_api.listener;

import cn.sdgundam.comicatsdgo.data_model.PostList;

/**
 * Created by xhguo on 10/23/2014.
 */
public interface FetchGeneralListListener<T> {
    void onReceivePostList(PostList<T> list);
    void onFetchingPostListFail(Exception e);
}
