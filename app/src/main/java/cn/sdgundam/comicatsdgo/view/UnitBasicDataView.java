package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;

/**
 * Created by xhguo on 11/19/2014.
 */
public class UnitBasicDataView extends LinearLayout {
    GaugeBarView attackGauge;
    GaugeBarView defenseGauge;
    GaugeBarView mobilityGauge;
    GaugeBarView controlGauge;
    ScrollingTextView modelNameTextView;
    ImageView unitImageView;
    TextView rankTextView;
    TextView sum3DTextView;
    TextView sum4DTextView;

    private static final float UNIT_MAX_ABILITY_VALUE = 205f;

    public UnitBasicDataView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_unit_basic, this, true);

        // find views
        attackGauge = (GaugeBarView)findViewById(R.id.gauge_attack);
        defenseGauge = (GaugeBarView)findViewById(R.id.gauge_defense);
        mobilityGauge = (GaugeBarView)findViewById(R.id.gauge_mobility);
        controlGauge = (GaugeBarView)findViewById(R.id.gauge_control);

        modelNameTextView = (ScrollingTextView)findViewById(R.id.model_name_text_view);
        modelNameTextView.setSpeed(16);
        modelNameTextView.setTextColor(Color.BLACK);
        modelNameTextView.setTextSize(getResources().getDimensionPixelSize(R.dimen.unit_view_model_name_text_size));

        unitImageView = (ImageView)findViewById(R.id.unit_image_view);
        rankTextView = (TextView)findViewById(R.id.rank_text_view);
        sum3DTextView = (TextView)findViewById(R.id.sum_3d_text_view);
        sum4DTextView = (TextView)findViewById(R.id.sum_4d_text_view);
    }

    String unitId;
    String modelName;
    String rank;

    float attackValue;
    float defenseValue;
    float mobilityValue;
    float controlValue;

    float sum3DValue;
    float sum4DValue;

    public void setUnitId(String unitId) {
        this.unitId = unitId;
        Picasso.with(getContext())
                .load(Uri.parse(String.format("http://cdn.sdgundam.cn/data-source/acc/unit-yoppa/app/%s.png", unitId)))
                .into(unitImageView);
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
        modelNameTextView.setText(modelName);
    }

    public void setRank(String rank) {
        this.rank = rank;
        rankTextView.setText(rank);
    }

    public void setAttackValue(float attackValue) {
        this.attackValue = attackValue;
        attackGauge.setGaugePercent(attackValue / UNIT_MAX_ABILITY_VALUE);
        attackGauge.setTextOnGauge((int)attackValue + "");
    }

    public void setDefenseValue(float defenseValue) {
        this.defenseValue = defenseValue;
        defenseGauge.setGaugePercent(defenseValue / UNIT_MAX_ABILITY_VALUE);
        defenseGauge.setTextOnGauge((int)defenseValue + "");
    }

    public void setMobilityValue(float mobilityValue) {
        this.mobilityValue = mobilityValue;
        mobilityGauge.setGaugePercent(mobilityValue / UNIT_MAX_ABILITY_VALUE);
        mobilityGauge.setTextOnGauge((int)mobilityValue + "");
    }

    public void setControlValue(float controlValue) {
        this.controlValue = controlValue;
        controlGauge.setGaugePercent(controlValue / UNIT_MAX_ABILITY_VALUE);
        controlGauge.setTextOnGauge((int)controlValue + "");
    }

    public void setSum3DValue(float sum3DValue) {
        this.sum3DValue = sum3DValue;
        sum3DTextView.setText((int)sum3DValue + "");
    }

    public void setSum4DValue(float sum4DValue) {
        this.sum4DValue = sum4DValue;
        sum4DTextView.setText((int)sum4DValue + "");
    }

    public void playAnimations() {
        attackGauge.playAnimation();
        defenseGauge.playAnimation();
        mobilityGauge.playAnimation();
        controlGauge.playAnimation();
    }
}
