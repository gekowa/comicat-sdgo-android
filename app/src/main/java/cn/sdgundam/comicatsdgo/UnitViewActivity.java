package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cn.sdgundam.comicatsdgo.data_model.UnitInfo;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchUnitInfoListener;
import cn.sdgundam.comicatsdgo.view.GaugeBarView;
import cn.sdgundam.comicatsdgo.view.ScrollingTextView;
import cn.sdgundam.comicatsdgo.view.UnitBasicDataView;


public class UnitViewActivity extends Activity implements FetchUnitInfoListener {
    UnitBasicDataView basicDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_view);

        GDApiService apiService = new GDApiService(this);
        apiService.setUnitInfoListener(this);
        apiService.fetchUnitInfo("10212", true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        basicDataView = (UnitBasicDataView)findViewById(R.id.unit_basic_data_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.unit_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveUnitInfo(UnitInfo unitInfo) {
        configureBasicDataView(unitInfo);

        basicDataView.playAnimations();
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

    }
}
