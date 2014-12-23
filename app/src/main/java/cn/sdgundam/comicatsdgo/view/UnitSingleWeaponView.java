package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.sdgundam.comicatsdgo.ImageLoaderOptions;
import cn.sdgundam.comicatsdgo.R;

/**
 * Created by xhguo on 11/20/2014.
 */
public class UnitSingleWeaponView extends RelativeLayout {
    int weaponIndex;
    int weaponId;
    String weaponCaption;
    String weaponName;
    String weaponProperty;
    String weaponEffect;
    String weaponRange;
    String weaponExLine1;
    String weaponExLine2;

    TextView weaponCaptionTextView;
    TextView weaponNameTextView;
    TextView weaponPropertyTextView;
    TextView weaponEffectTextView;
    TextView weaponRangeTextView;
    TextView weaponExLine1TextView;
    TextView weaponExLine2TextView;
    ImageView weaponImageView;

    static final String NONE_TEXT = "无";


    public UnitSingleWeaponView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnitSingleWeaponView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.view_unit_single_weapon, this, true);

        weaponCaptionTextView = (TextView)findViewById(R.id.weapon_caption_text_view);
        weaponNameTextView = (TextView)findViewById(R.id.weapon_name_text_view);
        weaponPropertyTextView = (TextView)findViewById(R.id.weapon_property_text_view);
        weaponEffectTextView = (TextView)findViewById(R.id.weapon_effect_text_view);
        weaponRangeTextView = (TextView)findViewById(R.id.weapon_range_text_view);
        weaponExLine1TextView = (TextView)findViewById(R.id.weapon_ex_line1_text_view);
        weaponExLine2TextView = (TextView)findViewById(R.id.weapon_ex_line2_text_view);
        weaponImageView = (ImageView)findViewById(R.id.weapon_image_view);
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;

        ImageLoader.getInstance().displayImage(String.format("http://cdn.sdgundam.cn/data-source/acc/weapon/%s.gif", weaponId), weaponImageView, ImageLoaderOptions.NormalOpaque);
    }

    public void setWeaponIndex(int weaponIndex) {
        this.weaponIndex = weaponIndex;

        if (weaponIndex > 3) {
            weaponCaption = String.format("R后 武器%s", weaponIndex - 3);
        } else {
            weaponCaption = String.format("武器%s", weaponIndex);
        }

        weaponCaptionTextView.setText(weaponCaption);
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;

        weaponNameTextView.setText(weaponName);
    }

    public void setWeaponProperty(String weaponProperty) {
        this.weaponProperty = weaponProperty;

        if (!NONE_TEXT.equals(weaponProperty)) {
            weaponPropertyTextView.setText(weaponProperty);
        }
    }

    public void setWeaponEffect(String weaponEffect) {
        this.weaponEffect = weaponEffect;

        if (!NONE_TEXT.equals(weaponEffect)) {
            weaponEffectTextView.setText(weaponEffect);
        }
    }

    public void setWeaponRange(String weaponRange) {
        this.weaponRange = weaponRange;

        weaponRangeTextView.setText(weaponRange);
    }

    public void setWeaponExLine1(String weaponExLine1) {
        this.weaponExLine1 = weaponExLine1;

        if (weaponExLine1.length() == 0) {
            weaponExLine1TextView.setVisibility(GONE);
        } else {
            weaponExLine1TextView.setText(weaponExLine1);
        }
    }

    public void setWeaponExLine2(String weaponExLine2) {
        this.weaponExLine2 = weaponExLine2;

        if (weaponExLine2.length() == 0) {
            weaponExLine2TextView.setVisibility(GONE);
        } else {
            weaponExLine2TextView.setText(weaponExLine2);
        }
    }
}
