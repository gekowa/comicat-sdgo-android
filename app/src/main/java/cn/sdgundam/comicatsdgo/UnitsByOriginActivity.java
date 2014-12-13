package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sdgundam.comicatsdgo.data_model.OriginInfo;
import cn.sdgundam.comicatsdgo.data_model.UnitInfoShort;
import cn.sdgundam.comicatsdgo.data_model.UnitList;
import cn.sdgundam.comicatsdgo.extension.SwipeRefreshLayout;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchUnitsByOriginListener;
import cn.sdgundam.comicatsdgo.view.NetworkErrorView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.DefaultHeaderTransformer;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class UnitsByOriginActivity extends Activity implements
        FetchUnitsByOriginListener,
        OnRefreshListener {
    static final String LOG_TAG = UnitsByOriginActivity.class.getSimpleName();

    GDApiService apiService;

    GridView unitsGV;
    private ViewGroup progressViewContainer;
    private PullToRefreshLayout ptrLayout;
    private NetworkErrorView nev;

    UnitsGridViewAdapter adapter;

    String origin;
    String shortTitle;
    List<UnitInfoShort> units = new ArrayList<UnitInfoShort>();

    Date lastSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize(getIntent());

        setContentView(R.layout.activity_units_by_origin);

        initializeViews();

        apiService = new GDApiService(this);
        apiService.setFetchUnitsByOriginListener(this);

        loadUnits(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("机体按作品");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("机体按作品");
        MobclickAgent.onPause(this);
    }

    private void initialize(Intent intent) {
        origin = intent.getStringExtra("origin");
        if (origin == null || origin.length() == 0) {
            origin = "01";
        }
        shortTitle = intent.getStringExtra("shortTitle");
    }

    private void initializeViews() {
        adapter = new UnitsGridViewAdapter(this);

        final Resources resources = getResources();

        unitsGV = (GridView)findViewById(R.id.units_grid_view);

        unitsGV.post(new Runnable() {
            @Override
            public void run() {
                // Log.d(LOG_TAG, "UnitsGridView width: " + unitsGV.getWidth());
                int fullWidth = unitsGV.getWidth();
                // calculate NumberOfColumns
                int gapMin = resources.getDimensionPixelSize(R.dimen.units_by_origin_grid_h_gap_min);
                int gapMax = resources.getDimensionPixelSize(R.dimen.units_by_origin_grid_h_gap_max);
                int horMargin = resources.getDimensionPixelSize(R.dimen.units_by_origin_grid_h_margin);
                int itemSize = resources.getDimensionPixelSize(R.dimen.units_by_origin_item_width);

                int numColumns = 3;
                boolean minused = false, plused = false;
                for (;;) {
                    int gap = (int)((float)(fullWidth - 2 * horMargin - numColumns * itemSize) / (numColumns - 1));
                    if (gap < gapMin) {
                        if (plused) {
                            numColumns--;
                            break;
                        } else {
                            numColumns--;
                            minused = true;
                        }

                        if (numColumns <= 1) {
                            numColumns = 2;
                            break;
                        }
                    } else if (gap > gapMax) {
                        if (minused) {
                            numColumns++;
                            break;
                        } else {
                            numColumns++;
                            plused = true;
                        }
                    } else {
                        break;
                    }
                }

                unitsGV.setNumColumns(numColumns);
                Log.d(LOG_TAG, "UnitsGridView numColumns: " + numColumns);
            }
        });

//        View emptyPlaceholder1 = new View(this);
//        emptyPlaceholder1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                getResources().getDimensionPixelSize(R.dimen.scroll_view_default_content_offset)));
//        unitsGV.addHeaderView(emptyPlaceholder1);
//
//        View emptyPlaceholder2 = new View(this);
//        emptyPlaceholder2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                getResources().getDimensionPixelSize(R.dimen.scroll_view_default_content_offset)));
//        unitsGV.addHeaderView(emptyPlaceholder2);

        unitsGV.setAdapter(adapter);
        unitsGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UnitInfoShort uis = (UnitInfoShort) adapter.getItem(position);
                Intent intent = Utility.makeUnitViewActivityIntent(UnitsByOriginActivity.this, uis.getUnitId());
                startActivity(intent);
            }
        });


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
                UnitsByOriginActivity.this.loadUnits(true);
                nev.setVisibility(View.INVISIBLE);
            }
        });

        getActionBar().setTitle(shortTitle);
    }

    private void loadUnits(boolean force) {
        showLoading();

        apiService.fetchUnitsByOrigin(origin, force);
    }

    @Override
    public void onReceiveUnitListResult(UnitList unitList, String originIndex) {
        hideAllLoadings();
        if (originIndex.equals(origin)) {
            units = unitList.getUnits();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetInvalidated();
                }
            });
        }
    }

    @Override
    public void onFetchingUnitsFail(Exception e) {
        hideAllLoadings();

        // Display "Network Unavailable" view
        if (units == null || units.size() == 0) {
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

    @Override
    public void onRefreshStarted(View view) {
        Boolean shouldForce = lastSwipeRefresh != null && (new Date().getTime() - lastSwipeRefresh.getTime()) / 1000 < 10;
        loadUnits(shouldForce);
        if (shouldForce) {
            lastSwipeRefresh = null;
        } else {
            lastSwipeRefresh = new Date();
        }
    }

    class UnitsGridViewAdapter extends BaseAdapter {
        Context context;

        UnitsGridViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return UnitsByOriginActivity.this.units.size();
        }

        @Override
        public Object getItem(int position) {
            return UnitsByOriginActivity.this.units.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            ImageView unitIV;
            TextView modelNameTV;

            if (convertView != null) {
                view = convertView;

                unitIV = (ImageView)view.getTag(R.id.unit_image_view);
                modelNameTV = (TextView)view.getTag(R.id.model_name_text_view);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.view_single_unit, null, false);
                unitIV = (ImageView)view.findViewById(R.id.unit_image_view);
                modelNameTV = (TextView)view.findViewById(R.id.model_name_text_view);

                view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, getResources().getDimensionPixelOffset(R.dimen.units_by_origin_item_height)));

                view.setTag(R.id.unit_image_view, unitIV);
                view.setTag(R.id.model_name_text_view, modelNameTV);
            }

            UnitInfoShort uis = (UnitInfoShort)getItem(position);

            Picasso.with(context)
                    .load(Uri.parse(Utility.getUnitImageURLByUnitId(uis.getUnitId())))
                    .into(unitIV);

            modelNameTV.setText(uis.getModelName());

            return view;
        }
    }
}
