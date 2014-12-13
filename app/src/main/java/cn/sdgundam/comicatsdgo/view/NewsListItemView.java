package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.api_model.PostInfo;

/**
 * Created by xhguo on 10/29/2014.
 */
public class NewsListItemView extends View {
    PostInfo postInfo;
    Bitmap gdCategoryDrawingCache;

    static TextPaint titlePaint;
    static TextPaint datePaint;

    public NewsListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        configurePaints();
    }

    void configurePaints() {
        float titleTextSize = getResources().getDimensionPixelSize(R.dimen.news_list_title_text_size);
        float dateTextSize = getResources().getDimensionPixelSize(R.dimen.video_grid_date_text_size);

        if (titlePaint == null) {
            titlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            titlePaint.setColor(Color.BLACK);
            titlePaint.setTextSize(titleTextSize);
        }

        if (datePaint == null) {
            datePaint = new TextPaint();
            datePaint.setColor(Color.GRAY);
            datePaint.setTextSize(dateTextSize);
        }
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

        int width = canvas.getWidth();
        int verticalPadding = getResources().getDimensionPixelSize(R.dimen.news_list_item_vertical_padding);
        int gdCatIconHeight = getResources().getDimensionPixelSize(R.dimen.gd_category_icon_s_height);
        int titleHeight = getResources().getDimensionPixelSize(R.dimen.news_list_item_title_height);

        if (gdCategoryDrawingCache != null) {
            // category icons
            canvas.drawBitmap(gdCategoryDrawingCache, 0, verticalPadding, null);
        }

        StaticLayout layoutTitle = new StaticLayout(postInfo.getTitle(), titlePaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        canvas.translate(0, gdCatIconHeight + verticalPadding * 2);
        layoutTitle.draw(canvas);

        StaticLayout layoutDate = new StaticLayout(Utility.dateStringByDay(getContext(), postInfo.getCreated()),
                datePaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        canvas.translate(0, titleHeight + verticalPadding);
        layoutDate.draw(canvas);

//        canvas.drawText(Utility.dateStringByDay(getContext(), postInfo.getCreated()),
//                0,
//                Utility.convertDpToPixel(6, getContext()) + datePaint.getTextSize() /* Magic number */,
//                datePaint);
    }
}
