package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.VideoViewActivity;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.post_list.PostListDataSource;
import cn.sdgundam.comicatsdgo.post_list.PostListDataSourceListener;
import cn.sdgundam.comicatsdgo.view.GDCategorySelectionView;
import cn.sdgundam.comicatsdgo.view.NetworkErrorView;
import cn.sdgundam.comicatsdgo.view.VideoGridItemView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.DefaultHeaderTransformer;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class VideoFragment extends Fragment implements
        GDCategorySelectionView.OnGDCategorySelectionListener,
        PostListDataSourceListener,
        OnRefreshListener,
        AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener {

    static final String LOG_TAG = VideoFragment.class.getSimpleName();

    static final Integer ALL_CATEGORIES = 0;
    static final List<Integer> GD_CATEGORIES = Arrays.asList(
            32, 16, 64, 256, 128, 512, 1024, 2048
    );

    int currentGDCategory = 0;
    VideoGridViewDataSource currentDS;
    private Map<Integer, VideoGridViewDataSource> dataSources;
    // private Map<Integer, VideoGridViewAdapter> adapters;
    VideoGridViewAdapter adapter;

    ViewGroup progressViewContainer;
    NetworkErrorView nev;
    PullToRefreshLayout ptrLayout;
    GDCategorySelectionView gdcsv;
    GridView videoListView;

    static VideoFragment instance = null;
    public static VideoFragment getInstance() {
        if (instance == null) {
            instance = new VideoFragment();
        }
        return instance;
    }

    public VideoFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSources = new HashMap<Integer, VideoGridViewDataSource>();

        // init for all categories
        for (int i = 0; i < GD_CATEGORIES.size(); i++) {
            int gdCategory = GD_CATEGORIES.get(i);
            VideoGridViewDataSource ds = new VideoGridViewDataSource(gdCategory);
            ds.setPldsListener(this);
            dataSources.put(gdCategory, ds);
        }

        VideoGridViewDataSource ds = new VideoGridViewDataSource(0);
        ds.setPldsListener(this);
        dataSources.put(0, ds);

        adapter = new VideoGridViewAdapter(getActivity(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_video, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();

        gdcsv = (GDCategorySelectionView)getView().findViewById(R.id.gd_category_sel_view);
        gdcsv.setGDCategories(GD_CATEGORIES);
        gdcsv.setGDCategorySelectionListener(this);

        videoListView = (GridView)getView().findViewById(R.id.video_grid_view);
        videoListView.setNumColumns(2);
        videoListView.setDrawSelectorOnTop(true);
        videoListView.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.video_grid_horizontal_spacing));
        videoListView.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.video_grid_vertical_spacing));
        videoListView.setOnScrollListener(this);
        videoListView.setOnItemClickListener(this);

        ptrLayout = (PullToRefreshLayout)getView().findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .theseChildrenArePullable(R.id.video_grid_view)
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
                VideoFragment.this.currentDS.reloadData();
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
        VideoListItem vli = (VideoListItem)adapterView.getAdapter().getItem(position);
        if (vli != null) {
            Intent intent = new Intent(getActivity(), VideoViewActivity.class);

            Bundle b = new Bundle();
            b.putInt("id", vli.getPostId());
            b.putString("videoHost", vli.getVideoHost());
            b.putString("videoId", vli.getVideoId());
            b.putString("videoId2", vli.getVideoId2());

            intent.putExtras(b);
            getActivity().startActivity(intent);
        }
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
        configureGridView(gdCategory);
    }

    private void configureGridView(int gdCategory) {
        currentGDCategory = gdCategory;

        currentDS = dataSources.get(currentGDCategory);

        if (currentDS.getPostListCount() == 0) {
            showLoading();
            currentDS.reloadData();
        }
        videoListView.setAdapter(adapter);
    }

    // region PostListDataSourceListener

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
        // TODO: don't do anything in onScroll
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

    // endregion

    class VideoGridViewDataSource extends PostListDataSource<VideoListItem> {

        VideoGridViewDataSource(int gdCategory) {
            super(gdCategory);
        }

        @Override
        protected void fetchList(GDApiService apiService, int gdCategory, int pageSize, int pageIndex) {
            apiService.fetchVideoList(gdCategory, pageSize, pageIndex);
        }
    }

    class VideoGridViewAdapter extends BaseAdapter {
        Context context;
        int gdCategory;

        static final int VIEW_TYPE_DATA = 0;
        static final int VIEW_TYPE_ACC = 1;
        static final int VIEW_TYPE_PLACEHOLDER = 2;

        static final int COLUMNS = 2;

        public VideoGridViewAdapter(Context context, int gdCategory) {
            this.context = context;
            this.gdCategory = gdCategory;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) == VIEW_TYPE_DATA;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < currentDS.getPostListCount()) {
                return VIEW_TYPE_DATA;
            } else if (getCount() - currentDS.getPostListCount() == 2) {
                if (position == currentDS.getPostListCount()) {
                    return VIEW_TYPE_PLACEHOLDER;
                } else {
                    return VIEW_TYPE_ACC;
                }
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

            // 2 columns
            if (currentDS.getPostListCount() % 2 == 0) {
                return currentDS.getPostListCount() + 1;
            } else {
                return currentDS.getPostListCount() + 2;
            }
        }


        @Override
        public VideoListItem getItem(int position) {
            List<VideoListItem> posts = currentDS.getPostList();
            return posts.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case VIEW_TYPE_DATA:
                    return getDataRow(position, convertView, parent);
                case VIEW_TYPE_ACC:
                    return getFooterView(convertView, parent);
                case VIEW_TYPE_PLACEHOLDER:
                default:
                    return getPlaceHolderView();
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
            } else if (currentDS.isLoading()) {
                ProgressBar progressBar = new ProgressBar(context);
                container.addView(progressBar);
            }

            return container;
        }

        public View getPlaceHolderView() {
            VideoGridItemView view = new VideoGridItemView(context, null) {
                @Override
                protected void onDraw(Canvas canvas) {
                    // do nothing
                }
            };

            view.setVli(new VideoListItem(0, "", "", "http://localhost", 0, null));
            return view;
        }

        public View getDataRow(int i, View convertView, ViewGroup viewGroup) {
            Log.d(LOG_TAG, "getDataRow " + i);
            VideoGridItemView view = (VideoGridItemView)convertView;
            if (view == null) {
                view = new VideoGridItemView(getActivity(), null);
                Log.d(LOG_TAG, "getDataRow create new" + i);
            }

            VideoListItem item = (VideoListItem) getItem(i);

            view.setVli(item);

            return view;
        }
    }

    private class FullWidthFixedViewLayout extends FrameLayout {
        public FullWidthFixedViewLayout(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int targetWidth = VideoFragment.this.videoListView.getMeasuredWidth()
                    - VideoFragment.this.videoListView.getPaddingLeft()
                    - VideoFragment.this.videoListView.getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(targetWidth,
                    MeasureSpec.getMode(widthMeasureSpec));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}


/* TODOs
* 1. 当没有更多内容显示时, 奇数的情况
* 2. Loading超时的情况
* 3. 初始的Loading界面
* */
