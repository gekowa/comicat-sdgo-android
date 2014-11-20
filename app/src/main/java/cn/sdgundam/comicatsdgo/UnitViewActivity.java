package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

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

    private UnitBasicDataView basicDataView;
    private ViewPager unitDetailViewPager;
    private TabPageIndicator pageIndicator;
    private ViewGroup progressViewContainer;

    private UnitViewDetailsPagerAdapter adapter;

    private GDApiService apiService;
    private PullToRefreshLayout ptrLayout;
    private NetworkErrorView nev;

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

        adapter = new UnitViewDetailsPagerAdapter(this);
        basicDataView = (UnitBasicDataView)findViewById(R.id.unit_basic_data_view);
        unitDetailViewPager = (ViewPager)findViewById(R.id.unit_detail_vp);
        unitDetailViewPager.setAdapter(adapter);

        pageIndicator = (TabPageIndicator)findViewById(R.id.unit_view_vpi);
        pageIndicator.setViewPager(unitDetailViewPager);

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

        loadUnitInfo();
    }

    void initialize(Bundle extra) {
        if (extra != null) {
            unitId = extra.getString("id");
        }

        unitId = "10212";
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

        configureBasicDataView(unitInfo);
        basicDataView.playAnimations();

        adapter.setUnitInfo(unitInfo);
        adapter.notifyDataSetChanged();

        pageIndicator.invalidate();
        pageIndicator.requestLayout();
    }

    private void configureBasicDataView(UnitInfo unitInfo) {
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

    class UnitViewDetailsPagerAdapter extends PagerAdapter {
        Context context;
        UnitInfo unitInfo;

        String[] unitViewTabTitles;

        public UnitViewDetailsPagerAdapter(Context context) {
            super();

            this.context = context;
            unitViewTabTitles = context.getResources().getStringArray(R.array.unit_view_tab_titles);
        }

        public void setUnitInfo(UnitInfo unitInfo) {
            this.unitInfo = unitInfo;
        }

        @Override
        public int getCount() {
            return unitViewTabTitles.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == (View)o;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return unitViewTabTitles[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (unitInfo == null) {
                View emptyView = new UnitViewPlaceHolder(context, null);
                container.addView(emptyView);
                return emptyView;
            }

            if (position == 0) {

                UnitWeaponsView weaponListView = new UnitWeaponsView(context, null);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                weaponListView.setLayoutParams(layoutParams);
                weaponListView.setUnitInfo(unitInfo);
                container.addView(weaponListView);
                return weaponListView;
            } else {
                View emptyView = new UnitViewPlaceHolder(context, null);
                container.addView(emptyView);
                return emptyView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    class UnitViewPlaceHolder extends View {
        UnitViewPlaceHolder(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }
    }
}
