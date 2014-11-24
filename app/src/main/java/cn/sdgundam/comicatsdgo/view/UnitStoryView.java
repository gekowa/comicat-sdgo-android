package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.sdgundam.comicatsdgo.R;

/**
 * Created by xhguo on 11/23/2014.
 */
public class UnitStoryView extends LinearLayout {
    String story;

    TextView storyTV;

    public UnitStoryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_unit_story, this, true);


        storyTV = (TextView)findViewById(R.id.story_text_view);
    }

    public void setStory(String story) {
        this.story = story;

        storyTV.setText(story);
    }
}
