package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.InvocationTargetException;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.api_model.UnitInfo;

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
                int weaponId = (Integer)UnitInfo.class.getDeclaredMethod("getWeapon" + i).invoke(unitInfo);

                String weaponName = (String)UnitInfo.class.getDeclaredMethod("getWeaponName" + i).invoke(unitInfo);
                String weaponProperty = (String)UnitInfo.class.getDeclaredMethod("getWeaponProperty" + i).invoke(unitInfo);
                String weaponEffect = (String)UnitInfo.class.getDeclaredMethod("getWeaponEffect" + i).invoke(unitInfo);
                String weaponRange = (String)UnitInfo.class.getDeclaredMethod("getWeaponRange" + i).invoke(unitInfo);
                String weaponExLine1 = (String)UnitInfo.class.getDeclaredMethod(String.format("getWeaponEx%sLine1", i)).invoke(unitInfo);
                String weaponExLine2 = (String)UnitInfo.class.getDeclaredMethod(String.format("getWeaponEx%sLine2", i)).invoke(unitInfo);

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
