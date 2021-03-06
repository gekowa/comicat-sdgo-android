package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

//import net.youmi.android.AdManager;
//import net.youmi.android.banner.AdSize;
//import net.youmi.android.banner.AdView;
//import net.youmi.android.banner.AdViewListener;
//import net.youmi.android.spot.SpotDialogListener;
//import net.youmi.android.spot.SpotManager;

import java.util.Date;
import java.util.List;

import cn.sdgundam.comicatsdgo.api_model.UnitInfo;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchUnitInfoListener;
import cn.sdgundam.comicatsdgo.service.TrackingService;
import cn.sdgundam.comicatsdgo.view.NetworkErrorView;
import cn.sdgundam.comicatsdgo.view.UnitBasicDataView;
import cn.sdgundam.comicatsdgo.view.UnitDetailView;
import cn.sdgundam.comicatsdgo.view.UnitMixPopupView;
import cn.sdgundam.comicatsdgo.view.UnitMixView;
import cn.sdgundam.comicatsdgo.view.UnitSkillsView;
import cn.sdgundam.comicatsdgo.view.UnitWeaponsView;
import cn.sdgundam.comicatsdgo.view.VideoGridView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.DefaultHeaderTransformer;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class UnitViewActivity extends Activity implements
        FetchUnitInfoListener,
        OnRefreshListener,
        UnitMixView.UnitMixEventListener,
        UnitMixPopupView.UMMEventListener,
        VideoGridView.OnVideoClickListener {

    private static final String LOG_TAG = UnitViewActivity.class.getSimpleName();

    private static final String TAB_INDEX_KEY = "TabIndex";
    private static final String SHOWING_UNIT_MIX = "ShowingUnitMix";
    private static final String SHOWING_UNIT_MIX_CN = "ShowingUnitMixCN";

    private Bundle myState;

    private boolean isRestarted;

    private ScrollView rootScrollView;
    private UnitBasicDataView basicDataView;
//    private ViewPager unitDetailViewPager;
//    private TabPageIndicator pageIndicator;
    private ViewGroup progressViewContainer;

    private TabHost tabHost;

    private ViewGroup unitMixContainer, unitMixContainerCN;
    private View popupMask;
    private Animation popupShowAnimation, popupHideAnimation;
    private UnitMixPopupView mixPopupView, mixPopupViewCN;

    private GDApiService apiService;
    private PullToRefreshLayout ptrLayout;
    private NetworkErrorView nev;

    private ViewGroup bannerContainer;

    private String unitId;
    private UnitInfo unitInfo;

    private boolean isShowingUnitMix = false, isShowingUnitMixCN = false;

    private Date lastSwipeRefresh;

    UMSocialService umSocialService = UMServiceFactory.getUMSocialService("com.umeng.share");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize(getIntent());

        new TrackingService(this).markUnitViewed(unitId);

        apiService = new GDApiService(this);
        apiService.setUnitInfoListener(this);

        setContentView(R.layout.activity_unit_view);
        initializeView();

        loadAds();

        loadUnitInfo();
    }


    void initialize(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            unitId = extras.getString("id");
        }

        if (unitId == null  || unitId.length() == 0) {
            Uri uri = intent.getData();
            if (uri.getHost().equals(getResources().getString(R.string.unit_view_host_name))) {
                List<String> params = uri.getPathSegments();
                unitId = params.get(0);
            }
        }

        if (unitId == null || unitId.length() == 0) {
            //unitId = "24713"; // 飞翼高达凤凰
            // unitId = "10214";    // 高达试作2号机/高达试作1号机全方位推进型玉兰
            //unitId = "10021";    // 石斛兰
//        unitId= "11046";    // 扎古II (指挥官专用) Mission多
//        unitId = "15006";   // 105短剑 扭蛋多, Quest多
            // unitId = "15002";   // 红异端
            // unitId = "10042";   // 高达试作2号机
            unitId = "10002";   // 全装甲高达
            // unitId = "88888";
        }
    }

    private void initializeView() {
        rootScrollView = (ScrollView)findViewById(R.id.root_scroll_view);

        basicDataView = (UnitBasicDataView)findViewById(R.id.unit_basic_data_view);

        ptrLayout = (PullToRefreshLayout)findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(ptrLayout);
        ((DefaultHeaderTransformer)ptrLayout.getHeaderTransformer()).setProgressBarColor(getResources().getColor(R.color.gd_tint_color));


        progressViewContainer = (ViewGroup)findViewById(R.id.progress_bar_container);
        progressViewContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        nev = (NetworkErrorView)findViewById(R.id.nev);
        nev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnitViewActivity.this.loadUnitInfo();
                nev.setVisibility(View.INVISIBLE);
            }
        });

        tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup();

        unitMixContainer = (ViewGroup)findViewById(R.id.unit_mix_container);
        unitMixContainerCN = (ViewGroup)findViewById(R.id.unit_mix_container_cn);
        popupMask = findViewById(R.id.popup_mask);

        popupShowAnimation = AnimationUtils.loadAnimation(UnitViewActivity.this, R.anim.popup_show);
        popupHideAnimation = AnimationUtils.loadAnimation(UnitViewActivity.this, R.anim.popup_hide);

        mixPopupView = (UnitMixPopupView)findViewById(R.id.unit_mix_popup_view);
        mixPopupViewCN = (UnitMixPopupView)findViewById(R.id.unit_mix_popup_view_cn);

        mixPopupView.setUMMEventListener(this);
        mixPopupViewCN.setUMMEventListener(this);

        bannerContainer = (RelativeLayout)findViewById(R.id.banner_container);
    }

    void loadAds() {
        InterstitialAdsManager.getInstance().displayNextAds(this);
    }

