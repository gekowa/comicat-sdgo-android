package cn.sdgundam.comicatsdgo.post_list;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.data_model.PostList;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchGeneralListListener;

/**
 * Created by xhguo on 10/23/2014.
 */
public abstract class PostListDataSource<T> implements FetchGeneralListListener<T> {
    static final int PAGE_SIZE = 20;

    int pageIndex;
    int gdCategory;

    Context context;

    PostListDataSourceListener pldsListener;

    List<T> postList;
    boolean noMoreData;
    boolean loading;

    boolean justReloaded;

    GDApiService apiService;

    public PostListDataSource(Context context, int gdCategory) {
        this.context = context;
        this.gdCategory = gdCategory;
        postList = new ArrayList<T>();

        apiService = new GDApiService(context);
        apiService.setPostListListener(this);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPldsListener(PostListDataSourceListener pldsListener) {
        this.pldsListener = pldsListener;
    }

    public List<T> getPostList() {
        return postList;
    }

    public int getPostListCount() {
        if (postList != null) {
            return postList.size();
        } else {
            return 0;
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isNoMoreData() {
        return noMoreData;
    }

    public void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
    }

    public void reloadData() {
        noMoreData = false;

        pageIndex = 0;

        loading = true;
        justReloaded = true;

        pldsListener.onBeforeLoadingData(gdCategory);

        fetchList(apiService, gdCategory, PAGE_SIZE, pageIndex);
    }

    public void loadMore() {
        loading = true;

        pageIndex++;

        pldsListener.onBeforeLoadingData(gdCategory);

        fetchList(apiService, gdCategory, PAGE_SIZE, pageIndex);
    }

    private void appendPosts(List<T> posts) {
        this.postList.addAll(posts);

        if (posts.size() < PAGE_SIZE) {
            noMoreData = true;
        }
    }

    protected abstract void fetchList(GDApiService apiService, int gdCategory, int pageSize, int pageIndex);

    // region FetchGeneralListListener members

    @Override
    public void onReceivePostList(PostList<T> list) {
        Log.d("PostListDataSourceAdapter", "" + list.getPostListItems().size());

        loading = false;

        if (justReloaded) {
            justReloaded = false;
            this.postList = new ArrayList<T>();
        }

        appendPosts(list.getPostListItems());

        pldsListener.dataPrepared(gdCategory);
    }

    @Override
    public void onFetchingPostListWithError(Exception e) {
        loading = false;

        pldsListener.dataError(this.gdCategory, e);
    }

    // endregion
}