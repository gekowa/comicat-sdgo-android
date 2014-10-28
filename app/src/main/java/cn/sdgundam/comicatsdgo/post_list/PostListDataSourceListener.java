package cn.sdgundam.comicatsdgo.post_list;

/**
 * Created by xhguo on 10/23/2014.
 */
public interface PostListDataSourceListener {
    void dataPrepared(int category);
    void dataError(int category, Exception e);
    void noMoreData(int category);
    void onBeforeLoadingData(int category);
}
