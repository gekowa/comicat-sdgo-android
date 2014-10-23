package cn.sdgundam.comicatsdgo.post_list;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.sdgundam.comicatsdgo.data_model.PostList;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchGeneralListListener;

/**
 * Created by xhguo on 10/23/2014.
 */
public abstract class PostListDataSourceAdapter<T> extends BaseAdapter implements FetchGeneralListListener<T> {
    static final int PAGE_SIZE = 20;

    protected GDApiService apiService;

    int pageIndex;
    int gdCategory;

    Context context;

    PostListDataSourceListener pldsListener;

    List<T> postList;

    boolean noMoreData;

    public PostListDataSourceAdapter(Context context, int gdCategory) {
        this.context = context;
        this.gdCategory = gdCategory;

        apiService = new GDApiService(this);

        postList = new ArrayList<T>();
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

    public Context getContext() {
        return context;
    }

    public void reloadData() {
        noMoreData = false;

        pageIndex = 0;
        postList = new ArrayList<T>();

        pldsListener.onBeforeLoadingData(gdCategory);

        fetchList(gdCategory, PAGE_SIZE, pageIndex);
    }

    protected abstract void fetchList(int gdCategory, int pageSize, int pageIndex);

    public void loadMore() {

    }

    private void appendPosts(List<VideoListItem> posts) {

    }

    // region FetchGeneralListListener members

    @Override
    public void onReceivePostList(PostList<T> list) {
        Log.d("PostListDataSourceAdapter", "" + list.getPostListItems().size());

        postList = list.getPostListItems();

        pldsListener.dataPrepared();
        notifyDataSetChanged();
    }

    @Override
    public void onFetchingPostListWithError(Exception e) {

    }

    // endregion

    // region BaseAdapter
    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public T getItem(int i) {
        return postList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);

    // endregion

}
