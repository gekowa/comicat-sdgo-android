package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import java.util.Date;

import cn.sdgundam.comicatsdgo.SearchUnitActivity;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchHomeInfoListener;
import cn.sdgundam.comicatsdgo.view.CarouselView;
import cn.sdgundam.comicatsdgo.view.NetworkErrorView;
import cn.sdgundam.comicatsdgo.view.PostListForHomeView;
import cn.sdgundam.comicatsdgo.view.UnitListForHomeView;
import cn.sdgundam.comicatsdgo.view.VideoGridView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.DefaultHeaderTransformer;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class HomeFragment extends Fragment implements
        OnRefreshListener, FetchHomeInfoListener {
    static final String LOG_TAG = HomeFragment.class.getSimpleName();

    static HomeFragment instance = null;
    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    HomeInfo homeInfo;

    private GDApiService apiService;

    private ViewGroup progressViewContainer;
    // private SwipeRefreshLayout swipeLayout;
    private PullToRefreshLayout ptrLayout;
    private NetworkErrorView nev;

    private boolean allViewSetup = false;

    Date lastSwipeRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // configure API service
        apiService = new GDApiService(this.getActivity());
        apiService.setHomeInfoListener(this);

        Log.v(LOG_TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeViews();

        loadHomeInfo();
    }

    private void initializeViews() {
        ptrLayout = (PullToRefreshLayout)getView().findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(ptrLayout);
        ((DefaultHeaderTransformer)ptrLayout.getHeaderTransformer()).setProgressBarColor(getResources().getColor(R.color.gd_tint_color));

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
                HomeFragment.this.loadHomeInfo();
                nev.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("首页");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.v(LOG_TAG, "onPause");

        MobclickAgent.onPageEnd("首页");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        allViewSetup = false;

        Log.v(LOG_TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.v(LOG_TAG, "onDestroy");
    }

    @Override
    public void onRefreshStarted(View view) {
        Boolean shouldForce = lastSwipeRefresh != null && (new Date().getTime() - lastSwipeRefresh.getTime()) / 1000 < 10;
        apiService.fetchHomeInfo(shouldForce);
        if (shouldForce) {
            lastSwipeRefresh = null;
        } else {
            lastSwipeRefresh = new Date();
        }
    }

    @Override
    public void onReceiveHomeInfo(HomeInfo homeInfo) {
        hideAllLoadings();

        this.homeInfo = homeInfo;

        View rootView = getView();
        if (rootView != null) {
            getView().post(new Runnable() {
                @Override
                public void run() {
                    setupViews();
                }
            });
        }
    }

    @Override
    public void onFetchingHomeInfoFail(Exception e) {
        hideAllLoadings();

        // Display "Network Unavailable" view
        if (HomeFragment.this.homeInfo == null) {
            nev.setVisibility(View.VISIBLE);
        }

        Utility.showNetworkErrorAlertDialog(HomeFragment.this.getActivity(), e);
    }

    private void loadHomeInfo() {
        showLoading();

        apiService.fetchHomeInfo(false);
    }

    void setupViews() {
        View containerView = getView();
        if (containerView  != null) {
            setupCarousel(containerView);
            setupUnitList(containerView);
            setupPostList(containerView);
            setupVideoList(containerView);

            allViewSetup = true;
        }
    }

    void setupCarousel(View containerView) {
        int fragmentWidth = containerView.getMeasuredWidth();
        int carouselHeight = getResources().getDimensionPixelSize(R.dimen.carousel_height);
        int carouselWidth = getResources().getDimensionPixelSize(R.dimen.carousel_width);
        int aspectCarouselHeight = (int) (fragmentWidth  * (float)carouselHeight  / carouselWidth);

        CarouselView carouselView = (CarouselView) containerView.findViewById(R.id.carousel);
        carouselView.getLayoutParams().height = aspectCarouselHeight;

        carouselView.setCarousel(homeInfo.getCarousel());
    }

    void setupUnitList(View containerView) {
        UnitListForHomeView unitList = (UnitListForHomeView) containerView.findViewById(R.id.unit_list);
        unitList.setUnits(homeInfo.getUnits());
    }

    void setupPostList(View containerView) {
        PostListForHomeView postList = (PostListForHomeView)containerView.findViewById(R.id.post_list);
        postList.setPosts(homeInfo.getPostList());
    }

    void setupVideoList(View containerView) {
        VideoGridView videoList = (VideoGridView)containerView.findViewById(R.id.video_list);
        videoList.setVideos(homeInfo.getVideoList());
    }

    void showLoading() {
        progressViewContainer.setVisibility(View.VISIBLE);
    }

    void hideAllLoadings() {
        progressViewContainer.setVisibility(View.INVISIBLE);
        // swipeLayout.setRefreshing(false);
        ptrLayout.setRefreshComplete();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: load again here.
        switch (item.getItemId()) {
//            case R.id.menu_item_video_5862: {
//                Intent intent = Utility.makeVideoViewActivityIntent(getActivity(), 5862, "2", "1024609", "");
//                this.getActivity().startActivity(intent);
//                return true;
//            }
//            case R.id.menu_item_video_5002: {
//                Intent intent = Utility.makeVideoViewActivityIntent(getActivity(), 5002, "2", "983675", "");
//                this.getActivity().startActivity(intent);
//                return true;
//            }
//            case R.id.menu_item_video_sample: {
//                Intent intent = Utility.makeVideoViewActivityIntent(getActivity(), 5002, "2", "18720707", "");
//                this.getActivity().startActivity(intent);
//                return true;
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search_unit);
        View searchUnitButton = MenuItemCompat.getActionView(searchMenuItem);
        searchUnitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), SearchUnitActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
