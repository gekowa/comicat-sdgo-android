package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.sdgundam.comicatsdgo.data_model.UnitInfoShort;
import cn.sdgundam.comicatsdgo.data_model.UnitList;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.SearchUnitListener;


public class SearchUnitActivity extends Activity implements
        SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener,
        SearchUnitListener {

    static final String LOG_TAG = SearchUnitActivity.class.getSimpleName();

    static final int SEARCH_DELAY = 600;

    String keyword = "";
    String rank = "";

    List<UnitInfoShort> units;
    SearchUnitResultAdapter adapter;
    GDApiService apiService;

    ViewGroup progressViewContainer;
    ListView unitListView;
    SearchView searchView;

    Handler delayedSearchHandler = new Handler();
    Runnable delayedSearch = new Runnable() {
        @Override
        public void run() {
            delayedSearchHandler.removeCallbacks(delayedSearch);

            performSearch();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_unit);

        units = new ArrayList<UnitInfoShort>();

        apiService = new GDApiService(this);
        apiService.setSearchUnitListener(this);

        adapter = new SearchUnitResultAdapter(this);
        unitListView = (ListView)findViewById(R.id.unit_list_view);
        unitListView.setAdapter(adapter);
        unitListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (searchView != null) {
                    searchView.clearFocus();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }
        });
        unitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UnitInfoShort uis = (UnitInfoShort)adapter.getItem(position);
                Intent intent = Utility.makeUnitViewActivityIntent(SearchUnitActivity.this, uis.getUnitId());
                SearchUnitActivity.this.startActivity(intent);
            }
        });

        progressViewContainer = (ViewGroup)findViewById(R.id.progress_bar_container);
    }

    private void performSearch() {
        performSearchInternal(keyword, rank);
    }

    private void performSearchInternal(String keyword, String rank) {
        if (keyword.length() > 0) {
            showLoading();
            apiService.searchUnit(keyword, rank);
        } else {
            // clear
            units = new ArrayList<UnitInfoShort>();
            // adapter.notifyDataSetInvalidated();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetInvalidated();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_unit, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search_unit);
        searchMenuItem.expandActionView();
        searchMenuItem.setOnActionExpandListener(this);

        searchView = (SearchView)MenuItemCompat.getActionView(searchMenuItem);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search_unit_hint));

        searchView.setOnQueryTextListener(this);
//        searchView.setOnCloseListener(this);
//        searchView.setOnQueryTextFocusChangeListener(this);

        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public void onClick(View view) {
//        Log.d(LOG_TAG, "OnSearchClick");
//    }
//
//    @Override
//    public boolean onClose() {
//        this.finish();
//        return true;
//    }
//
//    @Override
//    public void onFocusChange(View view, boolean b) {
//        Log.d(LOG_TAG, "onFocusChange: " + view.toString() + " b:" + b);
//    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        finish();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(LOG_TAG, "onQueryTextSubmit: " + query);

        if (!this.keyword.equals(query)) {

            this.keyword = query;
            this.rank = "";

            performSearch();
        }

        searchView.clearFocus();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(LOG_TAG, "onQueryTextChange: " + newText);
        Log.d(LOG_TAG, "onQueryTextChange: searchView.isIconified() " + searchView.isIconified());



        this.keyword = newText;
        this.rank = "";

        delayedSearchHandler.removeCallbacks(delayedSearch);
        delayedSearchHandler.postDelayed(delayedSearch, SEARCH_DELAY);

        return true;
    }

    @Override
    public void onReceiveUnitSearchResult(UnitList unitList) {
        this.units = unitList.getUnits();

        hideAllLoadings();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetInvalidated();
            }
        });
    }

    @Override
    public void onSearchUnitFail(Exception e) {
        hideAllLoadings();
        Utility.showNetworkErrorAlertDialog(this, e);
    }


    void showLoading() {
        progressViewContainer.setVisibility(View.VISIBLE);
    }

    void hideAllLoadings() {
        progressViewContainer.setVisibility(View.INVISIBLE);
    }

    class SearchUnitResultAdapter extends BaseAdapter {
        Context context;

        SearchUnitResultAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return SearchUnitActivity.this.units.size();
        }

        @Override
        public Object getItem(int position) {
            return SearchUnitActivity.this.units.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            ImageView unitImageView;
            TextView modelNameTextView;
            TextView rankTextView;

            if (convertView != null) {
                view = convertView;

                unitImageView = (ImageView)view.getTag(R.id.unit_image_view);
                modelNameTextView = (TextView)view.getTag(R.id.model_name_text_view);
                rankTextView = (TextView)view.getTag(R.id.rank_text_view);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.view_unit_search_result_item, null, false);
                unitImageView = (ImageView)view.findViewById(R.id.unit_image_view);
                modelNameTextView = (TextView)view.findViewById(R.id.model_name_text_view);
                rankTextView = (TextView)view.findViewById(R.id.rank_text_view);

                view.setTag(R.id.unit_image_view, unitImageView);
                view.setTag(R.id.model_name_text_view, modelNameTextView);
                view.setTag(R.id.rank_text_view, rankTextView);
            }

            UnitInfoShort uis = (UnitInfoShort)getItem(position);

            Picasso.with(context)
                    .load(Uri.parse(Utility.getUnitImageURLByUnitId(uis.getUnitId())))
                    .into(unitImageView);

            modelNameTextView.setText(uis.getModelName());
            rankTextView.setText(uis.getRank());

            return view;
        }
    }
}
