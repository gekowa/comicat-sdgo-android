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
 * Created by xhguo on 11/22/2014.
 */
public class UnitSkillsView extends LinearLayout {
    UnitInfo unitInfo;

    public UnitSkillsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(VERTICAL);
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;

        for (int i = 1; i <= 3; i++) {
            UnitSingleSkillView view;
            view = new UnitSingleSkillView(getContext(), null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            try {
                int skillId = (Integer)UnitInfo.class.getDeclaredMethod("getSkill" + i).invoke(unitInfo);

                String skillName = (String)UnitInfo.class.getDeclaredMethod("getSkillName" + i).invoke(unitInfo);
                String skillDesc = (String)UnitInfo.class.getDeclaredMethod("getSkillDesc" + i).invoke(unitInfo);
                String skillEx = (String)UnitInfo.class.getDeclaredMethod("getSkillEx" + i).invoke(unitInfo);

                view.setSkillId(skillId);
                view.setSkillName(skillName);
                view.setSkillDesc(skillDesc);
                view.setSkillEx(skillEx);

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
            if (i < 3) {
                addView(createSeparatorTextView());
            }

            requestLayout();
            invalidate();
        }
    }

    View createSeparatorTextView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_unit_detail_separator, this, false);
    }
}