//    @Override
//    protected void onStart() {
//        Log.d(LOG_TAG, "onStart");
//
//        super.onStart();
//    }

    @Override
    protected void onRestart() {
        super.onRestart();

        isRestarted = true;
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();

        MobclickAgent.onPageStart("机体详细");
        MobclickAgent.onResume(this);

        if (myState != null) {
            tabHost.setCurrentTab(myState.getInt(TAB_INDEX_KEY));

            if (myState.getBoolean(SHOWING_UNIT_MIX)) {
                showUnitMixPopup();
            } else if (myState.getBoolean(SHOWING_UNIT_MIX_CN)) {
                showUnitMixPopupCN();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("机体详细");
        MobclickAgent.onPause(this);

        myState = new Bundle();
        myState.putInt(TAB_INDEX_KEY, tabHost.getCurrentTab());
        myState.putBoolean(SHOWING_UNIT_MIX, isShowingUnitMix);
        myState.putBoolean(SHOWING_UNIT_MIX_CN, isShowingUnitMixCN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.unit_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_share: {
                String shareContent = String.format("我正在用漫猫SD敢达App查看\"%s\"详细资料和最新视频", unitInfo.getModelName());

                // 微信
                SharingUtility.setupWeixinShare(this, umSocialService,
                        getResources().getString(R.string.app_name),
                        shareContent,
                        "http://www.sdgundam.cn/pages/app/android-landing-page.html",
                        "");
                // QQ
                SharingUtility.setupQQShare(this, umSocialService,
                        getResources().getString(R.string.app_name),
                        shareContent,
                        "http://www.sdgundam.cn/pages/app/android-landing-page.html",
                        "");

                SharingUtility.configureListener(UnitViewActivity.this, umSocialService);

                umSocialService.openShare(this, false);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    protected void onStop() {
//        super.onStop();
//
//        Log.d(LOG_TAG, "onStop");
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState");

        super.onSaveInstanceState(outState);

//         outState.putInt(TAB_INDEX_KEY, tabHost.getCurrentTab());
    }


    void loadUnitInfo() {
        apiService.fetchUnitInfo(unitId, false);
    }


    public void onRefreshStarted(View view) {
        // TODO: show loading
        Boolean shouldForce = lastSwipeRefresh != null && (new Date().getTime() - lastSwipeRefresh.getTime()) / 1000 < 10;
        apiService.fetchUnitInfo(unitId, shouldForce);
        if (shouldForce) {
            lastSwipeRefresh = null;
        } else {
            lastSwipeRefresh = new Date();
        }
    }

    @Override
    public void onReceiveUnitInfo(UnitInfo unitInfo) {
        hideAllLoadings();

        this.unitInfo = unitInfo;

        configureBasicDataView();
        basicDataView.playAnimations();

        configureTabs();

        if (!isRestarted) {
            rootScrollView.post(new Runnable() {
                @Override
                public void run() {
                    rootScrollView.fullScroll(View.FOCUSABLES_TOUCH_MODE);
                }
            });
        }

//        if (myState != null) {
//            // rootScrollView.post();
//
//        }
    }

    @Override
    public void OnShowUnitMixInfo() {
        showUnitMixPopup();
    }

    @Override
    public void OnShowUnitMixInfoCN() {
        showUnitMixPopupCN();
    }

    private void configureBasicDataView() {
        basicDataView.setModelName(unitInfo.getModelName());
        basicDataView.setUnitId(unitInfo.getUnitId());
        basicDataView.setRank(unitInfo.getRank());
        basicDataView.setAttackValue(unitInfo.getAttackG());
        basicDataView.setDefenseValue(unitInfo.getDefenseG());
        basicDataView.setMobilityValue(unitInfo.getMobilityG());
        basicDataView.setControlValue(unitInfo.getControlG());
        basicDataView.setSum3DValue(unitInfo.getSum3D());
        basicDataView.setSum4DValue(unitInfo.getSum4D());
    }

    private void configureTabs() {
        tabHost.clearAllTabs();

        // add 4 tabs
        createUnitWeaponTab();
        createUnitSkillTab();
        createUnitDetailTab();
        createUnitRelatedVideoTab();

//        tabHost.setFocusableInTouchMode(false);

//        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//                tabHost.clearFocus();
//            }
//        });

        // tabHost.setOnTabChangedListener(new AnimatedTabHostListener(this, tabHost));
    }

    private void createUnitWeaponTab() {
        TabHost.TabSpec spec = tabHost.newTabSpec("weapons");
        spec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                UnitWeaponsView view = new UnitWeaponsView(UnitViewActivity.this, null);
                view.setUnitInfo(unitInfo);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                view.setLayoutParams(layoutParams);
                view.setPadding(getResources().getDimensionPixelSize(R.dimen.unit_view_detail_padding),
                        0, getResources().getDimensionPixelSize(R.dimen.unit_view_detail_padding), 0);
                view.setFocusable(false);
                return view;
            }
        });

        spec.setIndicator(getTabIndicator(getResources().getString(R.string.unit_view_tab_title_weapons), null));
        tabHost.addTab(spec);
    }

    private void createUnitSkillTab() {
        TabHost.TabSpec spec = tabHost.newTabSpec("skills");
        spec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                UnitSkillsView view = new UnitSkillsView(UnitViewActivity.this, null);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                view.setLayoutParams(layoutParams);
                view.setPadding(getResources().getDimensionPixelSize(R.dimen.unit_view_detail_padding),
                        0, getResources().getDimensionPixelSize(R.dimen.unit_view_detail_padding), 0);
                view.setUnitInfo(unitInfo);
                view.setFocusable(false);
                return view;
            }
        });

        spec.setIndicator(getTabIndicator(getResources().getString(R.string.unit_view_tab_title_skills), null));
        tabHost.addTab(spec);
    }

    private void createUnitDetailTab() {
        TabHost.TabSpec spec = tabHost.newTabSpec("detail");
        spec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                UnitDetailView view = new UnitDetailView(UnitViewActivity.this, null);
                view.setUnitMixEventListener(UnitViewActivity.this);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                view.setLayoutParams(layoutParams);
                view.setPadding(getResources().getDimensionPixelSize(R.dimen.unit_view_detail_padding),
                        0, getResources().getDimensionPixelSize(R.dimen.unit_view_detail_padding), 0);

                view.setUnitInfo(unitInfo);
                view.setFocusable(false);
                return view;
            }
        });

        spec.setIndicator(getTabIndicator(getResources().getString(R.string.unit_view_tab_title_details), null));
        tabHost.addTab(spec);
    }

    private void createUnitRelatedVideoTab() {
        TabHost.TabSpec spec = tabHost.newTabSpec("video");
        spec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                if (unitInfo.getVideoList() != null && unitInfo.getVideoList().length > 0) {
                    final VideoGridView grid = new VideoGridView(UnitViewActivity.this, null);
                    grid.setLayoutParams(
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    grid.setScrollContainer(false);
                    grid.setVerticalScrollBarEnabled(false);
                    grid.setFocusable(false);
                    grid.post(new Runnable() {
                        @Override
                        public void run() {
                            grid.setVideos(unitInfo.getVideoList());
                        }
                    });
                    grid.setOnVideoClickListener(UnitViewActivity.this);
                    return grid;
                } else {
                    View noVideoNotice = LayoutInflater.from(UnitViewActivity.this).inflate(R.layout.view_no_related_video, null, false);
                    noVideoNotice.setLayoutParams(
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    return noVideoNotice;
                }

            }
        });

        String videoCaption = getResources().getString(R.string.unit_related_video);
        int videoCount = unitInfo.getVideoList().length;
        String videoCountBadge = null;
        if (videoCount > 0) {
            if (videoCount <= 9) {
                videoCountBadge = String.valueOf(videoCount);
            } else if (videoCount <= 99) {
                videoCountBadge = String.valueOf(videoCount);
            } else {
                videoCountBadge = "N";
            }
        }
        spec.setIndicator(getTabIndicator(videoCaption, videoCountBadge));
        tabHost.addTab(spec);
    }

    @Override
    public void onItemClick(int postId, String videoHost, String videoId, String videoId2) {
        Intent intent = Utility.makeVideoViewActivityIntent(this, postId, videoHost, videoId, videoId2);
        intent.putExtra("fromUnitId", unitId);
        startActivity(intent);
    }

    private View getTabIndicator(String caption, String badgeText) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_unit_tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(caption);

        if (badgeText != null && badgeText.length() > 0) {
            TextView badge = (TextView)view.findViewById(R.id.badge_text_view);
            badge.setText(badgeText);
            badge.setVisibility(View.VISIBLE);
        }

        return view;
    }


    @Override
    public void onFetchingUnitInfoFail(Exception e) {
        hideAllLoadings();

        // Display "Network Unavailable" view
        if (unitInfo == null) {
            nev.setVisibility(View.VISIBLE);
        }

        Utility.showNetworkErrorAlertDialog(this, e);
    }

    void showLoading() {
        progressViewContainer.setVisibility(View.VISIBLE);
    }

    void hideAllLoadings() {
        progressViewContainer.setVisibility(View.INVISIBLE);
        // swipeLayout.setRefreshing(false);
        ptrLayout.setRefreshComplete();
    }

    void showUnitMixPopup() {
        popupMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideUnitMixPopup();
            }
        });
        popupMask.setVisibility(View.VISIBLE);

        if (!mixPopupView.isConfigured()) {
            mixPopupView.setUnitMixInfo(unitInfo.getMixingKeyUnit(), unitInfo.getMixingMaterialUnits());
        }

        unitMixContainer.startAnimation(popupShowAnimation);
        unitMixContainer.setVisibility(View.VISIBLE);

        isShowingUnitMix = true;
    }

    void hideUnitMixPopup() {
        unitMixContainer.startAnimation(popupHideAnimation);
        unitMixContainer.setVisibility(View.INVISIBLE);

        popupMask.setVisibility(View.INVISIBLE);
        popupMask.setOnClickListener(null);

        isShowingUnitMix = false;
    }

    void showUnitMixPopupCN() {
        popupMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideUnitMixPopupCN();
            }
        });
        popupMask.setVisibility(View.VISIBLE);

        if (!mixPopupViewCN.isConfigured()) {
            mixPopupViewCN.setUnitMixInfo(unitInfo.getMixingKeyUnitCN(), unitInfo.getMixingMaterialUnitsCN());
        }

        unitMixContainerCN.startAnimation(popupShowAnimation);
        unitMixContainerCN.setVisibility(View.VISIBLE);

        isShowingUnitMixCN = true;
    }

    void hideUnitMixPopupCN() {
        unitMixContainerCN.startAnimation(popupHideAnimation);
        unitMixContainerCN.setVisibility(View.INVISIBLE);

        popupMask.setVisibility(View.INVISIBLE);
        popupMask.setOnClickListener(null);

        isShowingUnitMixCN = false;
    }

    @Override
    public void onUMMSelected(String unitId) {
        // fire intent
        Intent intent = Utility.makeUnitViewActivityIntent(this, unitId);
        startActivity(intent);
    }

    public class AnimatedTabHostListener implements TabHost.OnTabChangeListener {

        private static final int ANIMATION_TIME = 240;
        private TabHost tabHost;
        private View previousView;
        private View currentView;
        private GestureDetector gestureDetector;
        private int currentTab;

        /**
         * Constructor that takes the TabHost as a parameter and sets previousView to the currentView at instantiation
         *
         * @param context
         * @param tabHost
         */
        public AnimatedTabHostListener(Context context, TabHost tabHost)
        {
            this.tabHost = tabHost;
            this.previousView = tabHost.getCurrentView();
            gestureDetector = new GestureDetector(context, new MyGestureDetector());
            tabHost.setOnTouchListener(new View.OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (gestureDetector.onTouchEvent(event))
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
            });
        }

        /**
         * When tabs change we fetch the current view that we are animating to and animate it and the previous view in the
         * appropriate directions.
         */
        @Override
        public void onTabChanged(String tabId)
        {

            currentView = tabHost.getCurrentView();
            if (tabHost.getCurrentTab() > currentTab)
            {
                previousView.setAnimation(outToLeftAnimation());
                currentView.setAnimation(inFromRightAnimation());
            }
            else
            {
                previousView.setAnimation(outToRightAnimation());
                currentView.setAnimation(inFromLeftAnimation());
            }
            previousView = currentView;
            currentTab = tabHost.getCurrentTab();

        }

        /**
         * Custom animation that animates in from right
         *
         * @return Animation the Animation object
         */
        private Animation inFromRightAnimation()
        {
            Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    0.0f);
            return setProperties(inFromRight);
        }

        /**
         * Custom animation that animates out to the right
         *
         * @return Animation the Animation object
         */
        private Animation outToRightAnimation()
        {
            Animation outToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            return setProperties(outToRight);
        }

        /**
         * Custom animation that animates in from left
         *
         * @return Animation the Animation object
         */
        private Animation inFromLeftAnimation()
        {
            Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    0.0f);
            return setProperties(inFromLeft);
        }

        /**
         * Custom animation that animates out to the left
         *
         * @return Animation the Animation object
         */
        private Animation outToLeftAnimation()
        {
            Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            return setProperties(outtoLeft);
        }

        /**
         * Helper method that sets some common properties
         *
         * @param animation
         *            the animation to give common properties
         * @return the animation with common properties
         */
        private Animation setProperties(Animation animation)
        {
            animation.setDuration(ANIMATION_TIME);
            animation.setInterpolator(new AccelerateInterpolator());
            return animation;
        }

        /**
         * A gesture listener that listens for a left or right swipe and uses the swip gesture to navigate a TabHost that
         * uses an AnimatedTabHost listener.
         *
         * @author Daniel Kvist
         *
         */
        class MyGestureDetector extends GestureDetector.SimpleOnGestureListener
        {
            private static final int SWIPE_MIN_DISTANCE = 120;
            private static final int SWIPE_MAX_OFF_PATH = 250;
            private static final int SWIPE_THRESHOLD_VELOCITY = 200;
            private int maxTabs;

            /**
             * An empty constructor that uses the tabhosts content view to decide how many tabs there are.
             */
            public MyGestureDetector()
            {
                maxTabs = tabHost.getTabContentView().getChildCount();
            }

            /**
             * Listens for the onFling event and performs some calculations between the touch down point and the touch up
             * point. It then uses that information to calculate if the swipe was long enough. It also uses the swiping
             * velocity to decide if it was a "true" swipe or just some random touching.
             */
            @Override
            public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY)
            {
                int newTab = 0;
                if (Math.abs(event1.getY() - event2.getY()) <= SWIPE_MAX_OFF_PATH)
                {
                    return false;
                }
                if (event1.getX() - event2.getX() >= SWIPE_MIN_DISTANCE && Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY)
                {
                    // Swipe right to left
                    newTab = currentTab + 1;
                }
                else if (event2.getX() - event1.getX() >= SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY)
                {
                    // Swipe left to right
                    newTab = currentTab - 1;
                }
                if (newTab == 0 || newTab <= (maxTabs - 1))
                {
                    return false;
                }
                tabHost.setCurrentTab(newTab);
                return super.onFling(event1, event2, velocityX, velocityY);
            }
        }
    }

    class UnitViewPlaceHolder extends View {
        String text;

        UnitViewPlaceHolder(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 200);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint solidRed = new Paint();
            solidRed.setColor(Color.RED);

            TextPaint yellowText = new TextPaint();
            yellowText.setColor(Color.YELLOW);
            yellowText.setTextSize(48);
            // solidRed.setColor(COLORS[(int)(COLORS.length * Math.random())]);

            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), solidRed);
            canvas.drawText(text, 100, 100, yellowText);
        }
    }
}
