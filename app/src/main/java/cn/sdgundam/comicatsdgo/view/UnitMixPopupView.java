package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.data_model.UnitMixMaterial;

/**
 * Created by xhguo on 11/26/2014.
 */
public class UnitMixPopupView extends FlowLayout {
    UnitMixMaterial keyUnit;
    UnitMixMaterial[] materialUnits;

    public UnitMixPopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUnitMixInfo(UnitMixMaterial keyUnit, UnitMixMaterial[] materialUnits) {
        this.keyUnit = keyUnit;
        this.materialUnits = materialUnits;

        setFillLines(FillLines.ALL);

        addUnitMixMaterial(keyUnit, true, false);
        for(int i = 0; i < materialUnits.length; i++) {
            boolean newLine = false;

            if (materialUnits.length == 4 && i == 1) {
                newLine = true;
            }

            addUnitMixMaterial(materialUnits[i], false, newLine);
        }

        requestLayout();
        invalidate();
    }

    private void addUnitMixMaterial(UnitMixMaterial umm, boolean isKeyUnit, boolean newLine) {
        View ummv = LayoutInflater.from(getContext()).inflate(R.layout.view_unit_mix_material, this, false);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.horizontalSpacing = 8;
        layoutParams.verticalSpacing = 6;
        layoutParams.weight = 0.1f;
        layoutParams.newLine = newLine;

        ummv.setLayoutParams(layoutParams);

        TextView keyUnitTV = (TextView)ummv.findViewById(R.id.unit_mix_key_caption);
        ImageView unitIV = (ImageView)ummv.findViewById(R.id.unit_mix_image_view);
        TextView lvTV = (TextView)ummv.findViewById(R.id.unit_mix_level);

        if (!isKeyUnit) {
            keyUnitTV.setText(" ");
            keyUnitTV.setBackgroundColor(Color.TRANSPARENT);
        }

        Picasso.with(getContext()).load(Uri.parse(Utility.getUnitImageURLByUnitId(umm.getUnitId()))).into(unitIV);

        lvTV.setText(umm.getLevel());

        addView(ummv);
    }


}
