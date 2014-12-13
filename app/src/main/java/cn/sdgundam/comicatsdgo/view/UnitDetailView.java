package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.api_model.UnitInfo;
import cn.sdgundam.comicatsdgo.video.UnitMiscInfoView;

/**
 * Created by xhguo on 11/22/2014.
 */
public class UnitDetailView extends LinearLayout {
    UnitInfo unitInfo;

    UnitMixView.UnitMixEventListener unitMixEventListener;

    public UnitDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(VERTICAL);
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;

        // add views
        addMiscInfoView();
        addMixView();
        addStoryView();
        addGetwayView();

        requestLayout();
        invalidate();

    }

    public void setUnitMixEventListener(UnitMixView.UnitMixEventListener unitMixEventListener) {
        this.unitMixEventListener = unitMixEventListener;
    }

    private void addMixView() {
        if (unitInfo.getMixingKeyUnit() != null || unitInfo.getMixingKeyUnitCN() != null) {
            UnitMixView view = new UnitMixView(getContext(), null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setListener(unitMixEventListener);
            view.setUnitInfo(unitInfo);
            addView(view);
            addView(createSeparatorTextView());
        }
    }

    private void addMiscInfoView() {
        UnitMiscInfoView view = new UnitMiscInfoView(getContext(), null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setUnitInfo(unitInfo);
        addView(view);
        addView(createSeparatorTextView());
    }

    private void addStoryView() {
        UnitStoryView view = new UnitStoryView(getContext(), null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setStory(unitInfo.getStory());
        addView(view);
        addView(createSeparatorTextView());
    }

    private void addGetwayView() {
        UnitGetwayView view = new UnitGetwayView(getContext(), null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setUnitInfo(unitInfo);
        addView(view);
        addView(createSeparatorTextView());
    }

    View createSeparatorTextView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_unit_detail_separator, this, false);
    }
}
