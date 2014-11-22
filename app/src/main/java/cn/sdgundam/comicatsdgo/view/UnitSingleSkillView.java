package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.sdgundam.comicatsdgo.R;

/**
 * Created by xhguo on 11/22/2014.
 */
public class UnitSingleSkillView extends LinearLayout {
    int skillId;
    String skillName;
    String skillDesc;
    String skillEx;

    ImageView skillImageView;
    TextView skillNameTextView;
    TextView skillDescTextView;
    TextView skillExTextView;

    public UnitSingleSkillView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnitSingleSkillView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.view_unit_single_skill, this, true);

        skillImageView = (ImageView)findViewById(R.id.skill_image_view);
        skillNameTextView = (TextView)findViewById(R.id.skill_name_text_view);
        skillDescTextView = (TextView)findViewById(R.id.skill_desc_text_view);
        skillExTextView = (TextView)findViewById(R.id.skill_ex_text_view);
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;

        Picasso.with(getContext())
                .load(Uri.parse(String.format("http://cdn.sdgundam.cn/data-source/acc/skill/%s.gif", skillId)))
                .into(skillImageView);
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;

        skillNameTextView.setText(skillName);
    }

    public void setSkillDesc(String skillDesc) {
        this.skillDesc = skillDesc;

        skillDescTextView.setText(skillDesc);
    }

    public void setSkillEx(String skillEx) {
        this.skillEx = skillEx;

        if (skillEx.length() == 0) {
            skillExTextView.setVisibility(GONE);
        } else {
            skillExTextView.setText(skillEx);
        }
    }
}
