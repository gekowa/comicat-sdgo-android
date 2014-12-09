package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.List;

import cn.sdgundam.comicatsdgo.data_model.UnitInfoShort;
import cn.sdgundam.comicatsdgo.data_model.UnitList;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchUnitsByOriginListener;


public class UnitsByOriginActivity extends Activity implements
        FetchUnitsByOriginListener {
    static final String LOG_TAG = UnitsByOriginActivity.class.getSimpleName();

    GDApiService apiService;

    GridView unitsGV;

    UnitsGridViewAdapter adapter;

    List<UnitInfoShort> units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units_by_origin);

        apiService = new GDApiService(this);
        apiService.setFetchUnitsByOriginListener(this);

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
                int itemSize = resources.getDimensionPixelSize(R.dimen.units_by_origin_item_size);

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

        unitsGV.setAdapter(adapter);
    }

    @Override
    public void onReceiveUnitListResult(UnitList unitList, String originIndex) {

    }

    @Override
    public void onFetchingUnitsFail(Exception e) {

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
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
