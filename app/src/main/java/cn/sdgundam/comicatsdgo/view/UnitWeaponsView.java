package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.data_model.UnitInfo;

/**
 * Created by xhguo on 11/20/2014.
 */
public class UnitWeaponsView extends LinearLayout {
    UnitInfo unitInfo;

    public UnitWeaponsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(VERTICAL);

//        LayoutInflater.from(context).inflate(R.layout.view_unit_weapons, this, true);
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;

        for (int i = 1; i <= unitInfo.getNumberOfWeapons(); i++) {
            UnitSingleWeaponView view;
            view = new UnitSingleWeaponView(getContext(), null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.setWeaponIndex(i);

            try {
                int weaponId = (Integer)UnitInfo.class.getDeclaredMethod("getWeapon" + i, null).invoke(unitInfo);

                String weaponName = (String)UnitInfo.class.getDeclaredMethod("getWeaponName" + i, null).invoke(unitInfo);
                String weaponProperty = (String)UnitInfo.class.getDeclaredMethod("getWeaponProperty" + i, null).invoke(unitInfo);
                String weaponEffect = (String)UnitInfo.class.getDeclaredMethod("getWeaponEffect" + i, null).invoke(unitInfo);
                String weaponRange = (String)UnitInfo.class.getDeclaredMethod("getWeaponRange" + i, null).invoke(unitInfo);
                String weaponExLine1 = (String)UnitInfo.class.getDeclaredMethod(String.format("getWeaponEx%sLine1", i), null).invoke(unitInfo);
                String weaponExLine2 = (String)UnitInfo.class.getDeclaredMethod(String.format("getWeaponEx%sLine2", i), null).invoke(unitInfo);

                view.setWeaponId(weaponId);
                view.setWeaponName(weaponName);
                view.setWeaponProperty(weaponProperty);
                view.setWeaponEffect(weaponEffect);
                view.setWeaponRange(weaponRange);
                view.setWeaponExLine1(weaponExLine1);
                view.setWeaponExLine2(weaponExLine2);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                continue;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                continue;
            }

            addView(view);
            if (i < unitInfo.getNumberOfWeapons()) {
                addView(createSeparatorTextView());
            }
        }

        requestLayout();
        invalidate();
    }

    View createSeparatorTextView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_unit_detail_separator, this, false);
    }
}
