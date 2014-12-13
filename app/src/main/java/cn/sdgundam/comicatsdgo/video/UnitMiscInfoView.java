package cn.sdgundam.comicatsdgo.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.api_model.UnitInfo;

/**
 * Created by xhguo on 11/22/2014.
 */
public class UnitMiscInfoView extends LinearLayout {
    UnitInfo unitInfo;

    TextView landformTV, wartypeTV, _4pTV, groupTV, featureTV, originTV,
        driverTV, regdateTV;

    TextView groupCaptionTV, _4pCaptionTV;

    public UnitMiscInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnitMiscInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_unit_misc_info, this, true);


        landformTV = (TextView)findViewById(R.id.landform_text_view);
        wartypeTV = (TextView)findViewById(R.id.wartype_text_view);
        _4pTV = (TextView)findViewById(R.id._4p_text_view);
        groupTV = (TextView)findViewById(R.id.group_text_view);
        featureTV = (TextView)findViewById(R.id.feature_text_view);
        originTV = (TextView)findViewById(R.id.origin_text_view);
        driverTV = (TextView)findViewById(R.id.driver_text_view);
        regdateTV = (TextView)findViewById(R.id.regdate_text_view);

        groupCaptionTV = (TextView)findViewById(R.id.group_caption_text_view);
        _4pCaptionTV = (TextView)findViewById(R.id._4p_caption_text_view);
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;

        landformTV.setText(unitInfo.getLandform());
        wartypeTV.setText(unitInfo.getWarType());

        if (unitInfo.isModification() || unitInfo.isOversize() || unitInfo.isRepair() || unitInfo.isSniping()) {
            String _4p = "";
            _4p += unitInfo.isModification() ? getContext().getString(R.string.unit_4p_modification) + " " : "";
            _4p += unitInfo.isOversize() ? getContext().getString(R.string.unit_4p_oversize) + " " : "";
            _4p += unitInfo.isRepair() ? getContext().getString(R.string.unit_4p_repair) + " " : "";
            _4p += unitInfo.isSniping() ? getContext().getString(R.string.unit_4p_sniping) + " " : "";

            _4pTV.setText(_4p);
        } else{
            _4pCaptionTV.setVisibility(GONE);
            _4pTV.setVisibility(GONE);
        }

        groupTV.setText(unitInfo.getGroupName1());
        if (unitInfo.getGroupName2().length() > 0) {
            groupCaptionTV.append("\n ");
            groupTV.append("\n" + unitInfo.getGroupName2());
        }

        /*
        * unit.feature,
                                 unit.originTitle,
                                 unit.driver,
                                 unit.regdate]];
        * */

        featureTV.setText(unitInfo.getFeature());
        originTV.setText(unitInfo.getOriginTitle());
        driverTV.setText(unitInfo.getDriver());
        regdateTV.setText(unitInfo.getRegdate());
     }
}
