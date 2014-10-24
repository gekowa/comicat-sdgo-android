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
public abstract class PostListDataSourceAdapter<T> extends BaseAdapter implements FetchGeneralListListener<T> {
    static final int PAGE_SIZE = 20;

    static final int VIEW_TYPE_ACTIVITY = 1;
    static final int VIEW_TYPE_ACC = 2;

    protected GDApiService apiService;

    int pageIndex;
    int gdCategory;
    Context context;

    PostListDataSourceListener pldsListener;

    List<T> postList;
    boolean noMoreData;
    boolean loading;



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

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void reloadData() {
        noMoreData = false;

        pageIndex = 0;
        postList = new ArrayList<T>();

        loading = true;
        pldsListener.onBeforeLoadingData(gdCategory);

        fetchList(gdCategory, PAGE_SIZE, pageIndex);
    }

    protected abstract void fetchList(int gdCategory, int pageSize, int pageIndex);

    public void loadMore() {
        loading = true;

        pageIndex++;

        pldsListener.onBeforeLoadingData(gdCategory);

        fetchList(gdCategory, PAGE_SIZE, pageIndex);
    }

    private void appendPosts(List<T> posts) {
        this.postList.addAll(posts);
    }

    // region FetchGeneralListListener members

    @Override
    public void onReceivePostList(PostList<T> list) {
        Log.d("PostListDataSourceAdapter", "" + list.getPostListItems().size());

        loading = false;

        appendPosts(list.getPostListItems());

        pldsListener.dataPrepared(gdCategory);

        notifyDataSetChanged();
    }

    @Override
    public void onFetchingPostListWithError(Exception e) {
        loading = false;
    }

    // endregion

    // region BaseAdapter
//    @Override
//    public boolean isEnabled(int position) {
//        return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

//    @Override
//    public int getItemViewType(int position) {
//        // TODO Auto-generated method stub
//        return (position >= postList.size()) ? VIEW_TYPE_ACC : VIEW_TYPE_ACTIVITY;
//    }

    @Override
    public T getItem(int position) {
//        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ?
//                postList.get(position) : null;
        return postList.get(position);
    }

    @Override
    public int getCount() {
//        if (postList.size() == 0) {
//            return 0;
//        }
//
//        return postList.size() + 1 /* for footer view*/;

        return postList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (getItemViewType(position) == VIEW_TYPE_ACC) {
//            // display the last row
//            return getFooterView(convertView, parent);
//        }

        return getDataRow(position, convertView, parent);
    }

//    public View getFooterView(View convertView, ViewGroup parent) {
//        if (noMoreData) {
//
//            TextView textNoMore = new TextView(context);
//            textNoMore.setHint(context.getResources().getString(R.string.no_more_data));
//            textNoMore.setGravity(Gravity.CENTER);
//            return textNoMore;
//
//        } else if (loading) {
//
//            ProgressBar progressBar = new ProgressBar(context);
//            return progressBar;
//
//        } else {
//            // display nothing
//            return new TextView(context);
//        }
//    }

    public abstract View getDataRow(int position, View convertView, ViewGroup parent);

    // endregion

}
