package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.api_model.UnitInfo;

/**
 * Created by xhguo on 11/26/2014.
 */
public class UnitMixView extends LinearLayout {
    UnitInfo unitInfo;

    Button mixButton, mixButtonCN;

    UnitMixEventListener listener;

    public UnitMixView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.view_unit_mix, this, true);

        mixButton = (Button)findViewById(R.id.mix_button);
        mixButtonCN = (Button)findViewById(R.id.mix_button_cn);
    }

    public void setListener(UnitMixEventListener listener) {
        this.listener = listener;
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;

        if (unitInfo.getMixingKeyUnit() != null) {
            mixButton.setVisibility(VISIBLE);
            if (listener != null) {
                mixButton.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && listener != null) {
                            listener.OnShowUnitMixInfo();
                            return true;
                        }
                        return false;
                    }
                });
            }
        }

        if (unitInfo.getMixingKeyUnitCN() != null) {
            mixButtonCN.setVisibility(VISIBLE);
            if (listener != null) {
                mixButtonCN.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && listener != null) {
                            listener.OnShowUnitMixInfoCN();
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
    }

    public interface UnitMixEventListener {
        void OnShowUnitMixInfo();
        void OnShowUnitMixInfoCN();
    }
}
