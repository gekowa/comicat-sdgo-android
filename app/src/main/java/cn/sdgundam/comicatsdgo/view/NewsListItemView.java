package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.data_model.PostInfo;

/**
 * Created by xhguo on 10/29/2014.
 */
public class NewsListItemView extends View {
    PostInfo postInfo;
    Bitmap gdCategoryDrawingCache;

    public NewsListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPostInfo(PostInfo postInfo) {
        this.postInfo = postInfo;

        GDCategorySmallIconView iconView = new GDCategorySmallIconView(getContext(), null);
        iconView.setGdCategory(postInfo.getGdPostCategory());
        gdCategoryDrawingCache = Utility.convertViewToBitmap(iconView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (postInfo == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = getResources().getDimensionPixelSize(R.dimen.news_list_view_item_height);
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
