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
import cn.sdgundam.comicatsdgo.video.GetYoukuVideoSrcAsyncTask;
import cn.sdgundam.comicatsdgo.view.CarouselView;
import cn.sdgundam.comicatsdgo.view.PostListForHomeView;
import cn.sdgundam.comicatsdgo.view.UnitListForHomeView;
import cn.sdgundam.comicatsdgo.view.VideoGridItemView;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Log.v(LOG_TAG, "onCreated");
    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.d(LOG_TAG, GetYoukuVideoSrcAsyncTask.getFileIDMixString("1234"));
//        Log.d(LOG_TAG, GetYoukuVideoSrcAsyncTask.getFileID("40*49*40*40*16*40*40*64*40*40*59*27*49*50*47*47*6*40*49*5*50*49*40*17*17*63*10*6*50*65*28*47*63*50*64*10*16*63*35*64*17*49*59*35*49*59*5*47*35*5*59*28*28*35*49*10*59*50*64*10*47*47*65*34*49*63*", "5320"));



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
