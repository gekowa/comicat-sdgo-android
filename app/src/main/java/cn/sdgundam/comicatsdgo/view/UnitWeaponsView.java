package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

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

        LayoutInflater.from(context).inflate(R.layout.view_unit_weapons, this, true);
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;

        for (int i = 1; i <= unitInfo.getNumberOfWeapons(); i++) {
            UnitSingleWeaponView view = null;
            try {
                Field field = R.id.class.getDeclaredField("unit_weapon_" + i);
                view = (UnitSingleWeaponView)findViewById(field.getInt(field));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                continue;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            if (view == null) {
                continue;
            }

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

            view.setVisibility(VISIBLE);
        }

        requestLayout();
        invalidate();
    }

//    class UnitWeaponListAdapter extends BaseAdapter {
//        Context context;
//        UnitInfo unitInfo;
//
//        public UnitWeaponListAdapter(Context context, UnitInfo unitInfo) {
//            this.context = context;
//            this.unitInfo = unitInfo;
//        }
//
//        @Override
//        public int getCount() {
//            return unitInfo.getNumberOfWeapons();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return unitInfo;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            UnitSingleWeaponView view = new UnitSingleWeaponView(getContext(), null);
//            view.setLayoutParams(new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            ));
//            int weaponIndex = position + 1;
//            view.setWeaponIndex(weaponIndex);
//
//            try {
//                int weaponId = (Integer)UnitInfo.class.getDeclaredMethod("getWeapon" + weaponIndex, null).invoke(unitInfo);
//                String weaponName = (String)UnitInfo.class.getDeclaredMethod("getWeaponName" + weaponIndex, null).invoke(unitInfo);
//                String weaponProperty = (String)UnitInfo.class.getDeclaredMethod("getWeaponProperty" + weaponIndex, null).invoke(unitInfo);
//                String weaponEffect = (String)UnitInfo.class.getDeclaredMethod("getWeaponEffect" + weaponIndex, null).invoke(unitInfo);
//                String weaponRange = (String)UnitInfo.class.getDeclaredMethod("getWeaponRange" + weaponIndex, null).invoke(unitInfo);
//                String weaponExLine1 = (String)UnitInfo.class.getDeclaredMethod(String.format("getWeaponEx%sLine1", weaponIndex), null).invoke(unitInfo);
//                String weaponExLine2 = (String)UnitInfo.class.getDeclaredMethod(String.format("getWeaponEx%sLine2", weaponIndex), null).invoke(unitInfo);
//
//                view.setWeaponId(weaponId);
//                view.setWeaponName(weaponName);
//                view.setWeaponProperty(weaponProperty);
//                view.setWeaponEffect(weaponEffect);
//                view.setWeaponRange(weaponRange);
//                view.setWeaponExLine1(weaponExLine1);
//                view.setWeaponExLine2(weaponExLine2);
//
//            }
//            catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//            catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//            catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//
//            return view;
//        }
//    }
}
