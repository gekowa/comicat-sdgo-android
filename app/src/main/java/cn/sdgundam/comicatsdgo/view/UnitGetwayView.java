package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.sdgundam.comicatsdgo.data_model.UnitInfo;
import cn.sdgundam.comicatsdgo.R;

/**
 * Created by xhguo on 11/23/2014.
 */
public class UnitGetwayView extends LinearLayout {
    UnitInfo unitInfo;

    private TextView capsuleTV, buyTV, buyMixTV, questTV, missionTV, labTV, etcTV;

    private LinearLayout capsuleContainer, buyContainer, questContainer,
        missionContainer, labContainer, etcContainer;

    public UnitGetwayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_unit_getway, null, true);

        capsuleTV = (TextView)findViewById(R.id.unit_getway_capsule);
        buyTV = (TextView)findViewById(R.id.unit_getway_buy);
        buyMixTV = (TextView)findViewById(R.id.unit_getway_buy_mix);
        questTV = (TextView)findViewById(R.id.unit_getway_quest);
        missionTV = (TextView)findViewById(R.id.unit_getway_mission);
        labTV = (TextView)findViewById(R.id.unit_getway_lab);
        etcTV = (TextView)findViewById(R.id.unit_getway_etc);

        capsuleContainer = (LinearLayout)findViewById(R.id.unit_getway_capsule_container);
        buyContainer = (LinearLayout)findViewById(R.id.unit_getway_buy_container);
        questContainer = (LinearLayout)findViewById(R.id.unit_getway_quest_container);
        missionContainer = (LinearLayout)findViewById(R.id.unit_mission_container);
        labContainer = (LinearLayout)findViewById(R.id.unit_labs_container);
        etcContainer = (LinearLayout)findViewById(R.id.unit_getway_etc_container);
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;


    }
}
