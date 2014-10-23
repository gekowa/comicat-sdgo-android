package cn.sdgundam.comicatsdgo.post_list;

/**
 * Created by xhguo on 10/23/2014.
 */
public interface PostListDataSourceListener {
    void dataPrepared();
    void dataError();
    void noMoreData();
    void onBeforeLoadingData(int category);
}
