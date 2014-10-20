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
import cn.sdgundam.comicatsdgo.view.GDCategorySelectionView;
import cn.sdgundam.comicatsdgo.view.VideoGridItemView;

public class VideoFragment extends Fragment implements GDCategorySelectionView.OnGDCategorySelectionListener {
    int pageIndex = 0;
    int currentGDCategory = 0;
    private Map<Integer, VideoGridViewAdapter> adapters;
    private Map<Integer, VideoList> videoLists;

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

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GDApiService apiService = new GDApiService() {
            @Override
            protected void onReceiveVideoList(VideoList videoList) {
                super.onReceiveVideoList(videoList);
            }

            @Override
            protected void onFetchingVideoListWithError(Exception e) {
                super.onFetchingVideoListWithError(e);
            }
        };

        adapters = new HashMap<Integer, VideoGridViewAdapter>();
        videoLists = new HashMap<Integer, VideoList>();
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
    }

    @Override
    public void onGDCategorySelected(int gdCategory) {
        this.currentGDCategory = gdCategory;
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
