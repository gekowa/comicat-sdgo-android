package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.post_list.PostListDataSource;
import cn.sdgundam.comicatsdgo.post_list.PostListDataSourceListener;
import cn.sdgundam.comicatsdgo.view.GDCategorySelectionView;
import cn.sdgundam.comicatsdgo.view.VideoGridItemView;
import cn.sdgundam.comicatsdgo.view.VideoGridView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.DefaultHeaderTransformer;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class VideoFragment extends Fragment implements
        GDCategorySelectionView.OnGDCategorySelectionListener,
        PostListDataSourceListener,
        OnRefreshListener,
        AbsListView.OnScrollListener {

    static final Integer ALL_CATEGORIES = 0;
    static final List<Integer> GD_CATEGORIES = Arrays.asList(
            32, 16, 64, 256, 128, 512, 1024, 2048
    );

    int currentGDCategory = 0;
    VideoGridViewDataSource currentDS;
    private Map<Integer, VideoGridViewDataSource> dataSources;
    // private Map<Integer, VideoGridViewAdapter> adapters;
    VideoGridViewAdapter adapter;

    PullToRefreshLayout ptrLayout;
    GDCategorySelectionView gdcsv;
    ListView videoListView;

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

        videoListView = (ListView)getView().findViewById(R.id.video_list_view);
        // videoListView.setNumColumns(2);
        videoListView.setDrawSelectorOnTop(true);
//        videoListView.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.video_grid_horizontal_spacing));
//        videoListView.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.video_grid_vertical_spacing));
        videoListView.setOnScrollListener(this);

        switchToGDCategory(0);

        ptrLayout = (PullToRefreshLayout)getView().findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .theseChildrenArePullable(R.id.video_list_view)
                .listener(this)
                .setup(ptrLayout);
        ((DefaultHeaderTransformer)ptrLayout.getHeaderTransformer()).setProgressBarColor(getResources().getColor(R.color.gd_tint_color));
    }

    @Override
    public void onRefreshStarted(View view) {

    }

    @Override
    public void onScroll(AbsListView absListView,
                         int firstVisibleItem,int visibleItemCount,int totalItemCount) {



//        // If it’s still loading, we check to see if the dataset count has
//        // changed, if so we conclude it has finished loading and update the current page
//        // number and total item count.
//        int currentItemCount = currentDSA.getCount();
//        if (loading && (totalItemCount > currentItemCount)) {
//            loading = false;
//            previousTotalItemCount = totalItemCount;
//            currentPage++;
//        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (currentDS != null) {
            if (!currentDS.isLoading() && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 0)) {
                currentDS.loadMore();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

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
    public void dataError(int gdCategory) {

    }

    @Override
    public void noMoreData(int gdCategory) {
        // TODO: don't do anything in onScroll
    }

    @Override
    public void onBeforeLoadingData(int category) {

    }

    private void hideAllLoadings() {

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

        static final int VIEW_TYPE_ACTIVITY = 1;
        static final int VIEW_TYPE_ACC = 2;

        public VideoGridViewAdapter(Context context, int gdCategory) {
            this.context = context;
            this.gdCategory = gdCategory;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position >= getCount()) ? VIEW_TYPE_ACC : VIEW_TYPE_ACTIVITY;
        }

        @Override
        public int getCount() {
            if (currentDS == null) {
                return 0;
            }
            if (currentDS.getPostListCount() == 0) {
                return 0;
            }
            return Math.round((float) (currentDS.getPostListCount()) / 2 + 1) /* for footer view*/;
        }


        @Override
        public VideoListItem getItem(int position) {
            List<VideoListItem> posts = currentDS.getPostList();
            return posts .contains(position) ? posts.get(position) : null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (getItemViewType(position) == VIEW_TYPE_ACC) {
                // display the last row
                return getFooterView(convertView, parent);
            }

            return getDataRow(position, convertView, parent);
        }

        public View getFooterView(View convertView, ViewGroup parent) {
            if (currentDS.isNoMoreData()) {
                TextView textNoMore = new TextView(context);
                textNoMore.setHint(context.getResources().getString(R.string.no_more_data));
                textNoMore.setGravity(Gravity.CENTER);
                return textNoMore;

            } else if (currentDS.isLoading()) {
                ProgressBar progressBar = new ProgressBar(context);
                return progressBar;
            } else {
                // display nothing
                return new ViewStub(context);
            }
        }

        public View getDataRow(int i, View convertView, ViewGroup viewGroup) {
//            VideoGridItemView view = (VideoGridItemView)convertView;
//            if (view == null) {
//                view = new VideoGridItemView(getActivity(), null);
//            }
//
//            VideoListItem item = (VideoListItem) getItem(i);
//            view.setVli(item);
//
//            return view;

            return new TextView(getActivity());
        }
    }
}
