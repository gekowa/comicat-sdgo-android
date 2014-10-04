package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import cn.sdgundam.comicatsdgo.R;

/**
 * Created by xhguo on 10/3/2014.
 */
public class GDCategorySmallIconView extends View {
    int gdCategory;

    static final int MAX_GD_CATEGORY = 2048;
    ArrayList<Integer> gdCategories;

    // for rendering
    float innerMargin, iconWidth, iconHeight;

    public GDCategorySmallIconView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init () {
        setWillNotDraw(false);

        innerMargin = getResources().getDimension(R.dimen.gd_category_icon_s_inner_margin);
        iconWidth = getResources().getDimension(R.dimen.gd_category_icon_s_width);
        iconHeight = getResources().getDimension(R.dimen.gd_category_icon_s_height);
    }

    public int getGdCategory() {
        return gdCategory;
    }

    public void setGdCategory(int gdCategory) {
        this.gdCategory = gdCategory;

        gdCategories = new ArrayList<Integer>();
        int temp = MAX_GD_CATEGORY;
        while(gdCategory > 0) {
            if(gdCategory >= temp) {
                gdCategories.add(temp);
                gdCategory -= temp;
            }
            temp = temp >> 1;
        }

        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (gdCategories != null && gdCategories.size() > 0) {
            Resources resources = getResources();
            float x = 0;
            for(Integer c : gdCategories) {
                Bitmap b = BitmapFactory.decodeResource(resources,
                        resources.getIdentifier("gdc_icon_s_" + c.toString(), "drawable", getContext().getPackageName()));
                canvas.drawBitmap(b, null, new RectF(x, 0, x + iconWidth, iconHeight), null);
                x += (iconWidth + innerMargin);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (gdCategories != null && gdCategories.size() > 0) {
            float w = getPaddingLeft() + getPaddingRight() + (gdCategories.size() - 1) * innerMargin + gdCategories.size() * iconWidth;
            float h = getPaddingTop() + getPaddingBottom() + iconHeight;

            setMeasuredDimension((int)w, (int)h);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
