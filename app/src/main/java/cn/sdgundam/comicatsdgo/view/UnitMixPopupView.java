package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.sdgundam.comicatsdgo.ImageLoaderOptions;
import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.api_model.UnitMixMaterial;

/**
 * Created by xhguo on 11/26/2014.
 */
public class UnitMixPopupView extends FrameLayout {
    UnitMixMaterial keyUnit;
    UnitMixMaterial[] materialUnits;

    UMMEventListener ummEventListener;

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

        isConfigured = true;



        requestLayout();
        invalidate();
    }

    public void setUMMEventListener(UMMEventListener ummEventListener) {
        this.ummEventListener = ummEventListener;
    }

    private void setupUnitMixMaterial(final UnitMixMaterial umm, View ummv, boolean isKeyUnit) {
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

        ImageLoader.getInstance().displayImage(Utility.getUnitImageURLByUnitId(umm.getUnitId()), unitIV, ImageLoaderOptions.Normal);

        lvTV.setText(umm.getLevel());

        ummv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getContext(), umm.getUnitId(), Toast.LENGTH_SHORT).show();
                if (UnitMixPopupView.this.ummEventListener != null) {
                    UnitMixPopupView.this.ummEventListener.onUMMSelected(umm.getUnitId());
                }
            }
        });
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    public interface UMMEventListener {
        public void onUMMSelected(String unitId);
    }
}
