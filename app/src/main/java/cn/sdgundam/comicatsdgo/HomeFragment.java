package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.sdgundam.comicatsdgo.data_model.CarouselInfo;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.gd_api.FetchHomeInfoAsyncTask;


public class HomeFragment extends Fragment {

    static final String LOG_TAG = HomeFragment.class.getSimpleName();

    static HomeFragment instance = null;
    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    HomeInfo homeInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreated");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.v(LOG_TAG, "onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "onCreateView");

        if (this.homeInfo == null) {
            // load api
            FetchHomeInfoAsyncTask task = new FetchHomeInfoAsyncTask() {
                @Override
                protected void onPostExecute(HomeInfo homeInfo) {
                    super.onPostExecute(homeInfo);
                    if (homeInfo != null) {
                        onReceiveHomeInfo(homeInfo);
                        Log.v(LOG_TAG, "Got 'HomeInfo'" + homeInfo.toString());
                    } else {
                        onFetchingHomeInfoWithError();
                    }
                }
            };
            task.execute();

            // TODO: show loading
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    void onReceiveHomeInfo(HomeInfo homeInfo) {
        this.homeInfo = homeInfo;

        // setup view
        setupView();
    }

    void setupView() {
        CarouselFragment carousel = (CarouselFragment) getFragmentManager().findFragmentById(R.id.carousel);
        carousel.setCarousel(homeInfo.getCarousel());

    }

    void onFetchingHomeInfoWithError() {
        // TODO: Toast?
        // TODO: display network not available fragment
    }
}
