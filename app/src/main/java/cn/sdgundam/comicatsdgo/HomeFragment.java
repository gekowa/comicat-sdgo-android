package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

        setupCarousel();
        setupUnitList();
        setupPostList();
    }


    void setupCarousel() {
        CarouselFragment carousel = (CarouselFragment) getFragmentManager().findFragmentById(R.id.carousel);
        carousel.setCarousel(homeInfo.getCarousel());

        int fragmentWidth = getView().getMeasuredWidth();
        int carouselHeight = getResources().getDimensionPixelSize(R.dimen.carousel_height);
        int carouselWidth = getResources().getDimensionPixelSize(R.dimen.carousel_width);
        int aspectCarouselHeight = (int) (fragmentWidth  * (float)carouselHeight  / carouselWidth);

        View v = this.getView().findViewById(R.id.carousel);
        v.getLayoutParams().height = aspectCarouselHeight;
    }

    void setupUnitList() {
        UnitListType1Fragment unitList = (UnitListType1Fragment) getFragmentManager().findFragmentById(R.id.unit_list);
        unitList.setUnits(homeInfo.getUnits());
    }

    void setupPostList() {
        PostListForHomeFragment postList = (PostListForHomeFragment) getFragmentManager().findFragmentById(R.id.post_list);
        postList.setPosts(homeInfo.getPostList());
    }


    void onFetchingHomeInfoWithError() {
        // TODO: Toast?
        Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_LONG).show();
        // TODO: display network not available fragment
    }


}
