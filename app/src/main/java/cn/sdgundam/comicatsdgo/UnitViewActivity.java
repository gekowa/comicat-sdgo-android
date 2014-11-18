package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cn.sdgundam.comicatsdgo.data_model.UnitInfo;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchUnitInfoListener;
import cn.sdgundam.comicatsdgo.view.GaugeBarView;
import cn.sdgundam.comicatsdgo.view.ScrollingTextView;


public class UnitViewActivity extends Activity implements FetchUnitInfoListener {
    GaugeBarView attackGauge;
    GaugeBarView defenseGauge;
    GaugeBarView mobilityGauge;
    GaugeBarView controlGauge;

    ScrollingTextView modelNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_view);

        GDApiService apiService = new GDApiService(this);
        apiService.setUnitInfoListener(this);
        apiService.fetchUnitInfo("10021", true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        attackGauge = (GaugeBarView)findViewById(R.id.gauge_attack);
        defenseGauge = (GaugeBarView)findViewById(R.id.gauge_defense);
        mobilityGauge = (GaugeBarView)findViewById(R.id.gauge_mobility);
        controlGauge = (GaugeBarView)findViewById(R.id.gauge_control);

        playAnimations();

        modelNameTextView = (ScrollingTextView)findViewById(R.id.model_name_text_view);
        modelNameTextView.setSpeed(12);
        modelNameTextView.setText("非常长的文字很长很长没完没了不旋转不算完滚动了才行哎哟简直要死人命了那还能咋办呢");


    }

    private void playAnimations() {
        Toast.makeText(this, "Animating...", Toast.LENGTH_SHORT).show();

        attackGauge.playAnimation();
        defenseGauge.playAnimation();
        mobilityGauge.playAnimation();
        controlGauge.playAnimation();
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


    }

    @Override
    public void onFetchingUnitInfoWithError(Exception e) {

    }
}
