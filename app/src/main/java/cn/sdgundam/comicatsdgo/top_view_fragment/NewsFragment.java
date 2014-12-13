package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdgundam.comicatsdgo.PostViewActivity;
import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.api_model.PostInfo;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.post_list.PostListDataSource;
import cn.sdgundam.comicatsdgo.post_list.PostListDataSourceListener;
import cn.sdgundam.comicatsdgo.view.GDCategorySelectionView;
import cn.sdgundam.comicatsdgo.view.NetworkErrorView;
import cn.sdgundam.comicatsdgo.view.NewsListItemView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.DefaultHeaderTransformer;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class NewsFragment extends Fragment implements
        GDCategorySelectionView.OnGDCategorySelectionListener,
        PostListDataSourceListener,
        OnRefreshListener,
        AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener {

    static final String LOG_TAG = NewsFragment.class.getSimpleName();

    static final String LIST_STATE_KEY = "ListState";
    Parcelable listState = null;

    static final Integer ALL_CATEGORIES = 0;
    static final List<Integer> GD_CATEGORIES = Arrays.asList(
            1, 32, 16, 64, 256, 128, 512, 1024, 2048
    );

    int currentGDCategory = 0;
    NewsListViewDataSource currentDS;
    Map<Integer, NewsListViewDataSource> dataSources;
    NewsListViewAdapter adapter;

    ViewGroup progressViewContainer;
    NetworkErrorView nev;
    PullToRefreshLayout ptrLayout;
    GDCategorySelectionView gdcsv;
    ListView newsListView;

    static NewsFragment instance = null;
    public static NewsFragment getInstance() {
        if (instance == null) {
            instance = new NewsFragment();
        }
        return instance;
    }

    public NewsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);

        dataSources = new HashMap<Integer, NewsListViewDataSource>();

        for (int i = 0; i < GD_CATEGORIES.size(); i++) {
            int gdCategory = GD_CATEGORIES.get(i);
            NewsListViewDataSource ds = new NewsListViewDataSource(gdCategory);
            ds.setPldsListener(this);
            dataSources.put(gdCategory, ds);
        }

        NewsListViewDataSource ds = new NewsListViewDataSource(0);
        ds.setPldsListener(this);
        dataSources.put(0, ds);

        adapter = new NewsListViewAdapter(getActivity());

        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("新闻列表");
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("新闻列表");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeViews();
    }

    private void initializeViews() {
        gdcsv = (GDCategorySelectionView)getView().findViewById(R.id.gd_category_sel_view);
        gdcsv.setGDCategories(GD_CATEGORIES);
        gdcsv.setGDCategorySelectionListener(this);

        newsListView = (ListView)getView().findViewById(R.id.news_list_view);
        newsListView.setDrawSelectorOnTop(true);
        newsListView.setOnScrollListener(this);
        newsListView.setOnItemClickListener(this);

        ptrLayout = (PullToRefreshLayout)getView().findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .theseChildrenArePullable(R.id.news_list_view)
                .listener(this)
                .setup(ptrLayout);
        ((DefaultHeaderTransformer)ptrLayout.getHeaderTransformer()).setProgressBarColor(getResources().getColor(R.color.gd_tint_color));

        progressViewContainer = (ViewGroup)getView().findViewById(R.id.progress_bar_container);
        progressViewContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        nev = (NetworkErrorView)getView().findViewById(R.id.nev);
        nev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsFragment.this.currentDS.reloadData();
                nev.setVisibility(View.INVISIBLE);
            }
        });

        switchToGDCategory(0);
    }

    @Override
    public void onRefreshStarted(View view) {
        if (currentDS != null) {
            currentDS.reloadData();
        } else {
            hideAllLoadings();
        }
    }

    @Override
    public void onScroll(AbsListView absListView,
                         int firstVisibleItem,int visibleItemCount,int totalItemCount) {

        if (currentDS != null) {
            if (!currentDS.isLoading() &&
                    !currentDS.isNoMoreData() &&
                    (totalItemCount - visibleItemCount) <= (firstVisibleItem + 0)) {
                currentDS.loadMore();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        PostInfo postInfo = (PostInfo)adapterView.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), PostViewActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", postInfo.getPostId());

        intent.putExtras(b);
        getActivity().startActivity(intent);
    }

    @Override
    public void onShowAllClicked() {
        switchToGDCategory(0);
    }

    @Override
    public void onGDCategorySelected(int gdCategory) {
        switchToGDCategory(gdCategory);
    }

    private void switchToGDCategory(int gdCategory) {
        this.currentGDCategory = gdCategory;
        configureListView(gdCategory);
    }

    private void configureListView(int gdCategory) {
        currentGDCategory = gdCategory;

        currentDS = dataSources.get(currentGDCategory);

        if (currentDS.getPostListCount() == 0) {
            showLoading();
            currentDS.reloadData();
        }
        newsListView.setAdapter(adapter);

    }

    @Override
    public void dataPrepared(int gdCategory) {
        if (gdCategory == currentGDCategory) {
            hideAllLoadings();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void dataError(int gdCategory, Exception e) {
        hideAllLoadings();

        // Display "Network Unavailable" view
        if (currentDS.getPostListCount() == 0) {
            nev.setVisibility(View.VISIBLE);
        }

        Utility.showNetworkErrorAlertDialog(getActivity(), e);
    }

    @Override
    public void noMoreData(int gdCategory) {

    }

    @Override
    public void onBeforeLoadingData(int category) {

    }

    void showLoading() {
        progressViewContainer.setVisibility(View.VISIBLE);
    }

    private void hideAllLoadings() {
        progressViewContainer.setVisibility(View.INVISIBLE);

        this.ptrLayout.setRefreshComplete();
    }


    class NewsListViewDataSource extends PostListDataSource<PostInfo> {

        NewsListViewDataSource(int gdCategory) {
            super(NewsFragment.this.getActivity(), gdCategory);
        }

        @Override
        protected void fetchList(GDApiService apiService, int gdCategory, int pageSize, int pageIndex) {
            apiService.fetchNewsList(gdCategory, pageSize, pageIndex);
        }
    }

    class NewsListViewAdapter extends BaseAdapter {
        Context context;

        static final int VIEW_TYPE_DATA = 0;
        static final int VIEW_TYPE_ACC = 1;

        public NewsListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) == VIEW_TYPE_DATA;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < currentDS.getPostListCount()) {
                return VIEW_TYPE_DATA;
            } else {
                return VIEW_TYPE_ACC;
            }
        }

        @Override
        public int getCount() {
            if (currentDS == null) {
                return 0;
            }
            if (currentDS.getPostListCount() == 0) {
                return 0;
            }
            return currentDS.getPostListCount() + 1;
        }

        @Override
        public PostInfo getItem(int position) {
            List<PostInfo> posts = currentDS.getPostList();
            return posts.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (getItemViewType(position) == VIEW_TYPE_ACC) {
                return getFooterView(convertView, parent);
            } else {
                return getDataRow(position, convertView, parent);
            }
        }

        public View getFooterView(View convertView, ViewGroup parent) {
            // Log.d(LOG_TAG, "getFooterView convertView:" + convertView + " parent:" + parent);
            FrameLayout container = new FullWidthFixedViewLayout(context);

            if (currentDS.isNoMoreData()) {
                TextView textNoMore = new TextView(context);
                textNoMore.setHint(context.getResources().getString(R.string.no_more_data));
                textNoMore.setGravity(Gravity.CENTER);
                textNoMore.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.no_more_data_bottom_padding));

                container.addView(textNoMore);
            } else {
                ProgressBar progressBar = new ProgressBar(context);
                container.addView(progressBar);
            }

            return container;
        }

        public View getDataRow(int i, View convertView, ViewGroup viewGroup) {
            Log.d(LOG_TAG, "getDataRow " + i);
            NewsListItemView view = (NewsListItemView)convertView;
            if (view == null) {
                view = new NewsListItemView(getActivity(), null);
                Log.d(LOG_TAG, "getDataRow create new" + i);
            }

            PostInfo item = (PostInfo) getItem(i);

            view.setPostInfo(item);

            return view;
        }

    }

    private class FullWidthFixedViewLayout extends FrameLayout {
        public FullWidthFixedViewLayout(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int targetWidth = NewsFragment.this.newsListView.getMeasuredWidth()
                    - NewsFragment.this.newsListView.getPaddingLeft()
                    - NewsFragment.this.newsListView.getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(targetWidth,
                    MeasureSpec.getMode(widthMeasureSpec));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
