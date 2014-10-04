package cn.sdgundam.comicatsdgo;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cn.sdgundam.comicatsdgo.data_model.PostInfo;
import cn.sdgundam.comicatsdgo.extension.BorderedTableRow;
import cn.sdgundam.comicatsdgo.view.GDCategorySmallIconView;
import cn.sdgundam.comicatsdgo.view.PostListForHomeItemView;

/**
 * Created by xhguo on 9/30/2014.
 */
public class PostListForHomeFragment extends Fragment {
    TableLayout table;

    PostInfo[] posts;

    public void setPosts(PostInfo[] posts) {
        this.posts = posts;

        table.removeAllViews();

        boolean hasOpenRow = false;
        BorderedTableRow openRow = new BorderedTableRow(getActivity());

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

                openRow = new BorderedTableRow(getActivity());
                openRow.addView(itemView);

                table.addView(openRow);
            } else if (p.getListStyle() == 2) {
                // half
                layoutParams.span = 1;

                if (hasOpenRow) {
                    openRow.addView(itemView);

                    hasOpenRow = false;
                } else {
                    openRow = new BorderedTableRow(getActivity());
                    openRow.addView(itemView);

                    table.addView(openRow);

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
    }

    View createPostListForHomeItemView (PostInfo p) {
        View v = View.inflate(getActivity(), R.layout.post_list_for_home_item, null);

        TextView textView = (TextView) v.findViewById(R.id.text_view);
        textView.setText(p.getTitle());


        GDCategorySmallIconView iconView = (GDCategorySmallIconView) v.findViewById(R.id.gd_icon_small);
        iconView.setGdCategory(p.getGdPostCategory());

        TextView dateView = (TextView)v.findViewById(R.id.date_text_view);
        dateView.setText(Utility.dateStringByDay(getActivity(), p.getCreated()));

        return v;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return table = (TableLayout) inflater.inflate(R.layout.fragment_post_list_for_home, container, false);
    }


}
