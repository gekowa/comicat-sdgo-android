package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.Date;

import cn.sdgundam.comicatsdgo.data_model.UnitInfo;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchUnitInfoListener;
import cn.sdgundam.comicatsdgo.view.NetworkErrorView;
import cn.sdgundam.comicatsdgo.view.UnitBasicDataView;
import cn.sdgundam.comicatsdgo.view.UnitWeaponsView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.DefaultHeaderTransformer;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class UnitViewActivity extends Activity implements
        FetchUnitInfoListener,
        OnRefreshListener {

    private static final String LOG_TAG = UnitViewActivity.class.getSimpleName();

    private UnitBasicDataView basicDataView;
//    private ViewPager unitDetailViewPager;
//    private TabPageIndicator pageIndicator;
    private ViewGroup progressViewContainer;

    private TabHost tabHost;

//    private UnitViewDetailsPagerAdapter adapter;

    private GDApiService apiService;
    private PullToRefreshLayout ptrLayout;
    private NetworkErrorView nev;

    // details view cache
//    private View unitWeaponView;
//    private View unitSkillView;
//    private View unitDetailView;
//    private View relatedVideoView;

    private String unitId;
    private UnitInfo unitInfo;

    private Date lastSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_view);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initialize(getIntent().getExtras());

        apiService = new GDApiService(this);
        apiService.setUnitInfoListener(this);

        basicDataView = (UnitBasicDataView)findViewById(R.id.unit_basic_data_view);

//        adapter = new UnitViewDetailsPagerAdapter(this);
//        unitDetailViewPager = (ViewPager)findViewById(R.id.unit_detail_vp);
//        unitDetailViewPager.setAdapter(adapter);

//        pageIndicator = (TabPageIndicator)findViewById(R.id.unit_view_vpi);
//        pageIndicator.setViewPager(unitDetailViewPager);

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
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();

        loadUnitInfo();
        // adapter.notifyDataSetChanged();
    }

    void initialize(Bundle extra) {
        if (extra != null) {
            unitId = extra.getString("id");
        }

        unitId = "24713"; // 飞翼高达凤凰
        // unitId = "10214";    // 高达试作2号机/高达试作1号机全方位推进型玉兰
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

//        adapter.setUnitInfo(unitInfo);
//        adapter.notifyDataSetChanged();

//        pageIndicator.invalidate();
//        pageIndicator.requestLayout();

        configureTabs();
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
                return view;
            }
        });

        spec.setIndicator(getResources().getString(R.string.unit_view_tab_title_weapons));
        tabHost.addTab(spec);
    }

    private void createUnitSkillTab() {
        TabHost.TabSpec spec = tabHost.newTabSpec("skills");
        spec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                UnitViewPlaceHolder emptyView = new UnitViewPlaceHolder(UnitViewActivity.this, null);
                emptyView.setText("2");
                emptyView.setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return emptyView;
            }
        });

        spec.setIndicator(getResources().getString(R.string.unit_view_tab_title_skills));
        tabHost.addTab(spec);
    }

    private void createUnitDetailTab() {
        TabHost.TabSpec spec = tabHost.newTabSpec("detail");
        spec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                UnitViewPlaceHolder emptyView = new UnitViewPlaceHolder(UnitViewActivity.this, null);
                emptyView.setText("3");
                emptyView.setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return emptyView;
            }
        });

        spec.setIndicator(getResources().getString(R.string.unit_view_tab_title_details));
        tabHost.addTab(spec);
    }

    private void createUnitRelatedVideoTab() {
        TabHost.TabSpec spec = tabHost.newTabSpec("video");
        spec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                UnitViewPlaceHolder emptyView = new UnitViewPlaceHolder(UnitViewActivity.this, null);
                emptyView.setText("4");
                emptyView.setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return emptyView;
            }
        });

        spec.setIndicator(getResources().getString(R.string.unit_related_video));
        tabHost.addTab(spec);
    }

    @Override
    public void onFetchingUnitInfoWithError(Exception e) {
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

//    class UnitViewDetailsPagerAdapter extends PagerAdapter {
//        Context context;
//        UnitInfo unitInfo;
//
//        String[] unitViewTabTitles;
//
//        public UnitViewDetailsPagerAdapter(Context context) {
//            super();
//
//            this.context = context;
//            unitViewTabTitles = context.getResources().getStringArray(R.array.unit_view_tab_titles);
//        }
//
//        public void setUnitInfo(UnitInfo unitInfo) {
//            this.unitInfo = unitInfo;
//        }
//
//        @Override
//        public int getCount() {
//            return unitViewTabTitles.length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object o) {
//            return view == o;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return unitViewTabTitles[position];
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            Log.d(LOG_TAG, "instantiateItem: " + position);
//            View view;
//            if (unitInfo == null) {
//                view = getEmptyView(position);
//                container.addView(view);
//                return view;
//            }
//
//            switch(position) {
//                case 0:
//                    view = getUnitWeaponView();
//                    break;
//                case 1:
//                    view = getUnitSkillView();
//                    break;
//                case 2:
//                    view = getUnitDetailView();
//                    break;
//                case 3:
//                    view = getRelatedVideoView();
//                    break;
//                default:
//                    view = getEmptyView(position);
//            }
//
//            view.setTag(position);
//            container.addView(view);
//            return view;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            Log.d(LOG_TAG, "destroyItem: " + position);
//            container.removeView((View)object);
//        }
//
//        View getEmptyView(int position) {
//            UnitViewPlaceHolder emptyView = new UnitViewPlaceHolder(context, null);
//            emptyView.setText(position + "");
//            emptyView.setLayoutParams(
//                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            return emptyView;
//        }
//
//        View getUnitWeaponView() {
//            if (unitWeaponView == null) {
//                Log.d(LOG_TAG, "getUnitWeaponView creating new");
//                unitWeaponView = new UnitWeaponsView(context, null);
//                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                );
//                unitWeaponView.setLayoutParams(layoutParams);
//                ((UnitWeaponsView) unitWeaponView).setUnitInfo(unitInfo);
//            }
//            return unitWeaponView;
//        }
//
//        View getUnitSkillView() {
//            if (unitSkillView == null) {
//                Log.d(LOG_TAG, "getUnitSkillView creating new");
//                unitSkillView = getEmptyView(1);
//            }
//            return unitSkillView;
//        }
//
//        View getUnitDetailView() {
//            if (unitDetailView == null) {
//                Log.d(LOG_TAG, "getUnitDetailView creating new");
//                unitDetailView = getEmptyView(2);
//            }
//            return unitDetailView;
//        }
//
//        View getRelatedVideoView() {
//            if (relatedVideoView == null) {
//                Log.d(LOG_TAG, "getRelatedVideoView creating new");
//                relatedVideoView = getEmptyView(3);
//            }
//            return relatedVideoView;
//        }
//    }

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
