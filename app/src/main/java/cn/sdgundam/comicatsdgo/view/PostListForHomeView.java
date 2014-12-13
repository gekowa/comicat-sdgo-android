package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cn.sdgundam.comicatsdgo.PostViewActivity;
import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.api_model.PostInfo;
import cn.sdgundam.comicatsdgo.extension.BorderedTableRow;

/**
 * Created by xhguo on 10/8/2014.
 */
public class PostListForHomeView extends TableLayout {
    public PostListForHomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    PostInfo[] posts;

    public void setPosts(PostInfo[] posts) {
        this.posts = posts;

        this.removeAllViews();

        boolean hasOpenRow = false;
        BorderedTableRow openRow = new BorderedTableRow(getContext());

        TableRow.LayoutParams layoutParams =
                new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;

        PostInfo last = posts[posts.length - 1];

        for (int i = 0; i < posts.length; i++) {
            PostInfo p = posts[i];

            // create text view
            View itemView = createPostListForHomeItemView(p);
            itemView.setPadding(18, 18, 18, 18);
            itemView.setLayoutParams(layoutParams);

            if (p.getListStyle() == 1) {
                // full
                hasOpenRow = false;

                layoutParams.span = 2;

                openRow = new BorderedTableRow(getContext());
                openRow.addView(itemView);

                this.addView(openRow);
            } else if (p.getListStyle() == 2) {
                // half
                layoutParams.span = 1;

                if (hasOpenRow) {
                    openRow.addView(itemView);

                    hasOpenRow = false;
                } else {
                    openRow = new BorderedTableRow(getContext());
                    openRow.addView(itemView);

                    this.addView(openRow);

                    hasOpenRow = true;
                }

                openRow.setBorder(openRow.getBorder() | BorderedTableRow.BORDER_CENTER_SPLIT);
            }

            if (i == posts.length - 1 /* last one */||
                    i == posts.length - 2 && p.getListStyle() == 2 && last.getListStyle() != 1) {

            } else {
                openRow.setBorder(openRow.getBorder() | BorderedTableRow.BORDER_BOTTOM);
            }
        }

        requestLayout();
        invalidate();
    }

    View createPostListForHomeItemView (final PostInfo p) {
        View v = View.inflate(getContext(), R.layout.post_list_for_home_item, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(getResources().getDrawable(R.drawable.list_selector_common));
        }
        else {
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_selector_common));
        }

        TextView textView = (TextView) v.findViewById(R.id.text_view);
        textView.setText(p.getTitle());


        GDCategorySmallIconView iconView = (GDCategorySmallIconView) v.findViewById(R.id.gd_icon_small);
        iconView.setGdCategory(p.getGdPostCategory());

        TextView dateView = (TextView)v.findViewById(R.id.date_text_view);
        dateView.setText(Utility.dateStringByDay(getContext(), p.getCreated()));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(),
//                        String.format("PostId: %s clicked", p.getPostId()), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), PostViewActivity.class);
                Bundle b = new Bundle();
                b.putInt("id", p.getPostId());

                intent.putExtras(b);
                getContext().startActivity(intent);
            }
        });

        return v;

    }
}
