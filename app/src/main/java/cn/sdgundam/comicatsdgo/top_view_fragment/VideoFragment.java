package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.view.GDCategorySelectionView;

public class VideoFragment extends Fragment {
    int pageIndex = 0;
    int currentGDCategory = 0;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        gdcsv = (GDCategorySelectionView)rootView.findViewById(R.id.gd_category_sel_view);
        gdcsv.setGDCategories(GD_CATEGORIES);


        return rootView;
    }


}
