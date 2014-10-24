package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.post_list.PostListDataSourceAdapter;
import cn.sdgundam.comicatsdgo.post_list.PostListDataSourceListener;
import cn.sdgundam.comicatsdgo.view.GDCategorySelectionView;
import cn.sdgundam.comicatsdgo.view.VideoGridItemView;
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
    VideoGridViewDSA currentDSA;
    private Map<Integer, VideoGridViewDSA> dataSourceAdapters;
    GDApiService apiService;

    PullToRefreshLayout ptrLayout;
    GDCategorySelectionView gdcsv;
    GridView videoGridView;

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

        dataSourceAdapters = new HashMap<Integer, VideoGridViewDSA>();

        // init for all categories
        for (int i = 0; i < GD_CATEGORIES.size(); i++) {
            int gdCategory = GD_CATEGORIES.get(i);
            VideoGridViewDSA dsa = new VideoGridViewDSA(getActivity(), gdCategory);
            dsa.setPldsListener(this);
            dataSourceAdapters.put(gdCategory, dsa);
        }

        VideoGridViewDSA dsa = new VideoGridViewDSA(getActivity(), 0);
        dsa.setPldsListener(this);
        dataSourceAdapters.put(0, dsa);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        gdcsv = (GDCategorySelectionView)rootView.findViewById(R.id.gd_category_sel_view);
        gdcsv.setGDCategories(GD_CATEGORIES);
        gdcsv.setGDCategorySelectionListener(this);

        videoGridView = (GridView)rootView.findViewById(R.id.video_grid_view);
        videoGridView.setNumColumns(2);
        videoGridView.setDrawSelectorOnTop(true);
        videoGridView.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.video_grid_horizontal_spacing));
        videoGridView.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.video_grid_vertical_spacing));
        videoGridView.setOnScrollListener(this);

        switchToGDCategory(0);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ptrLayout = (PullToRefreshLayout)getView().findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .theseChildrenArePullable(R.id.video_grid_view)
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
        if (currentDSA != null) {
            if (!currentDSA.isLoading() && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 0)) {
                currentDSA.loadMore();
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

        currentDSA = dataSourceAdapters.get(currentGDCategory);
        videoGridView.setAdapter(currentDSA);
        currentDSA.reloadData();
    }

    // region PostListDataSourceListener

    @Override
    public void dataPrepared(int gdCategory) {
        if (gdCategory == currentGDCategory) {
            hideAllLoadings();
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

    class VideoGridViewDSA extends PostListDataSourceAdapter<VideoListItem> {
        public VideoGridViewDSA(Context context, int gdCategory) {
            super(context, gdCategory);
        }

        @Override
        public View getDataRow(int i, View convertView, ViewGroup viewGroup) {
            VideoGridItemView view = (VideoGridItemView)convertView;
            if (view == null) {
                view = new VideoGridItemView(getContext(), null);
            }

            VideoListItem item = (VideoListItem) getItem(i);
            view.setVli(item);

            return view;
        }

        @Override
        protected void fetchList(int gdCategory, int pageSize, int pageIndex) {
            apiService.fetchVideoList(gdCategory, pageSize, pageIndex);
        }
    }
}
