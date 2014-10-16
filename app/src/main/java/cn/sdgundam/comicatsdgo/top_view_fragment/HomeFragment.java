package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;

import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.extension.SwipeRefreshLayout;
import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.gd_api.FetchHomeInfoAsyncTask;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.view.CarouselView;
import cn.sdgundam.comicatsdgo.view.NetworkErrorView;
import cn.sdgundam.comicatsdgo.view.PostListForHomeView;
import cn.sdgundam.comicatsdgo.view.UnitListForHomeView;
import cn.sdgundam.comicatsdgo.view.VideoGridView;


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
    public void setHomeInfo(HomeInfo homeInfo) {
        this.homeInfo = homeInfo;
    }

    private GDApiService apiService;

    private ViewGroup progressViewContainer;
    private SwipeRefreshLayout swipeLayout;
    private NetworkErrorView nev;

    Date lastSwipeRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // configure API service
        apiService = new GDApiService() {
            @Override
            protected void onReceiveHomeInfo(HomeInfo homeInfo) {
                hideAllLoadings();

                setHomeInfo(homeInfo);
                setupViews();
            }

            @Override
            protected void onFetchingHomeInfoWithError(Exception e) {
                hideAllLoadings();

                // Display "Network Unavailable" view
                if (HomeFragment.this.homeInfo == null) {
                    nev.setVisibility(View.VISIBLE);
                }

                Utility.showNetworkErrorAlertDialog(HomeFragment.this.getActivity(), e);
            }
        };

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
                    } else {
                        setupViews();
                    }
                }
            });
        }

        swipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Boolean shouldForce = lastSwipeRefresh != null && (new Date().getTime() - lastSwipeRefresh.getTime()) / 1000 < 10;
                apiService.fetchHomeInfo(shouldForce);
                if (shouldForce) {
                    lastSwipeRefresh = null;
                } else {
                    lastSwipeRefresh = new Date();
                }
            }
        });
        swipeLayout.setColorScheme(R.color.gundam_1,
                R.color.gundam_2,
                R.color.gundam_3,
                R.color.gundam_4);


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
                HomeFragment.this.refreshHomeInfo();
                nev.setVisibility(View.INVISIBLE);
            }
        });

        Log.v(LOG_TAG, "onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void refreshHomeInfo() {
        // TODO: show loading
        showLoading();

        apiService.fetchHomeInfo(false);
    }

    void setupViews() {
        setupCarousel();
        setupUnitList();
        setupPostList();
        setupVideoList();
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

    void setupVideoList() {
        VideoGridView videoList = (VideoGridView)getView().findViewById(R.id.video_list);
        videoList.setVideos(homeInfo.getVideoList());
    }

    void showLoading() {
        progressViewContainer.setVisibility(View.VISIBLE);
    }

    void hideAllLoadings() {
        progressViewContainer.setVisibility(View.INVISIBLE);
        swipeLayout.setRefreshing(false);
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
