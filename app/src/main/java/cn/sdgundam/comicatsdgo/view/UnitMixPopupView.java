package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.data_model.UnitMixMaterial;

/**
 * Created by xhguo on 11/26/2014.
 */
public class UnitMixPopupView extends FrameLayout {
    UnitMixMaterial keyUnit;
    UnitMixMaterial[] materialUnits;

    boolean isConfigured = false;

    public UnitMixPopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUnitMixInfo(UnitMixMaterial keyUnit, UnitMixMaterial[] materialUnits) {
        this.keyUnit = keyUnit;
        this.materialUnits = materialUnits;

        try {
            LayoutInflater.from(getContext()).inflate(
                    R.layout.class.getDeclaredField("view_unit_mix_popup_for_" + (materialUnits.length + 1)).getInt(null), this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        setupUnitMixMaterial(keyUnit, findViewById(R.id.umm_1), true);

        try {
            for (int i = 0; i < materialUnits.length; i++) {
                setupUnitMixMaterial(materialUnits[i], findViewById(R.id.class.getDeclaredField("umm_" + (i + 2)).getInt(null)), false);
            }
          } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

//        addUnitMixMaterial(keyUnit, true, false);
//        for(int i = 0; i < materialUnits.length; i++) {
//            boolean newLine = false;
//
//            if (materialUnits.length == 4 && i == 1) {
//                newLine = true;
//            }
//
//            addUnitMixMaterial(materialUnits[i], false, newLine);
//        }
//
        isConfigured = true;

        requestLayout();
        invalidate();
    }

    private void setupUnitMixMaterial(UnitMixMaterial umm, View ummv, boolean isKeyUnit) {
        if (ummv == null) {
            return;
        }

        TextView keyUnitTV = (TextView)ummv.findViewById(R.id.unit_mix_key_caption);
        ImageView unitIV = (ImageView)ummv.findViewById(R.id.unit_mix_image_view);
        TextView lvTV = (TextView)ummv.findViewById(R.id.unit_mix_level);

        if (!isKeyUnit) {
            keyUnitTV.setText(" ");
            keyUnitTV.setBackgroundColor(Color.TRANSPARENT);
        }

        Picasso.with(getContext()).load(Uri.parse(Utility.getUnitImageURLByUnitId(umm.getUnitId()))).into(unitIV);

        lvTV.setText(umm.getLevel());
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    public void setConfigured(boolean isConfigured) {
        this.isConfigured = isConfigured;
    }
}
