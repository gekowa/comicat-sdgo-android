package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.gd_api.FetchHomeInfoAsyncTask;
import cn.sdgundam.comicatsdgo.view.CarouselView;
import cn.sdgundam.comicatsdgo.view.PostListForHomeView;
import cn.sdgundam.comicatsdgo.view.UnitListForHomeView;


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

        setHasOptionsMenu(true);

        Log.v(LOG_TAG, "onCreated");
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewTreeObserver vto = getView().getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver vtoInner = getView().getViewTreeObserver();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        vtoInner.removeOnGlobalLayoutListener(this);
                    } else {
                        vtoInner.removeGlobalOnLayoutListener(this);
                    }

                    if (HomeFragment.this.homeInfo == null) {
                        refreshHomeInfo();
                        Log.d(LOG_TAG, "refreshHomeInfo");
                    } else {
                        setupViews();
                        Log.d(LOG_TAG, "setupViews");
                    }
                }
            });
        }

        Log.v(LOG_TAG, "onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void refreshHomeInfo() {
        // load api
        FetchHomeInfoAsyncTask task = new FetchHomeInfoAsyncTask() {
            @Override
            protected void onPostExecute(HomeInfo homeInfo) {
                super.onPostExecute(homeInfo);

                if (homeInfo != null) {
                    Log.v(LOG_TAG, "Got 'HomeInfo'" + homeInfo.toString());
                    onReceiveHomeInfo(homeInfo);
                } else {
                    onFetchingHomeInfoWithError();
                }
            }
        };
        task.execute();

        // TODO: show loading
    }

    void onReceiveHomeInfo(HomeInfo homeInfo) {
        this.homeInfo = homeInfo;

        setupViews();
    }

    void setupViews() {
        setupCarousel();
        setupUnitList();
        setupPostList();
    }

    void setupCarousel() {
        int fragmentWidth = getView().getMeasuredWidth();
        int carouselHeight = getResources().getDimensionPixelSize(R.dimen.carousel_height);
        int carouselWidth = getResources().getDimensionPixelSize(R.dimen.carousel_width);
        int aspectCarouselHeight = (int) (fragmentWidth  * (float)carouselHeight  / carouselWidth);

        CarouselView carouselView = (CarouselView) this.getView().findViewById(R.id.carousel);
        carouselView.getLayoutParams().height = aspectCarouselHeight;

        carouselView.setCarousel(homeInfo.getCarousel());
    }

    void setupUnitList() {
        UnitListForHomeView unitList = (UnitListForHomeView) getView().findViewById(R.id.unit_list);
        unitList.setUnits(homeInfo.getUnits());
    }

    void setupPostList() {
        PostListForHomeView postList = (PostListForHomeView)getView().findViewById(R.id.post_list);
        postList.setPosts(homeInfo.getPostList());
    }


    void onFetchingHomeInfoWithError() {
        // TODO: Toast?
        Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_LONG).show();
        // TODO: display network not available fragment
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: load again here.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();


    }
}
