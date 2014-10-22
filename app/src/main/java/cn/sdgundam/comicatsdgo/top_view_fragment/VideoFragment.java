package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.data_model.VideoList;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchVideoListListener;
import cn.sdgundam.comicatsdgo.view.GDCategorySelectionView;
import cn.sdgundam.comicatsdgo.view.VideoGridItemView;

public class VideoFragment extends Fragment implements
        GDCategorySelectionView.OnGDCategorySelectionListener, FetchVideoListListener
{
    int pageIndex = 0;
    int currentGDCategory = 0;
    private Map<Integer, VideoGridViewAdapter> adapters;
    private Map<Integer, VideoList> videoLists;
    private Map<Integer, Integer> indices;

    GDApiService apiService;

    static final int PAGE_SIZE = 20;

    static final List<Integer> GD_CATEGORIES = Arrays.asList(
        32, 16, 64, 256, 128, 512, 1024, 2048
    );

    GDCategorySelectionView gdcsv;

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

        apiService = new GDApiService(this);

        adapters = new HashMap<Integer, VideoGridViewAdapter>();
        videoLists = new HashMap<Integer, VideoList>();

        loadListForCurrentGDCategory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        gdcsv = (GDCategorySelectionView)rootView.findViewById(R.id.gd_category_sel_view);
        gdcsv.setGDCategories(GD_CATEGORIES);
        gdcsv.setGDCategorySelectionListener(this);

        return rootView;
    }

    @Override
    public void onShowAllClicked() {
        this.currentGDCategory = 0;
        pageIndex = 0;
    }

    @Override
    public void onGDCategorySelected(int gdCategory) {
        this.currentGDCategory = gdCategory;
    }

    void loadListForCurrentGDCategory() {
        apiService.fetchVideoList(currentGDCategory, PAGE_SIZE, pageIndex);
    }

    VideoGridViewAdapter getAdapterForCurrentGDCategory() {
        return this.adapters.get(currentGDCategory);
    }

    @Override
    public void onReceiveVideoList(VideoList videoList) {
        if (this.currentGDCategory == videoList.getCategory()) {

        }
    }

    void appendPosts(List<VideoListItem> posts) {

    }

    @Override
    public void onFetchingVideoListWithError(Exception e) {

    }

    class VideoGridViewAdapter extends BaseAdapter {
        Context context;
        int gdCategory;

        public VideoGridViewAdapter(Context context, int gdCategory) {
            this.context = context;
            this.gdCategory = gdCategory;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            VideoGridItemView view = (VideoGridItemView)convertView;
            if (view == null) {
                view = new VideoGridItemView(context, null);
            }

            VideoListItem item = (VideoListItem) getItem(i);
            view.setVli(item);

            return view;
        }
    }
}
