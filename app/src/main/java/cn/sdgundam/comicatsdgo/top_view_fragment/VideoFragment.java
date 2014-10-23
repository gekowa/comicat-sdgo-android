package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class VideoFragment extends Fragment implements
        GDCategorySelectionView.OnGDCategorySelectionListener,
        PostListDataSourceListener {

    int currentGDCategory = 0;

    private Map<Integer, VideoGridViewDSA> dataSourceAdapters;

    GDApiService apiService;

    static final Integer ALL_CATEGORIES = 0;
    static final List<Integer> GD_CATEGORIES = Arrays.asList(
        32, 16, 64, 256, 128, 512, 1024, 2048
    );

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

        switchToGDCategory(0);

        return rootView;
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
        VideoGridViewDSA dsa = getCurrentDSA(gdCategory);
        currentGDCategory = gdCategory;

        videoGridView.setAdapter(dsa);
        dsa.reloadData();
    }

    public VideoGridViewDSA getCurrentDSA(int gdCategory) {
        return dataSourceAdapters.get(gdCategory);
    }

    // region PostListDataSourceListener

    @Override
    public void dataPrepared() {

    }

    @Override
    public void dataError() {

    }

    @Override
    public void noMoreData() {

    }

    @Override
    public void onBeforeLoadingData(int category) {

    }


    // endregion

    class VideoGridViewDSA extends PostListDataSourceAdapter<VideoListItem> {
        public VideoGridViewDSA(Context context, int gdCategory) {
            super(context, gdCategory);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
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
